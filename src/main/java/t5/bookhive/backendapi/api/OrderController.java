package t5.bookhive.backendapi.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import t5.bookhive.backendapi.entity.OrderMain;
import t5.bookhive.backendapi.entity.ProductInOrder;
import t5.bookhive.backendapi.service.OrderService;
import t5.bookhive.backendapi.service.UserService;

import java.util.Collection;

/*This @CrossOrigin annotation enables cross-origin resource sharing only for this specific method. By default, its allows all origins, all headers, and the HTTP methods specified in the @RequestMapping annotation.
@RestController is a Spring annotation that is used to build REST API in a declarative way. RestController annotation is applied to a class to mark it as a request handler, and Spring will do the building and provide the RESTful web service at runtime.
https://codeburst.io/rest-controller-building-rest-api-638d3ff4fa71
*/
@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

	/* Retrieving order details(AS API Microservice) */
	/*
	 * using Pageable to return json results with pages. If the number of items
	 * returned in the server's JSON response is too large, the server can limit the
	 * number of items in the JSON to a small subset ("page") of the total available
	 * set to reduce the amount of data transferred from the server and speed up the
	 * server response time. In this case, the server will provide links to get the
	 * previous and next JSON pages from the dataset.
	 
	 * Pagination is often helpful when we have a large dataset and we want to present it to the user in smaller chunks.Also, we often need to sort that data by some criteria while paging.

	 * https://reqbin.com/req/yqyqa5ve/json-pagination-example
	 * https://reqbin.com/req/java/yqyqa5ve/json-pagination-example
	 * https://stackoverflow.com/questions/53213655/pageable-rest-api
	 * https://www.baeldung.com/spring-data-jpa-pagination-sorting
	 */    
    
    @GetMapping("/order")
    public Page<OrderMain> orderList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     Authentication authentication) {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<OrderMain> orderPage;
        
		/*
		 * https://stackoverflow.com/questions/12612096/how-to-check-if-authority-exists
		 * -in-a-collection-of-grantedauthority
		 * https://www.programcreek.com/java-api-examples/?api=org.springframework.
		 * security.core.authority.SimpleGrantedAuthority
		 */        
        
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            orderPage = orderService.findByBuyerEmail(authentication.getName(), request);
        } else {
            orderPage = orderService.findAll(request);
        }
        return orderPage;
    }

	/* Cancelling order using order id
	 * https://www.sourcecodeexamples.net/2019/10/patchmapping-spring-boot-example.
	 * html https://www.baeldung.com/http-put-patch-difference-spring
	 */
    @PatchMapping("/order/cancel/{id}")
    public ResponseEntity<OrderMain> cancel(@PathVariable("id") Long orderId, Authentication authentication) {
        OrderMain orderMain = orderService.findOne(orderId);
        if (!authentication.getName().equals(orderMain.getBuyerEmail()) && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {

			/* Returns unauthorized exception if not authorized */
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.cancel(orderId));
    }

	/*
	 * https://www.sourcecodeexamples.net/2019/10/patchmapping-spring-boot-example.
	 * html https://www.baeldung.com/http-put-patch-difference-spring
	 */    
    @PatchMapping("/order/finish/{id}")
    public ResponseEntity<OrderMain> finish(@PathVariable("id") Long orderId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
		/* Changing the order status to finish */
        return ResponseEntity.ok(orderService.finish(orderId));
    }

	/* Retrieveing order items */
    @GetMapping("/order/{id}")
    public ResponseEntity show(@PathVariable("id") Long orderId, Authentication authentication) {
        boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        OrderMain orderMain = orderService.findOne(orderId);
		/*
		 * if (isCustomer &&
		 * !authentication.getName().equals(orderMain.getBuyerEmail())) { return
		 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
		 */

        Collection<ProductInOrder> items = orderMain.getProducts();
        return ResponseEntity.ok(orderMain);
    }
}
