package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
