package port.management.system.service;

import port.management.system.dto.AddressDTO;
import port.management.system.dto.AddressDTO2;
import port.management.system.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressDTO addNewAddress(AddressDTO2 addressDTO2);

    AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO2> getAddressByUser();

    AddressDTO updateAddressById(Long addressId, AddressDTO2 addressDTO2);

    String deleteAddressById(Long addressId);

}
