package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortAnalysisDTO {

    private Long portId;
    private String portName;
    private Integer maxShipsCapacity;
    private Integer maxContainersCapacity;
    private Integer maxProductsCapacity;
    private Integer initialShipStrength;
    private Integer initialContainerStrength;
    private Integer initialProductsStrength;
    private Double shipStrengthPercent;
    private Double containerStrengthPercent;
    private Double productStrengthPercent;
    private Double portFilledPercent;

}
