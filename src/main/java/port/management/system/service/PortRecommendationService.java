package port.management.system.service;

import port.management.system.dto.PortRecommendation;

public interface PortRecommendationService {

    PortRecommendation getRecommendedPorts(Long userId);

}
