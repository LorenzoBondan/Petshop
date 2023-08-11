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

import com.projects.petshop.dto.AddressDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.entities.Client;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class AddressServiceTests {

	@InjectMocks
	private AddressService service;
	
	@Mock
	private AddressRepository repository;
	
	@Mock
	private ClientRepository clientRepository;
	
	private long existingId;
	private long nonExistingId;
	
	private Address address;
	
	private Client client;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		address = Factory.createAddress();
		client = Factory.createClient();
		
		Mockito.when(repository.getOne(existingId)).thenReturn(address);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(clientRepository.getOne(existingId)).thenReturn(client);
		Mockito.when(clientRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void updateShouldReturnAddressDtoWhenExistingId() {
	    AddressDTO addressDTO = Factory.createAddressDTO();
	    Mockito.when(repository.save(Mockito.any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
	    AddressDTO result = service.update(existingId, addressDTO);
	    Assertions.assertNotNull(result);
	    Assertions.assertEquals(addressDTO.getStreet(), result.getStreet());
	}
	
	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			AddressDTO addressDto = Factory.createAddressDTO();
			service.update(nonExistingId, addressDto);
		} );
	}
}
