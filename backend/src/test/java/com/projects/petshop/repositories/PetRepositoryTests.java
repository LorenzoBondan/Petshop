package com.projects.petshop.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.projects.petshop.entities.Pet;
import com.projects.petshop.tests.Factory;

@DataJpaTest
public class PetRepositoryTests {

	@Autowired
	private PetRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalPets;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalPets = 2L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Pet> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDontExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		} );
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Pet pet = Factory.createPet();
		pet.setId(null);
		pet = repository.save(pet);
		Assertions.assertNotNull(pet.getId());
		Assertions.assertEquals(countTotalPets + 1, pet.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Optional<Pet> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void getByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Pet result = repository.getById(existingId);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Optional<Pet> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void getByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Pet result = repository.getById(nonExistingId);
		Assertions.assertNull(result);
	}
}
