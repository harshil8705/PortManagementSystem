package port.management.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Container;
import port.management.system.model.Port;
import port.management.system.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findByCurrentProductPort(Port port, Pageable pageDetails);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageDetails);

    Page<Product> findByContainer(Container existingContainer, Pageable pageDetails);

}
