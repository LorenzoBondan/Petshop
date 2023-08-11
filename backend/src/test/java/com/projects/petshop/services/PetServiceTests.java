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

import com.projects.petshop.dto.PetDTO;
import com.projects.petshop.entities.Breed;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Pet;
import com.projects.petshop.repositories.BreedRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.PetRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class PetServiceTests {

	@InjectMocks
	private PetService service;
	
	@Mock
	private PetRepository repository;
	
	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private BreedRepository breedRepository;
	
	private long existingId;
	private long nonExistingId;
	
	private PageImpl<Pet> page;
	private Pet pet;
	
	private Client client;
	private Breed breed;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		pet = Factory.createPet();
		client = Factory.createClient();
		breed = Factory.createBreed();
		
		page = new PageImpl<>(List.of(pet));
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(pet);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pet));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(pet);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(clientRepository.getOne(existingId)).thenReturn(client);
		Mockito.when(clientRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(breedRepository.getOne(existingId)).thenReturn(breed);
		Mockito.when(breedRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
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
		Page<PetDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnObjectWhenExistingId() {
		PetDTO result = service.findById(existingId);
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
	public void updateShouldReturnPetDtoWhenExistingId() {
		PetDTO petDTO = Factory.createPetDTO();
		PetDTO result = service.update(existingId, petDTO);
		Assertions.assertNotNull(result);
	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			PetDTO petDto = Factory.createPetDTO();
			service.update(nonExistingId, petDto);
		} );
	}

}
