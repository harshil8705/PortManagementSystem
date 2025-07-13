package port.management.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.configuration.AppConstants;
import port.management.system.model.Ship;
import port.management.system.dto.*;
import port.management.system.service.ShipServiceImpl;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private ShipServiceImpl shipService;

    @PostMapping("/admin/ships/port-id/{portId}")
    public ResponseEntity<ShipDTO> addNewShipByPortId(@PathVariable Long portId,
                                                      @Valid @RequestBody Ship ship) {

        return new ResponseEntity<>(shipService.addNewShipByPortId(portId, ship), HttpStatus.OK);

    }

    @PutMapping("/admin/ships/ship-id/{shipId}")
    public ResponseEntity<ShipDTO> updateShipById(@PathVariable Long shipId,
                                                  @RequestBody ShipDTO2 shipDTO2) {

        return new ResponseEntity<>(shipService.updateShipById(shipId, shipDTO2), HttpStatus.OK);

    }

    @GetMapping("/public/ships/port-id/{portId}")
    public ResponseEntity<ShipResponse2> getAllShipsByPortId(
            @PathVariable Long portId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_SHIP_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(shipService.getAllShipsByPortId(portId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ships")
    public ResponseEntity<ShipResponse> getAllShips(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_SHIP_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(shipService.getAllShips(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ships/ship-name/{shipName}")
    public ResponseEntity<ShipResponse> getAllShipsByName(
            @PathVariable String shipName,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_SHIP_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(shipService.getAllShipsByName(shipName, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/ships/ship-id/{shipId}")
    public ResponseEntity<ShipDTO> getShipById(@PathVariable Long shipId) {

        return new ResponseEntity<>(shipService.getShipById(shipId), HttpStatus.FOUND);

    }

    @DeleteMapping("/admin/ships/ship-id/{shipId}")
    public ResponseEntity<String> removeShipById(@PathVariable Long shipId) {

        return new ResponseEntity<>(shipService.removeShipById(shipId), HttpStatus.OK);

    }

    @PutMapping("/admin/ships/ship-id/{shipId}/destination-port-id/{portId}")
    public ResponseEntity<ShipDTO> changePortOfShip(@PathVariable Long shipId,
                                                    @PathVariable Long portId) {

        return new ResponseEntity<>(shipService.changePortOfShip(shipId, portId), HttpStatus.OK);

    }

    @GetMapping("/admin/analysis/ships/ship-id/{shipId}")
    public ResponseEntity<ShipAnalysisDTO> getAnalysisOfShipById(@PathVariable Long shipId) {

        return new ResponseEntity<>(shipService.getAnalysisOfShipById(shipId), HttpStatus.OK);

    }

}
