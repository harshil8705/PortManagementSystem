package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
