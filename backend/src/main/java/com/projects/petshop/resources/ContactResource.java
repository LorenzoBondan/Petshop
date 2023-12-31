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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "contacts-API")
@RestController
@RequestMapping(value = "/contacts")
public class ContactResource {

	@Autowired
	private ContactService service;
	
	@Autowired
	private AuthService authService;
	
	@Operation(summary = "Update the user contact", method = "PUT")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "422", description = "Invalid Data"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
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
