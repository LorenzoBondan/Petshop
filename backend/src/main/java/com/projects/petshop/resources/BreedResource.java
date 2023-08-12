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

import com.projects.petshop.dto.BreedDTO;
import com.projects.petshop.services.BreedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "breeds-API")
@RestController
@RequestMapping(value = "/breeds")
public class BreedResource {
	
	@Autowired
	private BreedService service;
	
	@Operation(summary = "Get all the breeds", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful search"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<Page<BreedDTO>> findAll(Pageable pageable){		
		Page<BreedDTO> list = service.findAllPaged(pageable);	
		return ResponseEntity.ok().body(list);
	}

	@Operation(summary = "Get one breed by id", method = "GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful search"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<BreedDTO> findById(@PathVariable Long id) {
		BreedDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Insert a new breed", method = "POST")
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
	public ResponseEntity<BreedDTO> insert (@RequestBody BreedDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);	
	}
	
	@Operation(summary = "Update an breed", method = "PUT")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "422", description = "Invalid Data"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<BreedDTO> update(@PathVariable Long id, @RequestBody BreedDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@Operation(summary = "Delete an breed", method = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Deleted Successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "403", description = "Forbbiden"),
			@ApiResponse(responseCode = "404", description = "Object not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<BreedDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
