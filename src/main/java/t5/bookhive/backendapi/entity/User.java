package t5.bookhive.backendapi.entity;

//importing JsonIgnore to mark a property or list of properties to be ignored.
import com.fasterxml.jackson.annotation.JsonIgnore;

//importing to generate getters,setters,constructors etc.
import lombok.Data;
import lombok.NoArgsConstructor;

//https://howtodoinjava.com/hibernate/hibernate-naturalid-example-tutorial/
import org.hibernate.annotations.NaturalId;

//For performing cascading
import javax.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//https://www.geeksforgeeks.org/serialization-in-java/
import java.io.Serializable;

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
@Table(name = "users")
@NoArgsConstructor
public class User implements Serializable {

	/*
	 * The serialVersionUID is a universal version identifier for a Serializable
	 * class. Deserialization uses this number to ensure that a loaded class
	 * corresponds exactly to a serialized object. If no match is found, then an
	 * InvalidClassException is thrown
	 */
    private static final long serialVersionUID = 4887904943282174032L;
    
	/*
	 * https://thorben-janssen.com/jpa-generate-primary-keys/ Hibernate selects the
	 * generation strategy based on the used dialect
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    @NaturalId
    @NotEmpty
    private String email;
    
    @NotEmpty
    @Size(min = 3, message = "Length must be more than 3")
    private String password;
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String phone;
    
    @NotEmpty
    private String address;
    
    @NotNull
    private boolean active;
    
    @NotEmpty
    private String role = "ROLE_CUSTOMER";

	/*
	 * https://www.baeldung.com/jpa-cascade-types#1-cascadetypeall Entity
	 * relationships often depend on the existence of another entity, for example
	 * the Person–Address relationship. Without the Person, the Address entity
	 * doesn't have any meaning of its own. When we delete the Person entity, our
	 * Address entity should also get deleted.Cascading is the way to achieve this.
	 * When we perform some action on the target entity, the same action will be
	 * applied to the associated entity.
	 */    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // fix bi-direction toString() recursion problem
    private Cart cart;




    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", role='" + role + '\'' +
                '}';
    }

}

