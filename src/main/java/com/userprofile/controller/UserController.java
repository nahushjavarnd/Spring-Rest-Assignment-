package com.userprofile.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;

import com.userprofile.entity.*;
import com.userprofile.repository.UserRepository;
import com.userprofile.service.UserService;
import com.userprofile.service.impl.JwtService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Autowired(required = true)
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home() {
		return "welcome to the home page";

	}

	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }

	@PostMapping("/register")
	public ResponseEntity<UserInfo> register(@RequestBody UserInfo user) {
		try {
			UserInfo user1 = userService.register(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user1);
		} catch (HttpClientErrorException ex) {
			// Handle 4xx client errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			// Handle 5xx server errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			// Handle other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/user/{Id}")
	public ResponseEntity<UserInfo> getProfile(@PathVariable String Id) {
		try {
			UserInfo user = userService.getUser(Id);
			return ResponseEntity.ok(user);
		} catch (HttpClientErrorException ex) {
			// Handle 4xx client errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			// Handle 5xx server errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			// Handle other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping
	public ResponseEntity<List<UserInfo>> getAllUser() {
		try {
			List<UserInfo> allUser = userService.getAllUser();
			return ResponseEntity.ok(allUser);
		} catch (HttpClientErrorException ex) {
			// Handle 4xx client errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			// Handle 5xx server errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			// Handle other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{Id}")
	public ResponseEntity<UserInfo> updateProfile(@PathVariable String Id, @RequestBody UserInfo user) {
		try {
			Optional<UserInfo> existingUser = userRepository.findById(Id);
			if (existingUser.isPresent()) {
				user.setId(Id);
				userRepository.save(user);
				return new ResponseEntity<>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (HttpClientErrorException ex) {
			// Handle 4xx client errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			// Handle 5xx server errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			// Handle other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{Id}")
	public ResponseEntity<UserInfo> deleteProfile(@PathVariable String Id) {
		try {
			Optional<UserInfo> existingUser = userRepository.findById(Id);
			if (existingUser.isPresent()) {
				userRepository.deleteById(Id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (HttpClientErrorException ex) {
			// Handle 4xx client errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			// Handle 5xx server errors
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			// Handle other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
