package port.management.system.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import port.management.system.model.Country;
import port.management.system.model.InteractionType;
import port.management.system.model.Port;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.dto.*;
import port.management.system.model.User;
import port.management.system.repository.CountryRepository;
import port.management.system.repository.PortRepository;
import port.management.system.security.utility.UserUtil;
import port.management.system.utility.BuildPort;

import java.io.IOException;
import java.util.List;

@Service
public class PortServiceImpl implements PortService {

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BuildPort buildPort;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private PortInteractionServiceImpl portInteractionService;

    @Autowired
    private UserUtil userUtil;

    @Value("${project.image}")
    private String path;

    @Override
    public PortDTO addNewPortByCountryId(Long countryId, Port port) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "countryId", countryId));

        port.setCountry(country);

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        Port savedPort = portRepository.save(port);

        portInteractionService.logInteraction(user, port, InteractionType.PORT_ADDED);

        return buildPort.buildPortDTO(savedPort);

    }

    @Override
    public PortDTO updatePortById(Long portId, @Valid PortDTO2 updatedPortDTO2) {

        Port existingPort = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        existingPort.setPortName(updatedPortDTO2.getPortName());
        existingPort.setMaxContainersCapacity(updatedPortDTO2.getMaxContainersCapacity());
        existingPort.setMaxShipsCapacity(updatedPortDTO2.getMaxShipsCapacity());
        existingPort.setMaxProductsCapacity(updatedPortDTO2.getMaxProductsCapacity());

        Country country = existingPort.getCountry();

        if (country == null) {

            throw new ApiException("No Country is assigned for the Port with portId : " + portId);

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        Port savedPort = portRepository.save(existingPort);

        portInteractionService.logInteraction(user, existingPort, InteractionType.PORT_UPDATED);

        return buildPort.buildPortDTO(savedPort);

    }

    @Override
    public PortResponse getAllPortsByCountryId(Long countryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "countryId", countryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Port> portPage = portRepository.findByCountry(country, pageDetails);

        List<Port> ports = portPage.getContent();

        if (ports.isEmpty()) {

            throw new ApiException("No Ports Created for the Country : " + country.getCountryName() + " till now.");

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        for (Port port : ports) {

            portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        }

        return buildPort.buildPortResponse(ports, portPage);

    }

    @Override
    public PortResponse getAllPorts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Port> portPage = portRepository.findAll(pageDetails);

        List<Port> ports = portPage.getContent();

        if (ports.isEmpty()) {

            throw new ApiException("No Ports are created till now.");

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        for (Port port : ports) {

            portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        }

        return buildPort.buildPortResponse(ports, portPage);

    }

    @Override
    public PortResponse getAllPortsByName(String portName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Port> portPage = portRepository.findByPortNameContainingIgnoreCase(portName, pageDetails);

        List<Port> ports = portPage.getContent();

        if (ports.isEmpty()) {

            throw new ApiException("No Ports exists by the name : " + portName);

        }

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        for (Port port : ports) {

            portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        }

        return buildPort.buildPortResponse(ports, portPage);

    }

    @Override
    public PortDTO getPortById(Long portId) {

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, port, InteractionType.VIEWED);

        return buildPort.buildPortDTO(port);

    }

    @Override
    public String deletePortById(Long portId) {

        Port portToDelete = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        portRepository.delete(portToDelete);

        return "Port with the portId : " + portId + " removed Successfully.";

    }

    @Override
    public PortAnalysisDTO getPortAnalysisById(Long portId) {

        Port exisitingPort = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        portInteractionService.logInteraction(user, exisitingPort, InteractionType.VIEWED);

        int maxShipsCapacity = exisitingPort.getMaxShipsCapacity();
        int maxContainersCapacity = exisitingPort.getMaxContainersCapacity();
        int maxProductsCapacity = exisitingPort.getMaxProductsCapacity();
        int maxPortCapacity = maxShipsCapacity + maxContainersCapacity + maxProductsCapacity;

        int initialShipStrength = exisitingPort.getShips().size();
        int initialContainerStrength = exisitingPort.getContainers().size() - exisitingPort.getShips().stream().mapToInt(s -> s.getContainers().size()).sum();
        int initialProductsStrength = exisitingPort.getProducts().size() - exisitingPort.getContainers().stream().mapToInt(c -> c.getProducts().size()).sum();
        int initialPortStrength = initialShipStrength + initialContainerStrength + initialProductsStrength;

        double shipStrengthPercent = ((double) initialShipStrength / maxShipsCapacity) * 100;
        double containerStrengthPercent = ((double) initialContainerStrength / maxContainersCapacity) * 100;
        double productStrengthPercent = ((double) initialProductsStrength / maxProductsCapacity) * 100;

        double portFilledPercent = ((double) initialPortStrength / maxPortCapacity) * 100;

        return PortAnalysisDTO.builder()
                .portId(exisitingPort.getPortId())
                .portName(exisitingPort.getPortName())
                .maxShipsCapacity(maxShipsCapacity)
                .maxContainersCapacity(maxContainersCapacity)
                .maxProductsCapacity(maxProductsCapacity)
                .initialShipStrength(initialShipStrength)
                .initialContainerStrength(initialContainerStrength)
                .initialProductsStrength(initialProductsStrength)
                .shipStrengthPercent(shipStrengthPercent)
                .containerStrengthPercent(containerStrengthPercent)
                .productStrengthPercent(productStrengthPercent)
                .portFilledPercent(portFilledPercent)
                .build();

    }

    @Override
    public PortDTO uploadPortImage(Long portId, MultipartFile image) throws IOException {

        Port existingPort = portRepository.findById(portId)
                .orElseThrow(() -> new ResourceNotFoundException("Port", "portId", portId));

        String fileName = fileService.uploadImage(path, image);
        existingPort.setImage(fileName);

        Port updatedPort = portRepository.save(existingPort);

        return buildPort.buildPortDTO(updatedPort);

    }

}
