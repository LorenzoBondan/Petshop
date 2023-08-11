package com.projects.petshop.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.projects.petshop.entities.Client;
import com.projects.petshop.tests.Factory;

@DataJpaTest
public class ClientRepositoryTests {

	@Autowired
	private ClientRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalClients;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalClients = 2L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Client> result = repository.findById(existingId);
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
		Client client = Factory.createClient();
		client.setId(null);
		client = repository.save(client);
		Assertions.assertNotNull(client.getId());
		Assertions.assertEquals(countTotalClients + 1, client.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Optional<Client> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Optional<Client> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
}
