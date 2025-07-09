package port.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import port.management.system.model.InteractionType;
import port.management.system.model.Port;
import port.management.system.model.PortInteraction;
import port.management.system.model.User;
import port.management.system.repository.PortInteractionRepository;

import java.time.LocalDateTime;

@Service
public class PortInteractionServiceImpl implements PortInteractionService{

    @Autowired
    private PortInteractionRepository portInteractionRepository;

    @Override
    public void logInteraction(User user, Port port, InteractionType interactionType) {

        PortInteraction portInteraction = PortInteraction.builder()
                .user(user)
                .port(port)
                .interactedAt(LocalDateTime.now())
                .interactionType(interactionType)
                .build();

        portInteractionRepository.save(portInteraction);

    }

}
