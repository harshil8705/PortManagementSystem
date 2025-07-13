package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import port.management.system.model.AppRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO2 {

    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private AppRole role;


}
