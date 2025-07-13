package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
