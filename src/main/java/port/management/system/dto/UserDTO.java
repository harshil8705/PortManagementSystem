package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import port.management.system.model.AppRole;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private String email;
    private String mobile;
    private AppRole role;
    private List<ProductDTO> productDTOList;

}
