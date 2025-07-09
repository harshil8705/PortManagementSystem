package port.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ships")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipId;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String shipName;

    @Column(nullable = false)
    private int maxContainersCapacity;

    @ManyToOne
    @JoinColumn(name = "port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port port;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Container> containers;

}
