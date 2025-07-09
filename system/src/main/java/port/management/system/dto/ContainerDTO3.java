package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDTO3 {

    private Long containerId;
    private String containerName;
    private double maxWeightCapacity;
    private ShipDTO2 shipDTO2;

}
