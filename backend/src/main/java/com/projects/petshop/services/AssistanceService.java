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
import com.projects.petshop.entities.Assistance;
import com.projects.petshop.repositories.AssistanceRepository;
import com.projects.petshop.repositories.PetRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class AssistanceService {

	@Autowired
	private AssistanceRepository repository;
	
	@Autowired
	private PetRepository petRepository;

	@Transactional(readOnly = true)
	public Page<AssistanceDTO> findAllPaged(Pageable pageable) {
		Page<Assistance> list = repository.findAll(pageable);
		return list.map(x -> new AssistanceDTO(x));
	}

	@Transactional(readOnly = true)
	public AssistanceDTO findById(Long id) {
		Optional<Assistance> obj = repository.findById(id);
		Assistance entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new AssistanceDTO(entity);
	}

	@Transactional
	public AssistanceDTO insert(AssistanceDTO dto) {
		Assistance entity = new Assistance();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new AssistanceDTO(entity);
	}

	@Transactional
	public AssistanceDTO update(Long id, AssistanceDTO dto) {
		try {
			Assistance entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new AssistanceDTO(entity);
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
	
	private void copyDtoToEntity(AssistanceDTO dto, Assistance entity) {
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setValue(dto.getValue());
		entity.setPet(petRepository.getOne(dto.getPetId()));
	}
}
