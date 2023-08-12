package com.projects.petshop.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projects.petshop.dto.ClientDTO;
import com.projects.petshop.entities.User;
import com.projects.petshop.services.AuthService;
import com.projects.petshop.services.ClientService;
import com.projects.petshop.services.exceptions.UnauthorizedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "petshop-API")
@RestController
@RequestMapping(value = "/clients")
public class ClientResource {

	@Autowired
	private ClientService service;
	
	@Autowired
	private AuthService authService;
	
	@Operation(summary = "Get all the clients", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful search"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Page<ClientDTO>> findAll(Pageable pageable){		
		Page<ClientDTO> list = service.findAllPaged(pageable);	
		return ResponseEntity.ok().body(list);
	}
	
	@Operation(summary = "Get one assistance by id", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful search"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
		User user = authService.authenticated();
		if(!authService.isAdmin() && id != user.getClient().getId()) {
			throw new UnauthorizedException("You can't see information that is not yours");
		}
		ClientDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Insert a new client", method = "POST")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful insertion"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "422", description = "Invalid Data"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ClientDTO> insert (@RequestBody ClientDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);	
	}
	
	@Operation(summary = "Update an client", method = "PUT")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "422", description = "Invalid Data"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto) {
		User user = authService.authenticated();
		if(!authService.isAdmin() && id != user.getClient().getId()) {
			throw new UnauthorizedException("You can't update information that is not yours");
		}
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Delete an client", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Deleted Successfully"),
			@ApiResponse(responseCode = "400", description = "Integrity Violation"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "404", description = "Object not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
