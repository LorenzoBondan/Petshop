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

import com.projects.petshop.dto.AssistanceDTO;
import com.projects.petshop.dto.PetDTO;
import com.projects.petshop.entities.Assistance;
import com.projects.petshop.entities.Pet;
import com.projects.petshop.repositories.AssistanceRepository;
import com.projects.petshop.repositories.BreedRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.PetRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class PetService {

	@Autowired
	private PetRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private BreedRepository breedRepository;
	
	@Autowired
	private AssistanceRepository assistanceRepository;

	@Transactional(readOnly = true)
	public Page<PetDTO> findAllPaged(Pageable pageable) {
		Page<Pet> list = repository.findAll(pageable);
		return list.map(x -> new PetDTO(x));
	}

	@Transactional(readOnly = true)
	public PetDTO findById(Long id) {
		Optional<Pet> obj = repository.findById(id);
		Pet entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new PetDTO(entity);
	}

	@Transactional
	public PetDTO insert(PetDTO dto) {
		Pet entity = new Pet();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new PetDTO(entity);
	}

	@Transactional
	public PetDTO update(Long id, PetDTO dto) {
		try {
			Pet entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new PetDTO(entity);
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
	
	private void copyDtoToEntity(PetDTO dto, Pet entity) {
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setClient(clientRepository.getOne(dto.getClientId()));
		entity.setBreed(breedRepository.getOne(dto.getBreed().getId()));
		
		for (AssistanceDTO assistanceDto : dto.getAssistances()) {
			Assistance assistance = assistanceRepository.getOne(assistanceDto.getId());
			entity.getAssistances().add(assistance);
		}
	}
}
