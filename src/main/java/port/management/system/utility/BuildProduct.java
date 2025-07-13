package port.management.system.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import port.management.system.dto.*;
import port.management.system.model.Port;
import port.management.system.model.Product;
import port.management.system.model.User;
import port.management.system.exception.ApiException;
import port.management.system.security.utility.UserUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuildProduct {

    @Autowired
    private UserUtil userUtil;

    public ProductResponse3 buildProductResponse3(Product product) {

        UserDTO2 userDTO2 = UserDTO2.builder()
                .userId(product.getUser().getUserId())
                .username(product.getUser().getUsername())
                .email(product.getUser().getEmail())
                .mobile(product.getUser().getMobile())
                .role(product.getUser().getRole())
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productWeightInKg(product.getProductWeightInKg())
                .productStatus(product.getProductStatus())
                .containerDTO3(product.getContainer() != null
                        ? ContainerDTO3.builder()
                        .containerId(product.getContainer().getContainerId())
                        .containerName(product.getContainer().getContainerName())
                        .maxWeightCapacity(product.getContainer().getMaxWeightCapacity())
                        .shipDTO2(product.getContainer().getShip() != null
                                ? ShipDTO2.builder()
                                .shipId(product.getContainer().getShip().getShipId())
                                .shipName(product.getContainer().getShip().getShipName())
                                .maxContainersCapacity(product.getContainer().getShip().getMaxContainersCapacity())
                                .build()
                                : null
                        )
                        .build()
                        : null
                )
                .build();

        PortDTO currentPortDTO = PortDTO.builder()
                .portId(product.getCurrentProductPort().getPortId())
                .maxShipsCapacity(product.getCurrentProductPort().getMaxShipsCapacity())
                .portName(product.getCurrentProductPort().getPortName())
                .image(product.getCurrentProductPort().getImage())
                .maxProductsCapacity(product.getCurrentProductPort().getMaxProductsCapacity())
                .maxContainersCapacity(product.getCurrentProductPort().getMaxContainersCapacity())
                .countryDTO(CountryDTO.builder()
                        .countryId(product.getCurrentProductPort().getCountry().getCountryId())
                        .countryName(product.getCurrentProductPort().getCountry().getCountryName())
                        .build())
                .build();

        PortDTO sourcePortDTO = PortDTO.builder()
                .portId(product.getSourcePort().getPortId())
                .maxShipsCapacity(product.getSourcePort().getMaxShipsCapacity())
                .portName(product.getSourcePort().getPortName())
                .maxProductsCapacity(product.getSourcePort().getMaxProductsCapacity())
                .maxContainersCapacity(product.getSourcePort().getMaxContainersCapacity())
                .image(product.getSourcePort().getImage())
                .countryDTO(CountryDTO.builder()
                        .countryId(product.getSourcePort().getCountry().getCountryId())
                        .countryName(product.getSourcePort().getCountry().getCountryName())
                        .build())
                .build();

        PortDTO destinationPortDTO = PortDTO.builder()
                .portId(product.getDestinationPort().getPortId())
                .maxShipsCapacity(product.getDestinationPort().getMaxShipsCapacity())
                .portName(product.getDestinationPort().getPortName())
                .maxProductsCapacity(product.getDestinationPort().getMaxProductsCapacity())
                .maxContainersCapacity(product.getDestinationPort().getMaxContainersCapacity())
                .image(product.getDestinationPort().getImage())
                .countryDTO(CountryDTO.builder()
                        .countryId(product.getDestinationPort().getCountry().getCountryId())
                        .countryName(product.getDestinationPort().getCountry().getCountryName())
                        .build())
                .build();

        return ProductResponse3.builder()
                .destinationPort(destinationPortDTO)
                .currentProductPort(currentPortDTO)
                .sourcePort(sourcePortDTO)
                .productDTO(productDTO)
                .userDTO2(userDTO2)
                .build();

    }

    public ProductResponse2 buildProductResponse2(List<Product> products, Port sourcePort, Port destinationPort) {

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        List<ProductDTO> productDTOList = products.stream()
                .map(p -> ProductDTO.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .productDescription(p.getProductDescription())
                        .productWeightInKg(p.getProductWeightInKg())
                        .productStatus(p.getProductStatus())
                        .containerDTO3(p.getContainer() != null
                                ? ContainerDTO3.builder()
                                .containerId(p.getContainer().getContainerId())
                                .containerName(p.getContainer().getContainerName())
                                .maxWeightCapacity(p.getContainer().getMaxWeightCapacity())
                                .shipDTO2(p.getContainer().getShip() != null
                                        ? ShipDTO2.builder()
                                        .shipId(p.getContainer().getShip().getShipId())
                                        .shipName(p.getContainer().getShip().getShipName())
                                        .maxContainersCapacity(p.getContainer().getShip().getMaxContainersCapacity())
                                        .build()
                                        : null
                                )
                                .build()
                                : null
                        )
                        .build()
                )
                .toList();

        UserDTO2 userDTO2 = UserDTO2.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .build();

        PortDTO destinationPortDTO = PortDTO.builder()
                .portId(destinationPort.getPortId())
                .portName(destinationPort.getPortName())
                .maxShipsCapacity(destinationPort.getMaxShipsCapacity())
                .maxContainersCapacity(destinationPort.getMaxContainersCapacity())
                .maxProductsCapacity(destinationPort.getMaxProductsCapacity())
                .image(destinationPort.getImage())
                .countryDTO(CountryDTO.builder()
                        .countryId(destinationPort.getCountry().getCountryId())
                        .countryName(destinationPort.getCountry().getCountryName())
                        .build())
                .build();

        PortDTO sourcePortDTO = PortDTO.builder()
                .portId(sourcePort.getPortId())
                .portName(sourcePort.getPortName())
                .maxShipsCapacity(sourcePort.getMaxShipsCapacity())
                .maxContainersCapacity(sourcePort.getMaxContainersCapacity())
                .maxProductsCapacity(sourcePort.getMaxProductsCapacity())
                .image(sourcePort.getImage())
                .countryDTO(CountryDTO.builder()
                        .countryId(sourcePort.getCountry().getCountryId())
                        .countryName(sourcePort.getCountry().getCountryName())
                        .build())
                .build();

        return ProductResponse2.builder()
                .userDTO2(userDTO2)
                .sourcePort(sourcePortDTO)
                .currentProductPort(sourcePortDTO)
                .destinationPort(destinationPortDTO)
                .productDTOList(productDTOList)
                .build();

    }

    public ProductResponse buildProductResponse(List<Product> products, Page<Product> productPage) {

        List<ProductResponse3> productResponse3List = new ArrayList<>();

        for (Product product : products) {

            productResponse3List.add(buildProductResponse3(product));

        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductResponse3List(productResponse3List);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;

    }

}
