package me.zhulin.shopapi.service;


import me.zhulin.shopapi.entity.User;

import java.util.Collection;


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
