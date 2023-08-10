package com.projects.petshop.services;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.dto.ClientDTO;
import com.projects.petshop.dto.PetDTO;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Pet;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.repositories.PetRepository;
import com.projects.petshop.repositories.UserRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(Pageable pageable) {
		Page<Client> list = repository.findAll(pageable);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		
		Instant date = Instant.now();
		entity.setRegisterDate(date);
		
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			entity.setRegisterDate(dto.getRegisterDate());
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
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
	
	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		//entity.setRegisterDate(dto.getRegisterDate());
		entity.setUser(userRepository.findByCpf(dto.getUserCpf()));
		entity.setImgUrl(dto.getImgUrl());
		entity.setAddress(addressRepository.getOne(dto.getAddress().getId()));
		entity.setContact(contactRepository.getOne(dto.getContact().getId()));
		
		for (PetDTO petDto : dto.getPets()) {
			Pet pet = petRepository.getOne(petDto.getId());
			entity.getPets().add(pet);
		}
	}
}
