package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import com.wileyedge.healthyrecipe.model.User;

public interface UserServiceInterface {

	List<User> findAllUsers();

	User findUserByUsername(String username);

	User findUserByEmail(String email);

	Optional<User> findUserById(int userId);

	User updateUser(User user);

	User createUser(User user);

	void deleteUser(int userId);

}
