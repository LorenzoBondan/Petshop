package com.projects.petshop.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.dto.AddressDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;

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
	
	private void copyDtoToEntity(AddressDTO dto, Address entity) {
		entity.setStreet(dto.getStreet());
		entity.setCity(dto.getCity());
		entity.setComplement(dto.getComplement());
		entity.setNeighborhood(dto.getNeighborhood());
		entity.setTag(dto.getTag());
		entity.setClient(clientRepository.getOne(dto.getClientId()));
	}
}
