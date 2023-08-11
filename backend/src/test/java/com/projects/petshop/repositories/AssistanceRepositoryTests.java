package com.projects.petshop.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.projects.petshop.entities.Assistance;
import com.projects.petshop.tests.Factory;

@DataJpaTest
public class AssistanceRepositoryTests {

	@Autowired
	private AssistanceRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalAssistances;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalAssistances = 2L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Assistance> result = repository.findById(existingId);
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
		Assistance assistance = Factory.createAssistance();
		assistance.setId(null);
		assistance = repository.save(assistance);
		Assertions.assertNotNull(assistance.getId());
		Assertions.assertEquals(countTotalAssistances + 1, assistance.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Optional<Assistance> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Optional<Assistance> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
}
