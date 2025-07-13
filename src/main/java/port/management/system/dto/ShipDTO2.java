package port.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipDTO2 {

    private Long shipId;
    private String shipName;
    private int maxContainersCapacity;

}
