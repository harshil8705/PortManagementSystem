package port.management.system.service;

import jakarta.validation.Valid;
import port.management.system.model.Country;
import port.management.system.dto.CountryDTO;
import port.management.system.dto.CountryResponse;

public interface CountryService {

    CountryDTO addNewCountry(@Valid Country country);

    CountryDTO updateCountryById(Long countryId, @Valid Country country);

    CountryResponse getAllCountries(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CountryResponse getListOfCountriesByName(String countryName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CountryDTO getCountryById(Long countryId);

    String deleteCountryById(Long countryId);

}
