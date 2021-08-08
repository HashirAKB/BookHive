package t5.bookhive.backendapi.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import t5.bookhive.backendapi.entity.Cart;
import t5.bookhive.backendapi.entity.User;
import t5.bookhive.backendapi.enums.ResultEnum;
import t5.bookhive.backendapi.exception.MyException;
import t5.bookhive.backendapi.repository.CartRepository;
import t5.bookhive.backendapi.repository.UserRepository;
import t5.bookhive.backendapi.service.UserService;

import java.util.Collection;

/*Spring @Service annotation is a specialization of @Component annotation. Spring Service annotation can be applied only to classes. It is used to mark the class as a service provider.*/

/*https://www.baeldung.com/spring-depends-on
 * We should use this annotation for specifying bean dependencies. Spring guarantees that the defined beans will be initialized before attempting an initialization of the current bean.

we have a UserServiceImpl which depends on passwordEncoder. In this case, passwordEncoder should be initialized before the UserServiceImpl.
*/
@Service
@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {
	
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    //returns user obj
    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    //returns collection of users based on role.
    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional
    public User save(User user) {
        //register
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepository.save(user);

            // initial Cart
            Cart savedCart = cartRepository.save(new Cart(savedUser));
            savedUser.setCart(savedCart);
            return userRepository.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }

    }

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }

}
