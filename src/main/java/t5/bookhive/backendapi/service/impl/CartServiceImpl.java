/*Declaring cart services*/
package t5.bookhive.backendapi.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import t5.bookhive.backendapi.entity.Cart;
import t5.bookhive.backendapi.entity.OrderMain;
import t5.bookhive.backendapi.entity.ProductInOrder;
import t5.bookhive.backendapi.entity.User;
import t5.bookhive.backendapi.repository.CartRepository;
import t5.bookhive.backendapi.repository.OrderRepository;
import t5.bookhive.backendapi.repository.ProductInOrderRepository;
import t5.bookhive.backendapi.repository.UserRepository;
import t5.bookhive.backendapi.service.CartService;
import t5.bookhive.backendapi.service.ProductService;
import t5.bookhive.backendapi.service.UserService;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/*Spring @Service annotation is a specialization of @Component annotation. Spring Service annotation can be applied only to classes. It is used to mark the class as a service provider.*/

/*https://www.baeldung.com/spring-depends-on
 * We should use this annotation for specifying bean dependencies. Spring guarantees that the defined beans will be initialized before attempting an initialization of the current bean.

we have a UserServiceImpl which depends on passwordEncoder. In this case, passwordEncoder should be initialized before the UserServiceImpl.
*/
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserService userService;

    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
    	
		/* retrieveing cart details(as obj) from db. */
        Cart finalCart = user.getCart();
        
        productInOrders.forEach(productInOrder -> {
			/*
			 * Adding all the products into a set of name set from the FinalCart variable
			 * using getProducts Fn.
			 */
            Set<ProductInOrder> set = finalCart.getProducts();
            
			/*
			 * https://www.baeldung.com/java-optional
			 * Looking whether the product(book) is already in the cart.
			 */            
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            
            /*If it is present increasing the number of item when adding it again into the cart*/
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
            	
				/* If the item is not already in the cart, Adding the item into the cart */
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
			/* Adding the items in the cart to the ordered products table. */
            productInOrderRepository.save(prod);
        });
        
		/* Adding the cart into database table cart */
        cartRepository.save(finalCart);

    }

	/* Deleting a specific item from the cart of a user */
    @Override
    @Transactional
    public void delete(String itemId, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            productInOrderRepository.deleteById(productInOrder.getId());
        });
    }



    @Override
    @Transactional
    public void checkout(User user) {
        // Creat an order
        OrderMain order = new OrderMain(user);
        orderRepository.save(order);

        // clear cart's foreign key & set order's foreign key& decrease stock
        user.getCart().getProducts().forEach(productInOrder -> {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);
        });

    }
}
