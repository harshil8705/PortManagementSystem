package port.management.system.service;

import jakarta.validation.Valid;
import port.management.system.model.Container;
import port.management.system.dto.*;

public interface ContainerService {

    ContainerDTO addNewContainerByPortId(Long portId, @Valid Container container);

    ContainerDTO updateContainerById(Long containerId, ContainerDTO2 containerDTO2);

    ContainerResponse getAllContainersByPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ContainerResponse getAllContainers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ContainerResponse getAllContainersByName(String containerName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ContainerDTO getContainerById(Long containerId);

    String removeContainerById(Long containerId);

    ContainerDTO addContainerToShip(Long containerId, Long shipId);

    ContainerResponse getContainersByShipId(Long shipId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ContainerDTO unassignContainerFromShip(Long containerId);

    ContainerAnalysisDTO getAnalysisOfContainerById(Long containerId);

}
