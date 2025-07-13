package port.management.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products_cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(nullable = false)
    private Integer productQuantity;

    @Column(nullable = false)
    private Double totalCartWeight;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

}
