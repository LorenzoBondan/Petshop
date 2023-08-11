package com.projects.petshop.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.projects.petshop.entities.Address;
import com.projects.petshop.tests.Factory;

@DataJpaTest
public class AddressRepositoryTests {

	@Autowired
	private AddressRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalAddresses;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalAddresses = 2L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Address> result = repository.findById(existingId);
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
		Address address = Factory.createAddress();
		address.setId(null);
		address = repository.save(address);
		Assertions.assertNotNull(address.getId());
		Assertions.assertEquals(countTotalAddresses + 1, address.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Optional<Address> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Optional<Address> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
}
