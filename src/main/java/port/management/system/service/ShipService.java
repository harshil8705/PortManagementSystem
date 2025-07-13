package port.management.system.service;

import jakarta.validation.Valid;
import port.management.system.model.Ship;
import port.management.system.dto.*;

public interface ShipService {

    ShipDTO addNewShipByPortId(Long portId, @Valid Ship ship);

    ShipDTO updateShipById(Long shipId, ShipDTO2 shipDTO2);

    ShipResponse2 getAllShipsByPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ShipResponse getAllShips(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ShipResponse getAllShipsByName(String shipName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ShipDTO getShipById(Long shipId);

    String removeShipById(Long shipId);

    ShipDTO changePortOfShip(Long shipId, Long portId);

    ShipAnalysisDTO getAnalysisOfShipById(Long shipId);

}
