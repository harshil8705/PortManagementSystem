package port.management.system.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Page<Country> findByCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

    boolean existsByCountryNameIgnoreCase(@NotBlank String countryName);

}
