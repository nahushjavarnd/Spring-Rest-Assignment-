package com.userprofile.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.userprofile.entity.UserInfo;
import com.userprofile.exception.ResourceNotFoundException;
import com.userprofile.repository.UserRepository;
import com.userprofile.service.UserService;

@Service
public class UserServiceImpl<LoginRequest> implements UserService {

	@Autowired
	private UserRepository userRepository;
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserInfo register(UserInfo user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		String randomUserId = UUID.randomUUID().toString(); // generate unique user id
		user.setId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<UserInfo> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public UserInfo getUser(String id) {
		UserInfo user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("User with given id is not found on server !! : " + id));
		return user;

	}

	

}
