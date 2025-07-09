package port.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.configuration.AppConstants;
import port.management.system.dto.AddressDTO;
import port.management.system.dto.AddressDTO2;
import port.management.system.dto.AddressResponse;
import port.management.system.service.AddressServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressServiceImpl addressService;

    @PostMapping("/public/addresses")
    public ResponseEntity<AddressDTO> addNewAddress(@RequestBody AddressDTO2 addressDTO2) {

        return new ResponseEntity<>(addressService.addNewAddress(addressDTO2), HttpStatus.OK);

    }

    @GetMapping("/admin/addresses")
    public ResponseEntity<AddressResponse> getAllAddresses(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ADDRESS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(addressService.getAllAddresses(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/admin/addresses/address-id/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {

        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.FOUND);

    }

    @GetMapping("/public/addresses/user")
    public ResponseEntity<List<AddressDTO2>> getAddressByUser() {

        return new ResponseEntity<>(addressService.getAddressByUser(), HttpStatus.FOUND);

    }

    @PutMapping("/public/addresses/address-id/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
                                                        @RequestBody AddressDTO2 addressDTO2) {

        return new ResponseEntity<>(addressService.updateAddressById(addressId, addressDTO2), HttpStatus.OK);

    }

    @DeleteMapping("/public/addresses/address-id/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {

        return new ResponseEntity<>(addressService.deleteAddressById(addressId), HttpStatus.OK);

    }

}
