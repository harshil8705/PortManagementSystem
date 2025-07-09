package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse2 {

    private UserDTO2 userDTO2;
    private PortDTO currentProductPort;
    private PortDTO sourcePort;
    private List<ProductDTO> productDTOList;
    private PortDTO destinationPort;

}
