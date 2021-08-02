/*Declaring cart services*/
package me.zhulin.shopapi.service;

import me.zhulin.shopapi.entity.Cart;
import me.zhulin.shopapi.entity.ProductInOrder;
import me.zhulin.shopapi.entity.User;

import java.util.Collection;


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
