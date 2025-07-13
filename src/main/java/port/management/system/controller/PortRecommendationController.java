package port.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import port.management.system.dto.PortRecommendation;
import port.management.system.exception.ApiException;
import port.management.system.model.User;
import port.management.system.security.utility.UserUtil;
import port.management.system.service.PortRecommendationServiceImpl;

@RestController
@RequestMapping("/api")
public class PortRecommendationController {

    @Autowired
    private PortRecommendationServiceImpl portRecommendationService;

    @Autowired
    private UserUtil userUtil;

    @GetMapping("/public/recommend/ports")
    public ResponseEntity<PortRecommendation> getRecommendedPorts() {

        User user = userUtil.getCurrentUser().getBody();
        if (user == null) {
            throw new ApiException("Unable to fetch current user");
        }

        return ResponseEntity.ok(portRecommendationService.getRecommendedPorts(user.getUserId()));

    }

}
