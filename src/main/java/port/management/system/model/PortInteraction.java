package port.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "port_interactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portInteractionId;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    private LocalDateTime interactedAt;

    @ManyToOne
    @JoinColumn(name = "user_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port port;

}
