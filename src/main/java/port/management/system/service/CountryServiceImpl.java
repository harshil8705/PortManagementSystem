package port.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import port.management.system.model.Country;
import port.management.system.exception.ApiException;
import port.management.system.exception.ResourceNotFoundException;
import port.management.system.dto.CountryDTO;
import port.management.system.dto.CountryResponse;
import port.management.system.repository.CountryRepository;
import port.management.system.repository.PortRepository;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService{

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PortRepository portRepository;

    @Override
    public CountryDTO addNewCountry(Country country) {

        boolean countryAlreadyExists = countryRepository.existsByCountryNameIgnoreCase(country.getCountryName());

        if (countryAlreadyExists) {

            throw new ApiException("Country with the name : " + country.getCountryName() + " already Exists.");

        }

        Country savedCountry = countryRepository.save(country);

        return CountryDTO.builder()
                .countryId(savedCountry.getCountryId())
                .countryName(savedCountry.getCountryName())
                .build();

    }

    @Override
    public CountryDTO updateCountryById(Long countryId, Country country) {

        Country existingCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "countryId", countryId));

        existingCountry.setCountryName(country.getCountryName());

        Country savedCountry = countryRepository.save(existingCountry);

        return CountryDTO.builder()
                .countryId(savedCountry.getCountryId())
                .countryName(savedCountry.getCountryName())
                .build();

    }

    @Override
    public CountryResponse getAllCountries(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Country> countryPage = countryRepository.findAll(pageDetails);

        List<Country> countries = countryPage.getContent();

        if (countries.isEmpty()) {

            throw new ApiException("No Countries Created till now.");

        }

        List<CountryDTO> countryDTOS = countries.stream()
                .map(c -> CountryDTO.builder()
                        .countryId(c.getCountryId())
                        .countryName(c.getCountryName())
                        .build()
                )
                .toList();

        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryDTOS(countryDTOS);
        countryResponse.setPageNumber(countryPage.getNumber());
        countryResponse.setPageSize(countryPage.getSize());
        countryResponse.setTotalPages(countryPage.getTotalPages());
        countryResponse.setTotalElements(countryPage.getTotalElements());
        countryResponse.setLastPage(countryPage.isLast());

        return countryResponse;

    }

    @Override
    public CountryResponse getListOfCountriesByName(
            String countryName,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Country> countryPage = countryRepository.findByCountryNameContainingIgnoreCase(countryName, pageDetails);

        List<Country> countries = countryPage.getContent();

        if (countries.isEmpty()) {

            throw new ResourceNotFoundException("Country", "CountryName", countryName);

        }

        List<CountryDTO> countryDTOS = countries.stream()
                .map(c -> CountryDTO.builder()
                        .countryId(c.getCountryId())
                        .countryName(c.getCountryName())
                        .build()
                )
                .toList();

        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryDTOS(countryDTOS);
        countryResponse.setPageNumber(countryPage.getNumber());
        countryResponse.setPageSize(countryPage.getSize());
        countryResponse.setTotalPages(countryPage.getTotalPages());
        countryResponse.setTotalElements(countryPage.getTotalElements());
        countryResponse.setLastPage(countryPage.isLast());

        return countryResponse;

    }

    @Override
    public CountryDTO getCountryById(Long countryId) {

        Country existingCountry = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "countryId", countryId));

        return CountryDTO.builder()
                .countryId(existingCountry.getCountryId())
                .countryName(existingCountry.getCountryName())
                .build();

    }

    @Override
    public String deleteCountryById(Long countryId) {

        Country countryToDelete = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "countryId", countryId));

        countryRepository.delete(countryToDelete);

        return "Country with countryId : " + countryId + " removed successfully.";

    }

}
