package port.management.system.utility;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import port.management.system.dto.CountryDTO;
import port.management.system.dto.PortDTO;
import port.management.system.dto.ShipDTO;
import port.management.system.dto.ShipResponse;
import port.management.system.model.Ship;

import java.util.List;

@Component
public class BuildShip {

    public ShipDTO buildShipDTO(Ship ship) {

        return ShipDTO.builder()
                .shipId(ship.getShipId())
                .shipName(ship.getShipName())
                .maxContainersCapacity(ship.getMaxContainersCapacity())
                .portDTO(PortDTO.builder()
                        .portId(ship.getPort().getPortId())
                        .portName(ship.getPort().getPortName())
                        .image(ship.getPort().getImage())
                        .maxShipsCapacity(ship.getPort().getMaxShipsCapacity())
                        .maxContainersCapacity(ship.getPort().getMaxContainersCapacity())
                        .maxProductsCapacity(ship.getPort().getMaxProductsCapacity())
                        .countryDTO(CountryDTO.builder()
                                .countryId(ship.getPort().getCountry().getCountryId())
                                .countryName(ship.getPort().getCountry().getCountryName())
                                .build())
                        .build())
                .build();

    }

    public ShipResponse buildShipResponse(List<Ship> ships, Page<Ship> shipPage) {

        List<ShipDTO> shipDTOList = ships.stream()
                .map(s -> ShipDTO.builder()
                        .shipId(s.getShipId())
                        .shipName(s.getShipName())
                        .maxContainersCapacity(s.getMaxContainersCapacity())
                        .portDTO(PortDTO.builder()
                                .portId(s.getPort().getPortId())
                                .portName(s.getPort().getPortName())
                                .image(s.getPort().getImage())
                                .maxShipsCapacity(s.getPort().getMaxShipsCapacity())
                                .maxContainersCapacity(s.getPort().getMaxContainersCapacity())
                                .maxProductsCapacity(s.getPort().getMaxProductsCapacity())
                                .countryDTO(CountryDTO.builder()
                                        .countryId(s.getPort().getCountry().getCountryId())
                                        .countryName(s.getPort().getCountry().getCountryName())
                                        .build())
                                .build())
                        .build())
                .toList();

        ShipResponse shipResponse = new ShipResponse();

        shipResponse.setShipDTOS(shipDTOList);
        shipResponse.setPageNumber(shipPage.getNumber());
        shipResponse.setPageSize(shipPage.getSize());
        shipResponse.setTotalPages(shipPage.getTotalPages());
        shipResponse.setTotalElements(shipPage.getTotalElements());
        shipResponse.setLastPage(shipPage.isLast());

        return shipResponse;

    }

}
