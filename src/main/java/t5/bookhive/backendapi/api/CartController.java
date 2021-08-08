package t5.bookhive.backendapi.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import t5.bookhive.backendapi.entity.Cart;
import t5.bookhive.backendapi.entity.ProductInOrder;
import t5.bookhive.backendapi.entity.User;
import t5.bookhive.backendapi.form.ItemForm;
import t5.bookhive.backendapi.repository.ProductInOrderRepository;
import t5.bookhive.backendapi.service.CartService;
import t5.bookhive.backendapi.service.ProductInOrderService;
import t5.bookhive.backendapi.service.ProductService;
import t5.bookhive.backendapi.service.UserService;

//https://www.baeldung.com/security-context-basics
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

/*This @CrossOrigin annotation enables cross-origin resource sharing only for this specific method. By default, its allows all origins, all headers, and the HTTP methods specified in the @RequestMapping annotation.
@RestController is a Spring annotation that is used to build REST API in a declarative way. RestController annotation is applied to a class to mark it as a request handler, and Spring will do the building and provide the RESTful web service at runtime.
https://codeburst.io/rest-controller-building-rest-api-638d3ff4fa71
*/
@CrossOrigin
@RestController

/*
 * https://www.baeldung.com/spring-requestmapping
 */
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductInOrderService productInOrderService;
    @Autowired
    ProductInOrderRepository productInOrderRepository;

	/* Method implementation for adding items into existing cart */
    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        User user = userService.findOne(principal.getName());
        try {
            cartService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(user));
    }

	/* Retrieveing cart details */
    @GetMapping("")
    public Cart getCart(Principal principal) {
        User user = userService.findOne(principal.getName());
        return cartService.getCart(user);
    }

	/* Adding items into the cart */
    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        var productInfo = productService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

	/* Updating number of items in a cart */
	/*
	 * The @PathVariable annotation is used to extract the value from the URI. It is
	 * most suitable for the RESTful web service where the URL contains some value.
	 * Spring MVC allows us to use multiple @PathVariable annotations in the same
	 * method. A path variable is a critical part of creating rest resources.
	 * https://www.javatpoint.com/restful-web-services-path-variable
	 */
    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
         productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

	/* Deleting cart items */
	/*
	 * https://stackoverflow.com/questions/62853266/how-to-have-deletemapping-in-
	 * spring-that-deletes-based-on-different-types-of-pat
	 */    
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = userService.findOne(principal.getName());
		/* Deleting the item from the cart of respective user. */
         cartService.delete(itemId, user);
         // flush memory into DB
    }


	/* Checkout method */
    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        User user = userService.findOne(principal.getName());// Email as username
        cartService.checkout(user);
        return ResponseEntity.ok(null);
    }


}
