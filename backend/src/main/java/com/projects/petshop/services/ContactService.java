package com.projects.petshop.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class ContactService {

	@Autowired
	private ContactRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ContactDTO> findAllPaged(Pageable pageable) {
		Page<Contact> list = repository.findAll(pageable);
		return list.map(x -> new ContactDTO(x));
	}

	@Transactional(readOnly = true)
	public ContactDTO findById(Long id) {
		Optional<Contact> obj = repository.findById(id);
		Contact entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ContactDTO(entity);
	}

	@Transactional
	public ContactDTO insert(ContactDTO dto) {
		Contact entity = new Contact();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ContactDTO(entity);
	}

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

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}
	
	private void copyDtoToEntity(ContactDTO dto, Contact entity) {
		entity.setTag(dto.getTag());
		entity.setType(dto.getType());
		entity.setValue(dto.getValue());
		entity.setClient(clientRepository.getOne(dto.getClientId()));
	}
}
