package port.management.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.configuration.AppConstants;
import port.management.system.model.Container;
import port.management.system.dto.*;
import port.management.system.service.ContainerServiceImpl;

@RestController
@RequestMapping("/api")
public class ContainerController {

    @Autowired
    private ContainerServiceImpl containerService;

    @PostMapping("/admin/containers/port-id/{portId}")
    public ResponseEntity<ContainerDTO> addNewContainerByPortId(@PathVariable Long portId,
                                                                @Valid @RequestBody Container container) {

        return new ResponseEntity<>(containerService.addNewContainerByPortId(portId, container), HttpStatus.OK);

    }

    @PutMapping("/admin/containers/container-id/{containerId}")
    public ResponseEntity<ContainerDTO> updateContainerById(@PathVariable Long containerId,
                                                            @RequestBody ContainerDTO2 containerDTO2) {

        return new ResponseEntity<>(containerService.updateContainerById(containerId, containerDTO2), HttpStatus.OK);

    }

    @GetMapping("/public/containers/port-id/{portId}")
    public ResponseEntity<ContainerResponse> getAllContainersByPortId(
            @PathVariable Long portId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CONTAINER_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(containerService.getAllContainersByPortId(portId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/containers")
    public ResponseEntity<ContainerResponse> getAllContainers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CONTAINER_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(containerService.getAllContainers(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/containers/container-name/{containerName}")
    public ResponseEntity<ContainerResponse> getAllContainersByName(
            @PathVariable String containerName,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CONTAINER_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(containerService.getAllContainersByName(containerName, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/containers/container-id/{containerId}")
    public ResponseEntity<ContainerDTO> getContainerById(@PathVariable Long containerId) {

        return new ResponseEntity<>(containerService.getContainerById(containerId), HttpStatus.FOUND);

    }

    @DeleteMapping("/admin/containers/container-id/{containerId}")
    public ResponseEntity<String> removeContainerById(@PathVariable Long containerId) {

        return new ResponseEntity<>(containerService.removeContainerById(containerId), HttpStatus.OK);

    }

    @PutMapping("/admin/containers/container-id/{containerId}/ship-id/{shipId}")
    public ResponseEntity<ContainerDTO> addContainerToShip(@PathVariable Long containerId,
                                                           @PathVariable Long shipId) {

        return new ResponseEntity<>(containerService.addContainerToShip(containerId, shipId), HttpStatus.OK);

    }

    @GetMapping("/admin/containers/ship-id/{shipId}")
    public ResponseEntity<ContainerResponse> getContainersByShipId(
            @PathVariable Long shipId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CONTAINER_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(containerService.getContainersByShipId(shipId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @PutMapping("/admin/containers/ship/container-id/{containerId}")
    public ResponseEntity<ContainerDTO> unassignContainerFromShip(@PathVariable Long containerId) {

        return new ResponseEntity<>(containerService.unassignContainerFromShip(containerId), HttpStatus.OK);

    }

    @GetMapping("/admin/analysis/containers/container-id/{containerId}")
    public ResponseEntity<ContainerAnalysisDTO> getAnalysisOfContainerById(@PathVariable Long containerId) {

        return new ResponseEntity<>(containerService.getAnalysisOfContainerById(containerId), HttpStatus.OK);

    }

}
