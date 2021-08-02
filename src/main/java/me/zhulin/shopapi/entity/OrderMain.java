package me.zhulin.shopapi.entity;

//importing to generate getters,setters,constructors etc
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/*
 * The @Entity annotation specifies that the class is an entity and is mapped to
 * a database table. The @Table annotation specifies the name of the database
 * table to be used for mapping. The @Id annotation specifies the primary key of
 * an entity and the @GeneratedValue provides for the specification of
 * generation strategies for the values of primary keys.
 * 
 * @Data is a convenient shortcut annotation that bundles the features of @ToString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor together
 * 
 * https://zetcode.com/springboot/annotations/
 */

@Entity
@Data
@NoArgsConstructor

/*
 * when we use @DynamicUpdate on an entity, Hibernate does not use the cached
 * SQL statement for the update. Instead, it will generate a SQL statement each
 * time we update the entity. This generated SQL includes only the changed
 * columns. https://www.baeldung.com/spring-data-jpa-dynamicupdate
 */
@DynamicUpdate
public class OrderMain implements Serializable {
	
	/*
	 * The serialVersionUID is a universal version identifier for a Serializable
	 * class. Deserialization uses this number to ensure that a loaded class
	 * corresponds exactly to a serialized object. If no match is found, then an
	 * InvalidClassException is thrown
	 */
    private static final long serialVersionUID = -3819883511505235030L;

    /*
	 * https://thorben-janssen.com/jpa-generate-primary-keys/ Hibernate selects the
	 * generation strategy based on the used dialect
	 */
    
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "orderMain")
    private Set<ProductInOrder> products = new HashSet<>();

    @NotEmpty
    private String buyerEmail;

    @NotEmpty
    private String buyerName;

    @NotEmpty
    private String buyerPhone;

    @NotEmpty
    private String buyerAddress;

    // Total Amount
    @NotNull
    private BigDecimal orderAmount;

    /**
     * default 0: new order.
     */
    @NotNull
    @ColumnDefault("0")
    private Integer orderStatus;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public OrderMain(User buyer) {
        this.buyerEmail = buyer.getEmail();
        this.buyerName = buyer.getName();
        this.buyerPhone = buyer.getPhone();
        this.buyerAddress = buyer.getAddress();
        this.orderAmount = buyer.getCart().getProducts().stream().map(item -> item.getProductPrice().multiply(new BigDecimal(item.getCount())))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        this.orderStatus = 0;

    }
}
