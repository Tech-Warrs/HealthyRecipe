package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import com.wileyedge.healthyrecipe.model.User;

public interface IUserService {

	List<User> findAllUsers(String token);
	
	User updateUserDetailsById(User user);

	User createUser(User user);

	void deleteUser(long userId, String token);

	User findUserByUsername(String username, String token);

	User findUserByEmail(String email, String token);

	Optional<User> findUserById(long userId, String token);

}
