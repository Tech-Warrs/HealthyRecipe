package com.wileyedge.healthyrecipe.Utilities;



import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtils {
	private UserRepository userRepository;
	private Key key;
	
	@Autowired
    public TokenUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}



	  public String generateToken(User user) {
	        String role = user.getRole();

	        // Generate the token
	        String token = Jwts.builder()
	                .setSubject(user.getUsername())
	                .claim("role", role)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token validity: 24 hours
	                .signWith(key, SignatureAlgorithm.HS256)
	                .compact();

	        return token;
	    }
    

	//validate token
	  public boolean isTokenValid(String token) {
		  token = token.trim(); // Strip leading and trailing whitespace
		    
	        try {
	            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);         	            
	            return true;
	        } catch (ExpiredJwtException ex) {
	        	System.out.println("Expired token");
	        	System.out.println(ex.getMessage());
	            return false;
	        } catch (JwtException ex) {
	        	System.out.println("JWTException ");
	        	System.out.println(ex.getMessage());
	        
	            return false;
	        }
	    }
	
	
    public User getUserFromToken(String token) {    
    	try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = (String) claims.get("role");

            // Retrieve the user from the database based on the username
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("User not found.");
            }
            
            user.setRole(role);

            return user;
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("Token has expired.");
        } catch (JwtException ex) {
            throw new InvalidTokenException("Invalid token.");
        }

    
    }
        

}
