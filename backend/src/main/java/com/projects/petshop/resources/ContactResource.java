package com.projects.petshop.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.services.ContactService;

@RestController
@RequestMapping(value = "/contacts")
public class ContactResource {

	@Autowired
	private ContactService service;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody ContactDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
}
