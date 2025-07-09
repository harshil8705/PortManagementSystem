package port.management.system.service;

import port.management.system.model.InteractionType;
import port.management.system.model.Port;
import port.management.system.model.User;

public interface PortInteractionService {

    void logInteraction(User user, Port port, InteractionType interactionType);

}
