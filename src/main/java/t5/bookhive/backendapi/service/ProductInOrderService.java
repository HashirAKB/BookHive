package t5.bookhive.backendapi.service;

import t5.bookhive.backendapi.entity.ProductInOrder;
import t5.bookhive.backendapi.entity.User;


public interface ProductInOrderService {
	
    void update(String itemId, Integer quantity, User user);
    ProductInOrder findOne(String itemId, User user);
}
