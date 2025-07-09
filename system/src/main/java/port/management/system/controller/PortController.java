package port.management.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import port.management.system.configuration.AppConstants;
import port.management.system.model.Port;
import port.management.system.dto.*;
import port.management.system.service.PortServiceImpl;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PortController {

    @Autowired
    private PortServiceImpl portService;

    @PostMapping("/admin/ports/country-id/{countryId}")
    public ResponseEntity<PortDTO> addNewPortByCountryId(@PathVariable Long countryId,
                                                         @Valid @RequestBody Port port) {

        return new ResponseEntity<>(portService.addNewPortByCountryId(countryId, port), HttpStatus.OK);

    }

    @PutMapping("/admin/ports/port-id/{portId}")
    public ResponseEntity<PortDTO> updatePortById(@PathVariable Long portId,
                                                  @Valid @RequestBody PortDTO2 updatedPortDTO2) {

        return new ResponseEntity<>(portService.updatePortById(portId, updatedPortDTO2), HttpStatus.OK);

    }

    @GetMapping("/public/ports/country-id/{countryId}")
    public ResponseEntity<PortResponse> getAllPortsByCountryId(
            @PathVariable Long countryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(portService.getAllPortsByCountryId(countryId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ports")
    public ResponseEntity<PortResponse> getAllPorts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(portService.getAllPorts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ports/port-name/{portName}")
    public ResponseEntity<PortResponse> getAllPortsByName(
            @PathVariable String portName,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(portService.getAllPortsByName(portName, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ports/port-id/{portId}")
    public ResponseEntity<PortDTO> getPortById(@PathVariable Long portId) {

        return new ResponseEntity<>(portService.getPortById(portId), HttpStatus.FOUND);

    }

    @DeleteMapping("/admin/ports/port-id/{portId}")
    public ResponseEntity<String> deletePortById(@PathVariable Long portId) {

        return new ResponseEntity<>(portService.deletePortById(portId), HttpStatus.OK);

    }

    @GetMapping("/admin/analysis/ports/port-id/{portId}")
    public ResponseEntity<PortAnalysisDTO> getPortAnalysisById(@PathVariable Long portId) {

        return new ResponseEntity<>(portService.getPortAnalysisById(portId), HttpStatus.OK);

    }

    @PutMapping("/admin/ports/images/port-id/{portId}")
    public ResponseEntity<PortDTO> uploadPortImage(@PathVariable Long portId,
                                                   @RequestParam("image")MultipartFile image) throws IOException {

        return new ResponseEntity<>(portService.uploadPortImage(portId, image), HttpStatus.OK);

    }

}
