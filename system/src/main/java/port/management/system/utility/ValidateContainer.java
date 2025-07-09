package port.management.system.utility;

import org.springframework.stereotype.Component;
import port.management.system.model.Container;
import port.management.system.model.Ship;
import port.management.system.exception.ApiException;

@Component
public class ValidateContainer {

    public boolean isContainerValidBeforeAssignment(Container container, Ship ship) {

        if (container.getShip() != null && container.getShip().getShipId().equals(ship.getShipId())) {

            throw new ApiException("The Container with containerId : " + container.getContainerId() + " is already assigned to this ship.");

        }

        if (container.getShip() != null) {

            throw new ApiException("The Container with containerId : " + container.getContainerId() + " is already assigned to the ship with shipId : " + container.getShip().getShipId());

        }

        if (!container.getPort().getPortId().equals(ship.getPort().getPortId())) {

            throw new ApiException("The Container and Ship does not belongs to the same port.");

        }

        if (container.getProducts().isEmpty()) {

            throw new ApiException("Can't assign empty Container with containerId : " + container.getContainerId() + " into the Ship with shipId : " + ship.getShipId());

        }

        return true;

    }

}
