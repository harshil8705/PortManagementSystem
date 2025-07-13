package port.management.system.utility;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import port.management.system.dto.*;
import port.management.system.model.Port;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuildPort {

    public PortDTO buildPortDTO(Port port) {

        return PortDTO.builder()
                .portId(port.getPortId())
                .portName(port.getPortName())
                .image(port.getImage())
                .maxShipsCapacity(port.getMaxShipsCapacity())
                .maxContainersCapacity(port.getMaxContainersCapacity())
                .maxProductsCapacity(port.getMaxProductsCapacity())
                .countryDTO(CountryDTO.builder()
                        .countryId(port.getCountry().getCountryId())
                        .countryName(port.getCountry().getCountryName())
                        .build())
                .build();

    }

    public PortResponse buildPortResponse(List<Port> ports, Page<Port> portPage) {

        List<PortDTO> portDTOList = new ArrayList<>();

        for (Port port : ports) {

            portDTOList.add(buildPortDTO(port));

        }

        PortResponse portResponse = new PortResponse();
        portResponse.setPortDTOList(portDTOList);
        portResponse.setPageNumber(portPage.getNumber());
        portResponse.setPageSize(portPage.getSize());
        portResponse.setTotalPages(portPage.getTotalPages());
        portResponse.setTotalElements(portPage.getTotalElements());
        portResponse.setLastPage(portPage.isLast());

        return portResponse;

    }

}
