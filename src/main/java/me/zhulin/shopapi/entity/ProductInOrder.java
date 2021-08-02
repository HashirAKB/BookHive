package me.zhulin.shopapi.entity;

//importing JsonIgnore to mark a property or list of properties to be ignored.
import com.fasterxml.jackson.annotation.JsonIgnore;

//importing to generate getters,setters,constructors etc
import lombok.Data;
import lombok.NoArgsConstructor;

//For performing cascading
import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;


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
public class ProductInOrder {
	
	/*
	 * https://thorben-janssen.com/jpa-generate-primary-keys/ Hibernate selects the
	 * generation strategy based on the used dialect
	 */
	
	/*
	 * @Id annotation is used as meta data for specifying the primary key of an
	 * entity. But sometimes, entity is usually used in DAO layer code with id which
	 * not not primary key but its logical or natural id. In such cases, @NaturalId
	 * annotation will prove good replacement of named queries in hibernate.
	 */
	/*
	 * For example, in any application there can be an employee entity. In this
	 * case, primary key will definitely be “employee id” but in cases such as login
	 * by email, user will provide email and password. In this case, in stead of
	 * writing named query, you can directly use @NaturalId annotation on “email”
	 * field.
	 */
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // https://www.javatpoint.com/jpa-many-to-one-mapping
    //https://stackoverflow.com/questions/2990799/difference-between-fetchtype-lazy-and-eager-in-java-persistence-api
    //CascadeType.REMOVE : cascade type remove removes all related entities association with this setting when the owning entity is deleted.
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderMain orderMain;


    @NotEmpty
    private String productId;

    
    @NotEmpty
    private String productName;


    @NotNull
    private String productDescription;


    private String productIcon;


    @NotNull
    private Integer categoryType;

 
    @NotNull
    private BigDecimal productPrice;

 
    @Min(0)
    private Integer productStock;

    @Min(1)
    private Integer count;


    public ProductInOrder(ProductInfo productInfo, Integer quantity) {
        this.productId = productInfo.getProductId();
        this.productName = productInfo.getProductName();
        this.productDescription = productInfo.getProductDescription();
        this.productIcon = productInfo.getProductIcon();
        this.categoryType = productInfo.getCategoryType();
        this.productPrice = productInfo.getProductPrice();
        this.productStock = productInfo.getProductStock();
        this.count = quantity;
    }

    @Override
    public String toString() {
        return "ProductInOrder{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productIcon='" + productIcon + '\'' +
                ", categoryType=" + categoryType +
                ", productPrice=" + productPrice +
                ", productStock=" + productStock +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductInOrder that = (ProductInOrder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productDescription, that.productDescription) &&
                Objects.equals(productIcon, that.productIcon) &&
                Objects.equals(categoryType, that.categoryType) &&
                Objects.equals(productPrice, that.productPrice);
    }
    
	/*
	 * Generates a hash code for a sequence of input values. The hash code is
	 * generated as if all the input values were placed into an array, and that
	 * array were hashed by calling Arrays.hashCode(Object[]). This method is useful
	 * for implementing Object.hashCode() on objects containing multiple fields. For
	 * example, if an object that has three fields, x, y, and z, one could write:
	 * 
	 * @Override public int hashCode() { return Objects.hash(x, y, z); } Returns: a
	 * hash value of the sequence of input values
	 * 
	 * https://stackoverflow.com/questions/11597386/objects-hash-vs-objects-hashcode-clarification-needed
	 */

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, productId, productName, productDescription, productIcon, categoryType, productPrice);
    }
}
