package port.management.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String productDescription;

    @Column(nullable = false)
    private boolean isPaymentPending;

    @Column(nullable = false)
    private double productWeightInKg;

    @Column(nullable = false)
    private String productStatus;

    @ManyToOne
    @JoinColumn(name = "port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port currentProductPort;

    @ManyToOne
    @JoinColumn(name = "source_port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port sourcePort;

    @ManyToOne
    @JoinColumn(name = "destination_port_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Port destinationPort;

    @ManyToOne
    @JoinColumn(name = "container_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Container container;

    @ManyToOne
    @JoinColumn(name = "user_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne
    @JoinColumn(name = "cart_fk_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductCart cart;

}
