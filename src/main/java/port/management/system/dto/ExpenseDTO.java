package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long expenseId;
    private Double amountPaid;
    private LocalDateTime paymentDate;
    private UserDTO2 userDTO2;

}
