package com.projects.petshop.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projects.petshop.dto.BreedDTO;
import com.projects.petshop.entities.Breed;
import com.projects.petshop.repositories.BreedRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class BreedServiceTests {

	@InjectMocks
	private BreedService service;
	
	@Mock
	private BreedRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private PageImpl<Breed> page;
	private Breed breed;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		breed = Factory.createBreed();
		page = new PageImpl<>(List.of(breed));
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(breed);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(breed));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(breed);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow( () -> {
			service.delete(existingId);
		} );
		Mockito.verify(repository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		} );
		Mockito.verify(repository).deleteById(nonExistingId);
	}
	
	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0,10);
		Page<BreedDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnObjectWhenExistingId() {
		BreedDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		} );
		Mockito.verify(repository).findById(nonExistingId);
	}
	
	@Test
	public void updateShouldReturnBreedDtoWhenExistingId() {
		BreedDTO breedDTO = Factory.createBreedDTO();
		BreedDTO result = service.update(existingId, breedDTO);
		Assertions.assertNotNull(result);
	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			BreedDTO breedDto = Factory.createBreedDTO();
			service.update(nonExistingId, breedDto);
		} );
	}
}
