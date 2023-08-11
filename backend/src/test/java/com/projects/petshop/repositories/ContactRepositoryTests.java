package com.projects.petshop.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.projects.petshop.entities.Contact;
import com.projects.petshop.tests.Factory;

@DataJpaTest
public class ContactRepositoryTests {

	@Autowired
	private ContactRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalContacts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalContacts = 2L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Contact> result = repository.findById(existingId);
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
		Contact contact = Factory.createContact();
		contact.setId(null);
		contact = repository.save(contact);
		Assertions.assertNotNull(contact.getId());
		Assertions.assertEquals(countTotalContacts + 1, contact.getId());
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		Optional<Contact> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		Optional<Contact> result = repository.findById(nonExistingId);
		Assertions.assertFalse(result.isPresent());
	}
}
