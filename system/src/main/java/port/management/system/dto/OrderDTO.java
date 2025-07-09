package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;
    private String email;
    private LocalDate orderDate;
    private Double totalPrice;
    private String orderStatus;
    private PaymentDTO payment;
    private List<ProductDTO2> productDTO2List;
    private Long addressId;

}
