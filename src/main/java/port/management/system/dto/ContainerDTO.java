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
public class ContainerDTO {

    private Long containerId;
    private String containerName;
    private double maxWeightCapacity;
    private PortDTO portDTO;
    private ShipDTO2 shipDTO2;
    private List<ProductDTO2> productDTO2List;

}
