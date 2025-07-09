package port.management.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import port.management.system.model.Country;
import port.management.system.model.Port;

import java.util.List;

@Repository
public interface PortRepository extends JpaRepository<Port, Long> {

    Page<Port> findByCountry(Country country, Pageable pageDetails);

    Page<Port> findByPortNameContainingIgnoreCase(String portName, Pageable pageDetails);

    @Query("""
    SELECT p FROM Port p
    LEFT JOIN p.portInteractions pi
    GROUP BY p
    ORDER BY COUNT(pi) DESC
    """)
    List<Port> findTop5MostInteractedPorts(Pageable pageable);

}
