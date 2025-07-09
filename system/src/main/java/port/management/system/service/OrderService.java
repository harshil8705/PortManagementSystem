package port.management.system.service;

import port.management.system.dto.OrderDTO;
import port.management.system.dto.OrderDTO2;

public interface OrderService {

    OrderDTO orderProducts(String paymentMethod, OrderDTO2 orderDTO2);

    String cancelOrder(Long productId);

}
