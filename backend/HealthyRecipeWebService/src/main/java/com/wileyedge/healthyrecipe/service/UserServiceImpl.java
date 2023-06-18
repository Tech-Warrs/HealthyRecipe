package com.wileyedge.healthyrecipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.DuplicateEmailException;
import com.wileyedge.healthyrecipe.exception.InvalidCredentialException;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.exception.UsernameAlreadyExistsException;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class UserServiceImpl implements IUserService {

	private UserRepository userRepository;
	private AuthService authService;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository,AuthService authService, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authService = authService;
		this.passwordEncoder = passwordEncoder;
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
			throw new InvalidCredentialException("Password must be at least 8 characters including one uppercase, one lowercase, one number, and one symbol.");
		}

		// Salt and bcrypt the password
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);

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
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action :  Delete User.");
		}

		// Check if the user to be deleted is exist in db
		Optional<User> userOptional = userRepository.findById(userIdToDelete);
		if(userOptional.isPresent()) {
			userRepository.deleteById(userIdToDelete);
		}else {
			throw new UserNotFoundException("User ID : " + userIdToDelete);
		}
	}

	@Override
	public Optional<User> findUserById(long userId, String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action :  Find user by ID.");
		}

		return userRepository.findById(userId);
	}

	@Override
	public User findUserByEmail(String email, String token) {

		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		if (!loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
			throw new InvalidTokenException("ACCESS Denied for action :  Find user by email.");
		}
		return userRepository.findByEmail(email);
	}

	@Override
	public User findUserByUsername(String username, String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action :  Find user by name.");
		}

		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers(String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		// Check if the user has the role "ADMIN"
		boolean loggedInUserIsAdmin = authService.isLoggedInUserHasAdminRole(loggedInUser);
		if (!loggedInUserIsAdmin) {
			throw new UnauthorizedAccessException("ACCESS Denied for action : Get all users.");
		}

		return userRepository.findAll();
	}


}
