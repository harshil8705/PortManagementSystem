package port.management.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortRecommendation {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("recommended_ports")
    private List<String> recommendedPorts;

}
