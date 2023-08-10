package com.projects.petshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.entities.User;
import com.projects.petshop.repositories.UserRepository;
import com.projects.petshop.services.exceptions.ForbiddenException;
import com.projects.petshop.services.exceptions.UnauthorizedException;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public User authenticated() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			return userRepository.findByCpf(username);
		}
		catch(Exception e) {
			throw new UnauthorizedException("Invalid user");
		}
	}
	
	public void validateSelfOrAdmin(String userCpf) { 
		User user = authenticated();
		
		if(!user.getCpf().equals(userCpf) && !user.hasRole("ROLE_ADMIN")) { 
			throw new ForbiddenException("Access denied");
		}
	}
}