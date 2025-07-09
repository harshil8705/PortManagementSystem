package port.management.system.utility;

import org.springframework.stereotype.Component;
import port.management.system.dto.AddressDTO;
import port.management.system.dto.UserDTO2;
import port.management.system.model.Address;

@Component
public class BuildAddress {

    public AddressDTO buildAddressDTO(Address address) {

        return AddressDTO.builder()
                .addressId(address.getAddressId())
                .pincode(address.getPincode())
                .state(address.getState())
                .street(address.getStreet())
                .country(address.getCountry())
                .city(address.getCity())
                .buildingName(address.getBuildingName())
                .userDTO2(UserDTO2.builder()
                        .userId(address.getUser().getUserId())
                        .role(address.getUser().getRole())
                        .mobile(address.getUser().getMobile())
                        .email(address.getUser().getEmail())
                        .username(address.getUser().getUsername())
                        .build())
                .build();

    }

}
