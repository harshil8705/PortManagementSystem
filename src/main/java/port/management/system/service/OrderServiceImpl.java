package port.management.system.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import port.management.system.dto.OrderDTO;
import port.management.system.dto.OrderDTO2;
import port.management.system.dto.PaymentDTO;
import port.management.system.dto.ProductDTO2;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.model.*;
import port.management.system.repository.*;
import port.management.system.security.utility.UserUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PortInteractionServiceImpl portInteractionService;

    @Override
    @Transactional
    public OrderDTO orderProducts(String paymentMethod, OrderDTO2 orderDTO2) {

        Address address = addressRepository.findById(orderDTO2.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", orderDTO2.getAddressId()));

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        ProductCart cart = user.getCart();
        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {

            throw new ApiException("Cart is empty or not available.");

        }

        List<Product> products = cart.getProducts().stream()
                .filter(Product::isPaymentPending)
                .collect(Collectors.toList());

        if (products.isEmpty()) {

            throw new ApiException("No Products are pending for Payment.");

        }

        double totalPrice = products.stream().mapToDouble(Product::getProductWeightInKg).sum() * 500;

        Order order = Order.builder()
                .orderDate(LocalDate.now())
                .orderStatus("Order Accepted!")
                .email(user.getEmail())
                .products(products)
                .user(user)
                .totalPrice(totalPrice)
                .build();

        for (Product product : products) {

            product.setOrder(order);
            product.setPaymentPending(false);

        }

        Order savedOrder = orderRepository.save(order);

        user.getOrders().add(order);
        userRepository.save(user);

        Payment payment = Payment.builder()
                .pgPaymentId(orderDTO2.getPgPaymentId())
                .pgName(orderDTO2.getPgName())
                .pgStatus(orderDTO2.getPgStatus())
                .paymentMethod(paymentMethod)
                .pgResponseMessage(orderDTO2.getPgResponseMessage())
                .order(order)
                .build();

        order.setPayment(payment);
        Payment savedPayment = paymentRepository.save(payment);

//        Setting the Expense Tracking.
        Expense expense = Expense.builder()
                .amountPaid(savedOrder.getTotalPrice())
                .paymentDate(LocalDateTime.now())
                .user(user)
                .order(savedOrder)
                .build();

        expenseRepository.save(expense);

        user.getExpenses().add(expense);
        order.setExpense(expense);

        orderRepository.save(savedOrder);
        userRepository.save(user);

//        Clearing the User Cart
        cart.getProducts().clear();
        cart.setProductQuantity(0);
        cart.setTotalCartWeight(0.0);
        cartRepository.save(cart);

        return OrderDTO.builder()
                .orderId(savedOrder.getOrderId())
                .email(savedOrder.getEmail())
                .orderDate(savedOrder.getOrderDate())
                .totalPrice(savedOrder.getTotalPrice())
                .orderStatus(savedOrder.getOrderStatus())
                .productDTO2List(products.stream()
                        .map(p -> ProductDTO2.builder()
                                .productId(p.getProductId())
                                .productName(p.getProductName())
                                .productDescription(p.getProductDescription())
                                .productWeightInKg(p.getProductWeightInKg())
                                .build())
                        .toList())
                .addressId(address.getAddressId())
                .payment(PaymentDTO.builder()
                        .paymentId(savedPayment.getPaymentId())
                        .paymentMethod(savedPayment.getPaymentMethod())
                        .pgName(savedPayment.getPgName())
                        .pgPaymentId(savedPayment.getPgPaymentId())
                        .pgResponseMessage(savedPayment.getPgResponseMessage())
                        .pgStatus(savedPayment.getPgStatus())
                        .build())
                .build();

    }

    @Override
    @Transactional
    public String cancelOrder(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getContainer() != null && product.getContainer().getShip() != null) {

            throw new ApiException("The Product is assigned to the ship hence the we cannot Cancel this Order.");

        }

        Order productOrder = product.getOrder();

        if (productOrder == null) {

            throw new ApiException("No Order found for the given Product.");

        }

        User loggedInUser = userUtil.getCurrentUser().getBody();

        if (loggedInUser == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        if (loggedInUser.getProducts() == null) {

            throw new ApiException("No products are owned by you.");

        }

        if (!loggedInUser.getProducts().contains(product)) {

            throw new ApiException("This product does not belongs to you.");

        }

        productOrder.getProducts().remove(product);
        loggedInUser.getProducts().remove(product);
        product.setOrder(null);
        product.setContainer(null);

        if (productOrder.getProducts().isEmpty()) {

            productOrder.setOrderStatus("Cancelled");

        }
        product.setProductStatus("Cancelled");

        double refundAmount = product.getProductWeightInKg() * 500;
        User associatedUser = productOrder.getUser();
        Port currentPort = product.getCurrentProductPort();

        orderRepository.save(productOrder);
        productRepository.delete(product);

        associatedUser.setRefundWallet((associatedUser.getRefundWallet() != null ? associatedUser.getRefundWallet() : 0.0) + refundAmount);

        Expense refundExpense = Expense.builder()
                .order(productOrder)
                .user(associatedUser)
                .paymentDate(LocalDateTime.now())
                .amountPaid(-refundAmount)
                .build();
        expenseRepository.save(refundExpense);

        associatedUser.getExpenses().add(refundExpense);
        userRepository.save(associatedUser);

        portInteractionService.logInteraction(associatedUser, currentPort, InteractionType.CANCELLED);

        return "Order for product ID " + productId + " has been cancelled and ₹" + refundAmount + " has been refunded.";

    }

}
