package port.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import port.management.system.dto.AddressDTO;
import port.management.system.dto.AddressDTO2;
import port.management.system.dto.AddressResponse;
import port.management.system.dto.UserDTO2;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.model.Address;
import port.management.system.model.User;
import port.management.system.repository.AddressRepository;
import port.management.system.repository.UserRepository;
import port.management.system.security.utility.UserUtil;
import port.management.system.utility.BuildAddress;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildAddress buildAddress;

    @Override
    public AddressDTO addNewAddress(AddressDTO2 addressDTO2) {

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to Fetch the User.");

        }

        Address addressToSave = Address.builder()
                .addressId(addressDTO2.getAddressId())
                .state(addressDTO2.getState())
                .city(addressDTO2.getCity())
                .street(addressDTO2.getStreet())
                .pincode(addressDTO2.getPincode())
                .country(addressDTO2.getCountry())
                .buildingName(addressDTO2.getBuildingName())
                .user(user)
                .build();

        user.getUserAddress().add(addressToSave);

        Address savedAddress = addressRepository.save(addressToSave);

        return buildAddress.buildAddressDTO(savedAddress);

    }

    @Override
    public AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Address> addressPage = addressRepository.findAll(pageable);

        List<Address> addresses = addressPage.getContent();

        if (addresses.isEmpty()) {

            throw new ApiException("No Addresses is created till Now.");

        }

        List<AddressDTO> addressDTOList = addresses.stream()
                .map(a -> AddressDTO.builder()
                        .addressId(a.getAddressId())
                        .pincode(a.getPincode())
                        .state(a.getState())
                        .street(a.getStreet())
                        .country(a.getCountry())
                        .city(a.getCity())
                        .buildingName(a.getBuildingName())
                        .userDTO2(UserDTO2.builder()
                                .userId(a.getUser().getUserId())
                                .role(a.getUser().getRole())
                                .mobile(a.getUser().getMobile())
                                .email(a.getUser().getEmail())
                                .username(a.getUser().getUsername())
                                .build())
                        .build())
                .toList();

        AddressResponse addressResponse = new AddressResponse();

        addressResponse.setAddressDTOList(addressDTOList);
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setLastPage(addressPage.isLast());

        return addressResponse;

    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return buildAddress.buildAddressDTO(address);

    }

    @Override
    public List<AddressDTO2> getAddressByUser() {

        User user = userUtil.getCurrentUser().getBody();

        if (user == null) {

            throw new ApiException("Unable to fetch the User.");

        }

        List<Address> addresses = user.getUserAddress();

        if (addresses.isEmpty()) {

            throw new ApiException("No User Addresses found.");

        }

        return addresses.stream()
                .map(a -> AddressDTO2.builder()
                        .addressId(a.getAddressId())
                        .pincode(a.getPincode())
                        .state(a.getState())
                        .street(a.getStreet())
                        .country(a.getCountry())
                        .city(a.getCity())
                        .buildingName(a.getBuildingName())
                        .build())
                .toList();

    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO2 addressDTO2) {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        existingAddress.setCity(addressDTO2.getCity());
        existingAddress.setState(addressDTO2.getState());
        existingAddress.setPincode(addressDTO2.getPincode());
        existingAddress.setCountry(addressDTO2.getCountry());
        existingAddress.setStreet(addressDTO2.getStreet());
        existingAddress.setBuildingName(addressDTO2.getBuildingName());

        Address savedAddress = addressRepository.save(existingAddress);

        return buildAddress.buildAddressDTO(savedAddress);

    }

    @Override
    public String deleteAddressById(Long addressId) {

        Address addressToDelete = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(addressToDelete);

        return "Address with addressId : " + addressId + " deleted Successfully.";

    }

}
