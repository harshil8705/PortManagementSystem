package port.management.system.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import port.management.system.model.*;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.dto.*;
import port.management.system.repository.ContainerRepository;
import port.management.system.repository.PortRepository;
import port.management.system.repository.ProductRepository;
import port.management.system.repository.ShipRepository;
import port.management.system.security.utility.UserUtil;
import port.management.system.utility.BuildShip;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService{

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuildShip buildShip;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private PortInteractionServiceImpl portInteractionService;

    @Override
    public ShipDTO addNewShipByPortId(Long portId, Ship ship) {

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        int currentShipCount = port.getShips().size();
        int maxShipsCount = port.getMaxShipsCapacity();

        if (currentShipCount >= maxShipsCount) {

            throw new ApiException("Maximum limit for holding the Ships has been reached for this Port! Hence the ship can't be assigned.");

        }

        ship.setPort(port);

        Ship savedShip = shipRepository.save(ship);

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        return buildShip.buildShipDTO(savedShip);

    }

    @Override
    public ShipDTO updateShipById(Long shipId, ShipDTO2 shipDTO2) {

        Ship existingShip = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        existingShip.setShipName(shipDTO2.getShipName());
        existingShip.setMaxContainersCapacity(shipDTO2.getMaxContainersCapacity());

        Port port = existingShip.getPort();

        if (port == null) {

            throw new ApiException("No Port is assigned for the ship with shipId : " + shipId);

        }

        Ship savedShip = shipRepository.save(existingShip);

        return buildShip.buildShipDTO(savedShip);

    }

    @Override
    public ShipResponse2 getAllShipsByPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Ship> shipPage = shipRepository.findByPort(port, pageDetails);

        List<Ship> ships = shipPage.getContent();

        if (ships.isEmpty()) {

            throw new ApiException("No Ships Created for the Port : " + port.getPortName() + " till now.");

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        List<ShipDTO2> shipDTO2List = ships.stream()
                .map(s -> ShipDTO2.builder()
                        .shipId(s.getShipId())
                        .shipName(s.getShipName())
                        .maxContainersCapacity(s.getMaxContainersCapacity())
                        .build())
                .toList();

        ShipResponse2 shipResponse2 = new ShipResponse2();

        shipResponse2.setShipDTO2List(shipDTO2List);
        shipResponse2.setPageNumber(shipPage.getNumber());
        shipResponse2.setPageSize(shipPage.getSize());
        shipResponse2.setTotalPages(shipPage.getTotalPages());
        shipResponse2.setTotalElements(shipPage.getTotalElements());
        shipResponse2.setLastPage(shipPage.isLast());

        return shipResponse2;

    }

    @Override
    public ShipResponse getAllShips(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Ship> shipPage = shipRepository.findAll(pageDetails);

        List<Ship> ships = shipPage.getContent();

        if (ships.isEmpty()) {

            throw new ApiException("No Ships are created till now.");

        }

        return buildShip.buildShipResponse(ships, shipPage);

    }

    @Override
    public ShipResponse getAllShipsByName(String shipName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Ship> shipPage = shipRepository.findByShipNameContainingIgnoreCase(shipName, pageDetails);

        List<Ship> ships = shipPage.getContent();

        if (ships.isEmpty()) {

            throw new ApiException("No such Ship exists with the Ship Name : " + shipName);

        }

        return buildShip.buildShipResponse(ships, shipPage);

    }

    @Override
    public ShipDTO getShipById(Long shipId) {

        Ship ship = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        return buildShip.buildShipDTO(ship);

    }

    @Override
    public String removeShipById(Long shipId) {

        Ship shipToDelete = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        shipRepository.delete(shipToDelete);

        return "Ship with the shipId : " + shipId + " removed Successfully.";

    }

    @Override
    @Transactional
    public ShipDTO changePortOfShip(Long shipId, Long portId) {

        Ship existingShip = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        Port destinationPort = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        if (destinationPort.getShips().size() >= destinationPort.getMaxShipsCapacity()) {

            throw new ApiException("The Port with portId : " + portId + " has currently reached it's maximum Ship Capacity. Hence Port for this Ship can't be changed.");

        }

        if (existingShip.getPort().getPortId().equals(destinationPort.getPortId())) {

            throw new ApiException("This Ship with shipId : " + shipId + " is already present at the Port with portId : " + portId);

        }

        Port sourcePort = existingShip.getPort();

//        Finding the list of Containers which are assigned to the Ship whose port we need to change.
        List<Container> existingShipContainer = existingShip.getContainers();

//        Changing the Port for each Container and Product that were assigned to the Ship whose port we need to change.
        if (!existingShipContainer.isEmpty()) {

            for (Container container : existingShipContainer) {

                List<Product> existingContainerProduct = container.getProducts();
                for (Product product : existingContainerProduct) {

                    product.setCurrentProductPort(destinationPort);
                    destinationPort.getProducts().add(product);
                    sourcePort.getProducts().remove(product);
                    product.setContainer(container);
                    productRepository.save(product);

                }

                container.setPort(destinationPort);
                destinationPort.getContainers().add(container);
                sourcePort.getContainers().remove(container);
                container.setProducts(existingContainerProduct);
                containerRepository.save(container);

            }

        }

//        Changing Port of the Ship
        existingShip.setPort(destinationPort);
        sourcePort.getShips().remove(existingShip);
        destinationPort.getShips().add(existingShip);

        portRepository.save(sourcePort);

        Ship savedShip = shipRepository.save(existingShip);
        portRepository.save(destinationPort);

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, sourcePort, InteractionType.VIEWED);
        portInteractionService.logInteraction(user, destinationPort, InteractionType.VIEWED);

        return buildShip.buildShipDTO(savedShip);

    }

    @Override
    public ShipAnalysisDTO getAnalysisOfShipById(Long shipId) {

        Ship existingShip = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        int maxContainersCapacity = existingShip.getMaxContainersCapacity();
        int initialContainerStrength = existingShip.getContainers().size();

        double shipFilledPercent = ((double) initialContainerStrength/maxContainersCapacity) * 100;

        return ShipAnalysisDTO.builder()
                .shipId(existingShip.getShipId())
                .shipName(existingShip.getShipName())
                .maxContainersCapacity(existingShip.getMaxContainersCapacity())
                .initialContainerStrength(initialContainerStrength)
                .shipFilledPercent(shipFilledPercent)
                .build();

    }

}
