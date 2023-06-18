package com.wileyedge.healthyrecipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.DuplicateEmailException;
import com.wileyedge.healthyrecipe.exception.InvalidCredentialException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.exception.UsernameAlreadyExistsException;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class MemberServiceImpl implements IMemberService {

	private UserRepository userRepository;
	private AuthService authService;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public MemberServiceImpl(UserRepository userRepository,AuthService authService, BCryptPasswordEncoder passwordEncoder) {
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
	public User updateUserDetailsById(User user, String token) {
		//Validate token
		User loggedInUser = authService.isTokenValid(token);

		//Check if the ID of user to be updated is the same as the Id of the user in the token
		boolean isSameUser = loggedInUser.getId() == user.getId();
		if(!isSameUser) {
			throw new UnauthorizedAccessException("You are not authorized to edit details user ID : " + user.getId() );
		}

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
		//Check if token is valid (not expired, user in token is existed and token match with token stored in Db)
		User loggedInUser = authService.isTokenValid(token);

		//Check if the ID of user to be updated is the same as the Id of the user in the token
		boolean isSameUser = loggedInUser.getId() == userIdToDelete;
		if(!isSameUser) {
			throw new UnauthorizedAccessException("You are not authorized to delete details user ID : " + userIdToDelete );
		}

		userRepository.deleteById(userIdToDelete);

	}

	@Override
	public User findUserById(long userId, String token) {
		// Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		//Check if the ID of user to be updated is the same as the Id of the user in the token
		boolean isSameUser = loggedInUser.getId() == userId;

		if(!isSameUser) {
			throw new UnauthorizedAccessException("You are not authorized to view details user ID : " + userId );
		}

		return userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User ID: " + userId));
	}

	@Override
	public User findUserByEmail(String email, String token) {

		User loggedInUser = authService.isTokenValid(token);

		//Check if the ID of user to be updated is the same as the Id of the user in the token
		boolean isSameUser = loggedInUser.getUsername().equalsIgnoreCase(email);

		if(!isSameUser) {
			throw new UnauthorizedAccessException("You are not authorized to view details user : " + email );
		}

		return userRepository.findByEmail(email);
	}

	@Override
	public User findUserByUsername(String username, String token) {
		//Check if token is valid
		User loggedInUser = authService.isTokenValid(token);

		//Check if the username of user to be updated is the same as the username of the user in the token
		boolean isSameUser = loggedInUser.getUsername().equalsIgnoreCase(username);

		if(!isSameUser) {
			throw new UnauthorizedAccessException("You are not authorized to view details user : " + username );
		}		

		return userRepository.findByUsername(username);
	}


}
