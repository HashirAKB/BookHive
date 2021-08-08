package t5.bookhive.backendapi.service;


import java.util.Collection;

import t5.bookhive.backendapi.entity.User;


public interface UserService {
	
	//Fn to find user by using the email.
    User findOne(String email);

    //Return the list of users based on their assigned role.
    Collection<User> findByRole(String role);

    //Fn to save new user.
    User save(User user);

    //Fn to update user details
    User update(User user);
}
