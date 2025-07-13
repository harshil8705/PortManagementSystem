package port.management.system.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    private String username;
    private String email;
    private String mobile;
    private String password;
    private String secretKey;

}
