package com.projects.petshop.services;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ContactServiceTests {

	@InjectMocks
	private ContactService service;
	
	@Mock
	private ContactRepository repository;
	
	@Mock
	private ClientRepository clientRepository;
	
	private long existingId;
	private long nonExistingId;
	
	private Contact contact;
	
	private Client client;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		contact = Factory.createContact();
		client = Factory.createClient();
		
		Mockito.when(repository.getOne(existingId)).thenReturn(contact);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(clientRepository.getOne(existingId)).thenReturn(client);
		Mockito.when(clientRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void updateShouldReturnContactDtoWhenExistingId() {
	    ContactDTO contactDTO = Factory.createContactDTO();
	    Mockito.when(repository.save(Mockito.any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));
	    ContactDTO result = service.update(existingId, contactDTO);
	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(contactDTO.getId(), result.getId());
	}
	
	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ContactDTO contactDto = Factory.createContactDTO();
			service.update(nonExistingId, contactDto);
		} );
	}
}
