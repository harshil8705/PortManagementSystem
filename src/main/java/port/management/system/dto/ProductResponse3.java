package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse3 {

    private UserDTO2 userDTO2;
    private PortDTO currentProductPort;
    private PortDTO sourcePort;
    private ProductDTO productDTO;
    private PortDTO destinationPort;

}
