package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortDTO2 {

    private Long portId;
    private String portName;
    private Integer maxShipsCapacity;
    private Integer maxContainersCapacity;
    private Integer maxProductsCapacity;

}
