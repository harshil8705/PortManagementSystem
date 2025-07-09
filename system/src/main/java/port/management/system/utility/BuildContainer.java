package port.management.system.utility;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import port.management.system.dto.*;
import port.management.system.model.Container;

import java.util.List;

@Component
public class BuildContainer {

    public ContainerDTO buildContainerDTO(Container container) {

        return ContainerDTO.builder()
                .containerId(container.getContainerId())
                .containerName(container.getContainerName())
                .maxWeightCapacity(container.getMaxWeightCapacity())
                .shipDTO2(container.getShip() != null
                        ? ShipDTO2.builder()
                        .shipId(container.getShip().getShipId())
                        .shipName(container.getShip().getShipName())
                        .maxContainersCapacity(container.getShip().getMaxContainersCapacity())
                        .build()
                        : null
                )
                .productDTO2List(container.getProducts() != null
                        ? container.getProducts().stream()
                        .map(p -> ProductDTO2.builder()
                                .productId(p.getProductId())
                                .productName(p.getProductName())
                                .productDescription(p.getProductDescription())
                                .productWeightInKg(p.getProductWeightInKg())
                                .build())
                        .toList()
                        : null
                )
                .portDTO(PortDTO.builder()
                        .portId(container.getPort().getPortId())
                        .portName(container.getPort().getPortName())
                        .maxShipsCapacity(container.getPort().getMaxShipsCapacity())
                        .maxContainersCapacity(container.getPort().getMaxContainersCapacity())
                        .maxProductsCapacity(container.getPort().getMaxProductsCapacity())
                        .image(container.getPort().getImage())
                        .countryDTO(CountryDTO.builder()
                                .countryId(container.getPort().getCountry().getCountryId())
                                .countryName(container.getPort().getCountry().getCountryName())
                                .build())
                        .build())
                .build();

    }

    public ContainerResponse buildContainerResponse(List<Container> containers, Page<Container> containerPage) {

        List<ContainerDTO> containerDTOList = containers.stream()
                .map(c -> ContainerDTO.builder()
                        .containerId(c.getContainerId())
                        .containerName(c.getContainerName())
                        .maxWeightCapacity(c.getMaxWeightCapacity())
                        .shipDTO2(c.getShip() != null
                                ? ShipDTO2.builder()
                                .shipId(c.getShip().getShipId())
                                .shipName(c.getShip().getShipName())
                                .maxContainersCapacity(c.getShip().getMaxContainersCapacity())
                                .build()
                                : null
                        )
                        .productDTO2List(c.getProducts() != null
                                ? c.getProducts().stream()
                                .map(p -> ProductDTO2.builder()
                                        .productId(p.getProductId())
                                        .productName(p.getProductName())
                                        .productDescription(p.getProductDescription())
                                        .productWeightInKg(p.getProductWeightInKg())
                                        .build())
                                .toList()
                                : null
                        )
                        .portDTO(PortDTO.builder()
                                .portId(c.getPort().getPortId())
                                .portName(c.getPort().getPortName())
                                .maxShipsCapacity(c.getPort().getMaxShipsCapacity())
                                .maxContainersCapacity(c.getPort().getMaxContainersCapacity())
                                .maxProductsCapacity(c.getPort().getMaxProductsCapacity())
                                .image(c.getPort().getImage())
                                .countryDTO(CountryDTO.builder()
                                        .countryId(c.getPort().getCountry().getCountryId())
                                        .countryName(c.getPort().getCountry().getCountryName())
                                        .build())
                                .build())
                        .build())
                .toList();

        ContainerResponse containerResponse = new ContainerResponse();

        containerResponse.setContainerDTOS(containerDTOList);
        containerResponse.setPageNumber(containerPage.getNumber());
        containerResponse.setPageSize(containerPage.getSize());
        containerResponse.setTotalPages(containerPage.getTotalPages());
        containerResponse.setTotalElements(containerPage.getTotalElements());
        containerResponse.setLastPage(containerPage.isLast());

        return containerResponse;

    }

}
