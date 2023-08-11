package com.projects.petshop.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class ContactService {

	@Autowired
	private ContactRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Transactional
	public ContactDTO update(Long id, ContactDTO dto) {
		try {
			Contact entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ContactDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	private void copyDtoToEntity(ContactDTO dto, Contact entity) {
		entity.setTag(dto.getTag());
		entity.setType(dto.getType());
		entity.setValue(dto.getValue());
		entity.setClient(clientRepository.getOne(dto.getClientId()));
	}
}
