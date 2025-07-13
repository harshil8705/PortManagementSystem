package port.management.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.configuration.AppConstants;
import port.management.system.model.Country;
import port.management.system.dto.CountryDTO;
import port.management.system.dto.CountryResponse;
import port.management.system.service.CountryServiceImpl;

@RestController
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryServiceImpl countryService;

    @PostMapping("/admin/countries")
    public ResponseEntity<CountryDTO> addNewCountry(@Valid @RequestBody Country country) {

        return new ResponseEntity<>(countryService.addNewCountry(country), HttpStatus.OK);

    }

    @PutMapping("/admin/countries/country-id/{countryId}")
    public ResponseEntity<CountryDTO> updateCountryById(@PathVariable Long countryId,
                                                        @Valid @RequestBody Country country) {

        return new ResponseEntity<>(countryService.updateCountryById(countryId, country), HttpStatus.OK);

    }

    @GetMapping("/public/countries")
    public ResponseEntity<CountryResponse> getAllCountries(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_COUNTRY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(countryService.getAllCountries(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/countries/country-name/{countryName}")
    public ResponseEntity<CountryResponse> getListOfCountriesByName(
            @PathVariable String countryName,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_COUNTRY_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(countryService.getListOfCountriesByName(countryName, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/countries/country-id/{countryId}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long countryId) {

        return new ResponseEntity<>(countryService.getCountryById(countryId), HttpStatus.FOUND);

    }

    @DeleteMapping("/admin/countries/country-id/{countryId}")
    public ResponseEntity<String> deleteCountryById(@PathVariable Long countryId) {

        return new ResponseEntity<>(countryService.deleteCountryById(countryId), HttpStatus.OK);

    }

}
