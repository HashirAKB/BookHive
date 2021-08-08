/*Declaring cart services*/
package t5.bookhive.backendapi.service;

import java.util.Collection;

import t5.bookhive.backendapi.entity.Cart;
import t5.bookhive.backendapi.entity.ProductInOrder;
import t5.bookhive.backendapi.entity.User;


public interface CartService {
	
	//Getting cart details for specific user.
    Cart getCart(User user);

    //Method to update existing cart
    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    //Method for Deleting items from cart.
    void delete(String itemId, User user);

    //Checkout Function
    void checkout(User user);
}
