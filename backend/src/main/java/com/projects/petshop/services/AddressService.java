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

import com.projects.petshop.dto.AddressDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<AddressDTO> findAllPaged(Pageable pageable) {
		Page<Address> list = repository.findAll(pageable);
		return list.map(x -> new AddressDTO(x));
	}

	@Transactional(readOnly = true)
	public AddressDTO findById(Long id) {
		Optional<Address> obj = repository.findById(id);
		Address entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new AddressDTO(entity);
	}

	@Transactional
	public AddressDTO insert(AddressDTO dto) {
		Address entity = new Address();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new AddressDTO(entity);
	}

	@Transactional
	public AddressDTO update(Long id, AddressDTO dto) {
		try {
			Address entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new AddressDTO(entity);
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
	
	private void copyDtoToEntity(AddressDTO dto, Address entity) {
		entity.setStreet(dto.getStreet());
		entity.setCity(dto.getCity());
		entity.setComplement(dto.getComplement());
		entity.setNeighborhood(dto.getNeighborhood());
		entity.setTag(dto.getTag());
		entity.setClient(clientRepository.getOne(dto.getClientId()));
	}
}
