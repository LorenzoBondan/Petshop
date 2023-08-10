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

import com.projects.petshop.dto.BreedDTO;
import com.projects.petshop.entities.Breed;
import com.projects.petshop.entities.Pet;
import com.projects.petshop.repositories.BreedRepository;
import com.projects.petshop.repositories.PetRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class BreedService {

	@Autowired
	private BreedRepository repository;
	
	@Autowired
	private PetRepository petRepository;

	@Transactional(readOnly = true)
	public Page<BreedDTO> findAllPaged(Pageable pageable) {
		Page<Breed> list = repository.findAll(pageable);
		return list.map(x -> new BreedDTO(x));
	}

	@Transactional(readOnly = true)
	public BreedDTO findById(Long id) {
		Optional<Breed> obj = repository.findById(id);
		Breed entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new BreedDTO(entity);
	}

	@Transactional
	public BreedDTO insert(BreedDTO dto) {
		Breed entity = new Breed();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new BreedDTO(entity);
	}

	@Transactional
	public BreedDTO update(Long id, BreedDTO dto) {
		try {
			Breed entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new BreedDTO(entity);
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
	
	private void copyDtoToEntity(BreedDTO dto, Breed entity) {
		entity.setDescription(dto.getDescription());
		
		for (Long petId : dto.getPetsId()) {
			Pet pet = petRepository.getOne(petId);
			entity.getPets().add(pet);
		}
	}
}
