package com.projects.petshop.resources;

import java.net.URI;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projects.petshop.dto.UserDTO;
import com.projects.petshop.dto.UserInsertDTO;
import com.projects.petshop.dto.UserUpdateDTO;
import com.projects.petshop.entities.User;
import com.projects.petshop.services.AuthService;
import com.projects.petshop.services.UserService;
import com.projects.petshop.services.exceptions.ForbiddenException;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService service;
	
	@Autowired
	private AuthService authService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(value = "name", defaultValue = "") String name, Pageable pageable) {
		Page<UserDTO> list = service.findAllPaged(name.trim(), pageable);	
		return ResponseEntity.ok().body(list);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@GetMapping(value = "/{cpf}") 
	public ResponseEntity<UserDTO> findByCpf(@PathVariable String cpf) {
		User user = authService.authenticated();
		if(!authService.isAdmin() && !user.getCpf().equals(cpf)) {
			throw new ForbiddenException("You can't see information that is not yours");
		}
		UserDTO dto = service.findByCpf(cpf);	
		return ResponseEntity.ok().body(dto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<UserDTO> insert (@Valid @RequestBody UserInsertDTO dto) {
		UserDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}")
				.buildAndExpand(newDto.getCpf()).toUri();
		return ResponseEntity.created(uri).body(newDto);	
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@PutMapping(value = "/{cpf}")
	public ResponseEntity<UserDTO> update(@PathVariable String cpf, @Valid @RequestBody UserUpdateDTO dto)	{
		User user = authService.authenticated();
		if(!authService.isAdmin() && !user.getCpf().equals(cpf)) {
			throw new ForbiddenException("You can't update information that is not yours");
		}
		UserDTO newDto = service.update(cpf, dto);
		return ResponseEntity.ok().body(newDto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{cpf}")
	public ResponseEntity<UserDTO> delete(@PathVariable String cpf){
		service.delete(cpf);
		return ResponseEntity.noContent().build();
	}
}
