package port.management.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Container;
import port.management.system.model.Port;
import port.management.system.model.Ship;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

    Page<Container> findByPort(Port port, Pageable pageDetails);

    Page<Container> findByContainerNameContainingIgnoreCase(String containerName, Pageable pageDetails);

    Page<Container> findByShip(Ship existingShip, Pageable pageDetails);

}
