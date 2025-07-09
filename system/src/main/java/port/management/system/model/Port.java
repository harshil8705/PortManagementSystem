package port.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portId;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String portName;

    private String image;

    @Column(nullable = false)
    private int maxShipsCapacity;

    @Column(nullable = false)
    private int maxContainersCapacity;

    @Column(nullable = false)
    private int maxProductsCapacity;

    @ManyToOne
    @JoinColumn(name = "country_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Country country;

    @OneToMany(mappedBy = "port", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ship> ships;

    @OneToMany(mappedBy = "port", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Container> containers;

    @OneToMany(mappedBy = "currentProductPort", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products;

    @OneToMany(mappedBy = "sourcePort", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> productsFromSource;

    @OneToMany(mappedBy = "destinationPort", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> productsToDestination;

    @OneToMany(mappedBy = "port", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PortInteraction> portInteractions;

}
