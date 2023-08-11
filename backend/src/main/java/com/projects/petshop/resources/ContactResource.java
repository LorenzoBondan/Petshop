package com.projects.petshop.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.entities.User;
import com.projects.petshop.services.AuthService;
import com.projects.petshop.services.ContactService;
import com.projects.petshop.services.exceptions.UnauthorizedException;

@RestController
@RequestMapping(value = "/contacts")
public class ContactResource {

	@Autowired
	private ContactService service;
	
	@Autowired
	private AuthService authService;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody ContactDTO dto) {
		User user = authService.authenticated();
		if(!authService.isAdmin() && id != user.getClient().getContact().getId()) {
			throw new UnauthorizedException("You can't update an contact that is not yours");
		}
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
}
