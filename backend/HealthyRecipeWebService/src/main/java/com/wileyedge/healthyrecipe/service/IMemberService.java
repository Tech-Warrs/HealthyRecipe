package com.wileyedge.healthyrecipe.service;


import com.wileyedge.healthyrecipe.model.User;

public interface IMemberService {
	
	User createUser(User user);
	
	User findUserById(long userId, String token);

	User findUserByUsername(String username, String token);

	User findUserByEmail(String email, String token);

	User updateUserDetailsById(User user, String token);
	
	void deleteUser(long userId, String token);

}
