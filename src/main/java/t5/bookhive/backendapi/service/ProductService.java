package t5.bookhive.backendapi.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import t5.bookhive.backendapi.entity.ProductInfo;


public interface ProductService {

    ProductInfo findOne(String productId);

    // For all books that are open for buying
    Page<ProductInfo> findUpAll(Pageable pageable);
    
    // For all books in the list
    Page<ProductInfo> findAll(Pageable pageable);
    
    // Books By category
    Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable);

    // increase stock
    void increaseStock(String productId, int amount);

    //decrease stock
    void decreaseStock(String productId, int amount);

    ProductInfo offSale(String productId);

    ProductInfo onSale(String productId);

    ProductInfo update(ProductInfo productInfo);
    ProductInfo save(ProductInfo productInfo);

    void delete(String productId);


}
