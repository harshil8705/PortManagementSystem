package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.ProductCart;

@Repository
public interface CartRepository extends JpaRepository<ProductCart, Long> {
}
