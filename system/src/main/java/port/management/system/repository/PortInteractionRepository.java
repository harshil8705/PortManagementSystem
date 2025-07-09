package port.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.management.system.model.Port;
import port.management.system.model.PortInteraction;
import port.management.system.model.User;

import java.util.List;

@Repository
public interface PortInteractionRepository extends JpaRepository<PortInteraction, Long> {

    List<PortInteraction> findByUser(User user);
    List<PortInteraction> findByPort(Port port);

}
