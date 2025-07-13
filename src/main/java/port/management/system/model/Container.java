package port.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "containers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long containerId;

    @NotBlank
    private String containerName;

    @Column(nullable = false)
    private double maxWeightCapacity;

    @ManyToOne
    @JoinColumn(name = "port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port port;

    @ManyToOne
    @JoinColumn(name = "ship_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ship ship;

    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products;

}