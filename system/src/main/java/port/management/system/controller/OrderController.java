package port.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.dto.OrderDTO;
import port.management.system.dto.OrderDTO2;
import port.management.system.security.utility.UserUtil;
import port.management.system.service.OrderServiceImpl;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private UserUtil userUtil;

    @PostMapping("/public/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody OrderDTO2 orderDTO2) {

        return new ResponseEntity<>(orderService.orderProducts(paymentMethod, orderDTO2), HttpStatus.OK);

    }

    @PostMapping("/public/order/cancel/product-id/{productId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long productId) {

        return new ResponseEntity<>(orderService.cancelOrder(productId), HttpStatus.OK);

    }

}
