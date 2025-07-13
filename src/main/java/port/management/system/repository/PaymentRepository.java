package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
