package port.management.system.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import port.management.system.model.*;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.dto.*;
import port.management.system.repository.CartRepository;
import port.management.system.repository.ContainerRepository;
import port.management.system.repository.PortRepository;
import port.management.system.repository.ProductRepository;
import port.management.system.security.utility.UserUtil;
import port.management.system.utility.BuildProduct;
import port.management.system.utility.ValidateProduct;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ValidateProduct validateProduct;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private BuildProduct buildProduct;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PortInteractionServiceImpl portInteractionService;

    @Override
    public ProductResponse2 addNewProductsBySourceAndDestinationPortId(Long sourcePortId, Long destinationPortId, @Valid List<ProductDTO2> productDTO2List) {

        Port sourcePort = portRepository.findById(sourcePortId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", sourcePortId));

        Port destinationPort = portRepository.findById(destinationPortId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", destinationPortId));

        User loggedInUser = userUtil.getCurrentUser().getBody();

        if (loggedInUser == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        int currentProductStrength = sourcePort.getProductsFromSource().size() - sourcePort.getContainers().stream().mapToInt(c -> c.getProducts().size()).sum() + productDTO2List.size();
        int maxProductsCapacity = sourcePort.getMaxProductsCapacity();

        if (currentProductStrength > maxProductsCapacity) {

            int maxProductsUtilization = 0;

            for (int i = (currentProductStrength-productDTO2List.size()) ; i < maxProductsCapacity ; i++) {

                maxProductsUtilization += 1;

            }

            throw new ApiException("Maximum of : " + maxProductsUtilization + " products can be assigned to the port with portId : " + sourcePortId);

        }

        List<Product> products = productDTO2List.stream()
                .map(p -> Product.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .productDescription(p.getProductDescription())
                        .productWeightInKg(p.getProductWeightInKg())
                        .productStatus("Product Accepted")
                        .isPaymentPending(true)
                        .sourcePort(sourcePort)
                        .currentProductPort(sourcePort)
                        .destinationPort(destinationPort)
                        .user(loggedInUser)
                        .container(null)
                        .build()
                )
                .toList();

        productRepository.saveAll(products);

//        Setting Cart for the Products.
        Double totalWeight = products.stream().mapToDouble(Product::getProductWeightInKg).sum();

        ProductCart cart = loggedInUser.getCart();

        if (cart == null) {

            ProductCart newCart = ProductCart.builder()
                    .productQuantity(products.size())
                    .products(products)
                    .totalCartWeight(totalWeight)
                    .user(loggedInUser)
                    .build();

            for (Product product : products){

                product.setCart(newCart);

            }

            newCart.setProducts(products);

//        Setting Cart to the User.
            loggedInUser.setCart(newCart);

            ProductCart savedCart = cartRepository.save(newCart);

            if (savedCart.getProducts().isEmpty()) {

                throw new ApiException("Unable to add the Products List.");

            }

        } else {

            cart.setProductQuantity(cart.getProductQuantity() + products.size());
            cart.setTotalCartWeight(cart.getTotalCartWeight() + totalWeight);

            for (Product product : products) {

                product.setCart(cart);
                cart.getProducts().add(product);

            }

            ProductCart savedCart = cartRepository.save(cart);

            if (savedCart.getProducts().isEmpty()) {

                throw new ApiException("Unable to add the Products List.");

            }

        }

        portInteractionService.logInteraction(loggedInUser, sourcePort, InteractionType.BOOKED);

        List<Product> savedProducts = productRepository.saveAll(products);

        return buildProduct.buildProductResponse2(savedProducts, sourcePort, destinationPort);

    }

    @Override
    public TrackProductResponse trackProductStatusByProductId(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getProductStatus().equalsIgnoreCase("cancelled")) {

            throw new ApiException("The Product with the provided productId has been cancelled.");

        }

        return TrackProductResponse.builder()
                .userId(product.getUser().getUserId())
                .username(product.getUser().getUsername())
                .email(product.getUser().getEmail())
                .mobile(product.getUser().getMobile())
                .role(product.getUser().getRole())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .containerId(product.getContainer() != null
                        ? product.getContainer().getContainerId()
                        : null
                )
                .shipId(product.getContainer() == null
                        ? null
                        : product.getContainer().getShip() == null
                        ? null
                        : product.getContainer().getShip().getShipId()
                )
                .sourcePortName(product.getSourcePort().getPortName())
                .currentPortName(product.getCurrentProductPort().getPortName())
                .destinationPortName(product.getDestinationPort().getPortName())
                .build();

    }

    @Override
    public ProductResponse3 updateProductById(Long productId, ProductDTO2 productDTO2) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (existingProduct.getProductStatus().equalsIgnoreCase("cancelled")) {

            throw new ApiException("The Product with the provided productId has been cancelled.");

        }

        existingProduct.setProductName(productDTO2.getProductName());
        existingProduct.setProductDescription(productDTO2.getProductDescription());
        existingProduct.setProductWeightInKg(productDTO2.getProductWeightInKg());

        Port currentProductPort = existingProduct.getCurrentProductPort();

        if (currentProductPort == null) {

            throw new ApiException("No Port is assigned for the product with productId : " + productId);

        }

        Product savedProduct = productRepository.save(existingProduct);

        return buildProduct.buildProductResponse3(savedProduct);

    }

    @Override
    public ProductResponse getAllProductsByCurrentProductPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Port currentProductPort = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByCurrentProductPort(currentProductPort, pageDetails);

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) {

            throw new ApiException("No Products Created for the Port : " + currentProductPort.getPortName() + " till now.");

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, currentProductPort, InteractionType.VIEWED);

        return buildProduct.buildProductResponse(products, productPage);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) {

            throw new ApiException("No Products are created till now.");

        }

        return buildProduct.buildProductResponse(products, productPage);

    }

    @Override
    public ProductResponse getAllProductsByName(String productName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(productName, pageDetails);

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) {

            throw new ApiException("No such Product exists with the Product Name : " + productName);

        }

        return buildProduct.buildProductResponse(products, productPage);

    }

    @Override
    public ProductResponse3 getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        return buildProduct.buildProductResponse3(product);

    }

    @Override
    public String removeProductById(Long productId) {

        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(productToDelete);

        return "Product with the productId : " + productId + " removed Successfully.";

    }

    @Override
    @Transactional
    public ProductResponse3 addProductToContainer(Long productId, Long containerId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        if (validateProduct.isProductValidBeforeAssignment(existingProduct, existingContainer)) {

            existingProduct.setContainer(existingContainer);
            existingContainer.getProducts().add(existingProduct);

        }

        containerRepository.save(existingContainer);
        Product savedProduct = productRepository.save(existingProduct);

        return buildProduct.buildProductResponse3(savedProduct);

    }

    @Override
    public ProductResponse getAllProductsByContainer(Long containerId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByContainer(existingContainer, pageDetails);

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) {

            throw new ApiException("No products are assigned to the Container with containerId : " + containerId);

        }

        return buildProduct.buildProductResponse(products, productPage);

    }

    @Override
    @Transactional
    public ProductResponse3 unassignProductFromContainer(Long productId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Container existingContainer = existingProduct.getContainer();

        if (existingContainer == null) {

            throw new ApiException("No Container is assigned to the Product.");

        }

//        Removing Product from the Container.
        existingContainer.getProducts().remove(existingProduct);
        existingProduct.setContainer(null);

//        Checking whether the Port has the capacity to store the Product which is unassigned from the Container.
        Port productPort = existingContainer.getPort();
        if (productPort.getProducts().size() - productPort.getContainers().stream().mapToInt(c -> c.getProducts().size()).sum() >= productPort.getMaxProductsCapacity()) {

            throw new ApiException("Can't unassign Product from the Container as the Port's capacity to store the Products have reached to it's Maximum Limits.");

        }

//        Setting the current port of the product same as that of container's port.
        existingProduct.setCurrentProductPort(productPort);

        containerRepository.save(existingContainer);

        Product updatedProduct = productRepository.save(existingProduct);

        return buildProduct.buildProductResponse3(updatedProduct);

    }

}
