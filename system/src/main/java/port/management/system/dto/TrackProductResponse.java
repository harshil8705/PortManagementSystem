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
public class TrackProductResponse {

    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private AppRole role;
    private Long productId;
    private String productName;
    private String productDescription;
    private Long containerId;
    private Long shipId;
    private String sourcePortName;
    private String currentPortName;
    private String destinationPortName;

}
