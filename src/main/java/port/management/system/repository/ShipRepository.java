package port.management.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Port;
import port.management.system.model.Ship;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    Page<Ship> findByPort(Port port, Pageable pageDetails);

    Page<Ship> findByShipNameContainingIgnoreCase(String shipName, Pageable pageDetails);

}
