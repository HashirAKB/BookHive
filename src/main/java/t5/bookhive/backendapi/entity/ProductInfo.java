package t5.bookhive.backendapi.entity;

/*Books info(Individual product details) Implementation Of BookHive*/

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ProductInfo implements Serializable {
	
	
    @Id
    private String productId;


    @NotNull
    private String productName;


    @NotNull
    private BigDecimal productPrice;


    @NotNull
    @Min(0)
    private Integer productStock;


    private String productDescription;


    private String productIcon;

	/* productStatus is used here to set the item is open for sale or not 
	 * By dafault it is 0 which means its open for sale.*/
    @ColumnDefault("0")
    private Integer productStatus;



    @ColumnDefault("0")
    private Integer categoryType;

    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

    public ProductInfo() {
    }
}
