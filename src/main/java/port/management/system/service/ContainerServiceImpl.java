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
import port.management.system.utility.BuildContainer;
import port.management.system.utility.ValidateContainer;

import java.util.List;

@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValidateContainer validateContainer;

    @Autowired
    private BuildContainer buildContainer;

    @Autowired
    private PortInteractionServiceImpl portInteractionService;

    @Autowired
    private UserUtil userUtil;

    @Override
    public ContainerDTO addNewContainerByPortId(Long portId, Container container) {

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        int currentContainerStrength = port.getContainers().size() - port.getShips().stream().mapToInt(s -> s.getContainers().size()).sum();
        int maxContainersCapacity = port.getMaxContainersCapacity();

        if (currentContainerStrength >= maxContainersCapacity) {

            throw new ApiException("Maximum limit for holding the Containers has been reached for this Port! Hence the container can't be assigned.");

        }

        container.setPort(port);

        Container savedContainer = containerRepository.save(container);

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        return buildContainer.buildContainerDTO(savedContainer);

    }

    @Override
    public ContainerDTO updateContainerById(Long containerId, ContainerDTO2 containerDTO2) {

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        existingContainer.setContainerName(containerDTO2.getContainerName());
        existingContainer.setMaxWeightCapacity(containerDTO2.getMaxWeightCapacity());

        Container savedContainer = containerRepository.save(existingContainer);

        return buildContainer.buildContainerDTO(savedContainer);

    }

    @Override
    public ContainerResponse getAllContainersByPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Container> containerPage = containerRepository.findByPort(port, pageDetails);

        List<Container> containers = containerPage.getContent();

        if (containers.isEmpty()) {

            throw new ApiException("No Container Created for the Port : " + port.getPortName() + " till now.");

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        return buildContainer.buildContainerResponse(containers, containerPage);

    }

    @Override
    public ContainerResponse getAllContainers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Container> containerPage = containerRepository.findAll(pageDetails);

        List<Container> containers = containerPage.getContent();

        if (containers.isEmpty()) {

            throw new ApiException("No Containers are created till now.");

        }

        return buildContainer.buildContainerResponse(containers, containerPage);

    }

    @Override
    public ContainerResponse getAllContainersByName(String containerName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Container> containerPage = containerRepository.findByContainerNameContainingIgnoreCase(containerName, pageDetails);

        List<Container> containers = containerPage.getContent();

        if (containers.isEmpty()) {

            throw new ApiException("No such Container exists with the Container Name : " + containerName);

        }

        return buildContainer.buildContainerResponse(containers, containerPage);

    }

    @Override
    public ContainerDTO getContainerById(Long containerId) {

        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        return buildContainer.buildContainerDTO(container);

    }

    @Override
    public String removeContainerById(Long containerId) {

        Container containerToDelete = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        containerRepository.delete(containerToDelete);

        return "Container with the containerId : " + containerId + " removed Successfully.";

    }

    @Override
    @Transactional
    public ContainerDTO addContainerToShip(Long containerId, Long shipId) {

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        Ship existingShip = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        if (existingShip.getContainers().size() >= existingShip.getMaxContainersCapacity()) {

            throw new ApiException("Maximum limit for holding the Containers has been reached for this Ship! Hence the container can't be assigned.");

        }

        if (validateContainer.isContainerValidBeforeAssignment(existingContainer, existingShip)) {

            existingContainer.setShip(existingShip);
            existingShip.getContainers().add(existingContainer);

        }

        shipRepository.save(existingShip);
        Container savedContainer = containerRepository.save(existingContainer);

        return buildContainer.buildContainerDTO(savedContainer);

    }

    @Override
    public ContainerResponse getContainersByShipId(Long shipId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Ship existingShip = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship", "shipId", shipId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Container> containerPage = containerRepository.findByShip(existingShip, pageDetails);

        List<Container> containers = containerPage.getContent();

        if (containers.isEmpty()) {

            throw new ApiException("No Containers are assigned to the Ship with shipId : " + shipId);

        }

        return buildContainer.buildContainerResponse(containers, containerPage);

    }

    @Override
    @Transactional
    public ContainerDTO unassignContainerFromShip(Long containerId) {

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        Ship ship = existingContainer.getShip();

        if (ship == null) {

            throw new ApiException("No Ship is assigned to the Container with containerId : " + containerId);

        }

        Port shipCurrentPort = ship.getPort();

//        Removing Container from the Ship.
        ship.getContainers().remove(existingContainer);
        existingContainer.setShip(null);

//        Assigning Port to the Container which is unassigned from the ship.
        existingContainer.setPort(shipCurrentPort);

//        Assigning Port to the Products which are present in the Container.
        List<Product> products = existingContainer.getProducts();

        if (!products.isEmpty()) {

            for (Product product : products) {

//            Assigned Port to the Products.
                product.setCurrentProductPort(shipCurrentPort);
//            Assigned Container to the Products.
                product.setContainer(existingContainer);

            }

        }

//        Checking whether the Port has the Capacity to store the Container which is unassigned from the ship.
        if (shipCurrentPort.getContainers().size() - shipCurrentPort.getShips().stream().mapToInt(s -> s.getContainers().size()).sum() >= shipCurrentPort.getMaxContainersCapacity()) {

            throw new ApiException("Can't unassign Container from the Ship as the Port's capacity to store the Containers have reached to it's Maximum Limits.");

        }

        portRepository.save(ship.getPort());
        shipRepository.save(ship);

        Container updatedContainer = containerRepository.save(existingContainer);

        return buildContainer.buildContainerDTO(updatedContainer);

    }

    @Override
    public ContainerAnalysisDTO getAnalysisOfContainerById(Long containerId) {

        Container existingContainer = containerRepository.findById(containerId)
                .orElseThrow(() -> new ResourceNotFoundException("Container", "containerId", containerId));

        double maxWeightCapacity = existingContainer.getMaxWeightCapacity();
        double initialWeight = existingContainer.getProducts().stream().mapToDouble(Product::getProductWeightInKg).sum();

        double containerFilledPercent = (initialWeight/maxWeightCapacity) * 100;

        return ContainerAnalysisDTO.builder()
                .containerId(existingContainer.getContainerId())
                .containerName(existingContainer.getContainerName())
                .maxWeightCapacity(existingContainer.getMaxWeightCapacity())
                .initialWeight(initialWeight)
                .containerFilledPercent(containerFilledPercent)
                .build();

    }

}
