package port.management.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import port.management.system.dto.PortRecommendation;
import port.management.system.model.Port;
import port.management.system.repository.PortRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortRecommendationServiceImpl implements PortRecommendationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PortRepository portRepository;

    @Override
    public PortRecommendation getRecommendedPorts(Long userId) {

        String url = "http://localhost:5000/recommend/" + userId;

        try {

            PortRecommendation aiRecommendation = restTemplate.getForObject(url, PortRecommendation.class);

            if (aiRecommendation == null || aiRecommendation.getRecommendedPorts() == null || aiRecommendation.getRecommendedPorts().isEmpty()) {

                return fallbackRecommendation(userId);

            }

            return aiRecommendation;

        } catch (Exception e) {

            // fallback if Python microservice fails
            return fallbackRecommendation(userId);

        }

    }

    private PortRecommendation fallbackRecommendation(Long userId) {

        List<Port> popularPorts = portRepository.findTop5MostInteractedPorts(PageRequest.of(0, 5));

        List<String> fallbackPortNames = popularPorts.stream()
                .map(Port::getPortName)
                .toList();

        return PortRecommendation.builder()
                .userId(userId)
                .recommendedPorts(fallbackPortNames)
                .build();

    }

}
