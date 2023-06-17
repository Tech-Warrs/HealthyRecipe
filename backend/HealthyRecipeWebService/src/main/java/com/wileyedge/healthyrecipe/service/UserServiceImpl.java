package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.Utilities.TokenUtils;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.DuplicateEmailException;
import com.wileyedge.healthyrecipe.exception.InvalidPasswordException;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.exception.UsernameAlreadyExistsException;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class UserServiceImpl implements UserServiceInterface {

	private UserRepository userRepository;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User createUser(User user) {
		// Check if email already exists
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateEmailException("Email already exists: " + user.getEmail());
		}
		//Check if username already exists
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UsernameAlreadyExistsException("Username already exists: " + user.getUsername());
		}

		//Check if password valid
		if (!user.isPasswordValid()) {
			throw new InvalidPasswordException("Password must be at least 8 characters including one uppercase, one lowercase, one number, and one symbol.");
		}

		return userRepository.save(user);
	}

	@Override
	public User updateUserDetailsById(User user) {

		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new UserNotFoundException("User ID : " + user.getId()));

		if (user.getUsername() != null && !user.getUsername().isBlank()) {
			existingUser.setUsername(user.getUsername());
		}

		if (user.getEmail() != null && !user.getEmail().isBlank()) {
			existingUser.setEmail(user.getEmail());
		}

		if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
			existingUser.setFirstName(user.getFirstName());
		}

		if (user.getLastName() != null && !user.getLastName().isBlank()) {
			existingUser.setLastName(user.getLastName());
		}

		return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(long userIdToDelete, String token) {
		// validates the integrity and authenticity of the token based on its signature and other integrity checks
		if (!tokenUtils.isTokenValid(token)) {
			throw new InvalidTokenException("Invalid token");
		}

		// Validate user embedded in the token is actually exist
		User loggedInUser = tokenUtils.getUserFromToken(token);
		if (loggedInUser == null) {
			throw new UserNotFoundException("no username found in the provided token");
		}

		// Check if the logged-in user has admin role
		if (!loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
			throw new UnauthorizedAccessException("Only admin users can delete users");
		}

		// Find if the user to be deleted is exist in db
		Optional<User> userOptional = userRepository.findById(userIdToDelete);
		if(userOptional.isPresent()) {
			userRepository.deleteById(userIdToDelete);
		}else {
			throw new UserNotFoundException("User ID : " + userIdToDelete);
		}
	}

	@Override
	public Optional<User> findUserById(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public String loginUser(String identifier, String password) {
		boolean isEmail = identifier.contains("@") && identifier.contains(".");
		User user = isEmail ? userRepository.findByEmail(identifier) : userRepository.findByUsername(identifier);

		if (user != null && user.getPassword().equals(password)) {
			String token = tokenUtils.generateToken(user);
			user.setToken(token);
			return token;
		} else {
			throw new UserNotFoundException("Invalid credentials.");
		}
	}

	@Override
	public void logoutUser(String token) {
		// Extract the token from the Authorization header
		String authToken = token.replace("Bearer ", "");

		// Validate and decode the token to retrieve the user details
		User user = tokenUtils.getUserFromToken(authToken);

		user.setToken("");
	}

}
