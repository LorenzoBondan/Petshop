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

import com.projects.petshop.dto.ClientDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.entities.User;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.repositories.UserRepository;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

	@InjectMocks
	private ClientService service;
	
	@Mock
	private ClientRepository repository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private ContactRepository contactRepository;
	
	private long existingId;
	private long nonExistingId;
	private String existingCpf;
	private String nonExistingCpf;
	
	private PageImpl<Client> page;
	private Client client;
	
	private User user;
	private Address address;
	private Contact contact;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		existingCpf = "123.654.789-10";
		nonExistingCpf = "999.999.999-99";
		
		client = Factory.createClient();
		user = Factory.createUser();
		address = Factory.createAddress();
		contact = Factory.createContact();
		
		page = new PageImpl<>(List.of(client));
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(client);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(client));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getOne(existingId)).thenReturn(client);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(userRepository.findByCpf(existingCpf)).thenReturn(user);
		Mockito.when(userRepository.findByCpf(nonExistingCpf)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(addressRepository.getOne(existingId)).thenReturn(address);
		Mockito.when(addressRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(contactRepository.getOne(existingId)).thenReturn(contact);
		Mockito.when(contactRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
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
		Page<ClientDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnObjectWhenExistingId() {
		ClientDTO result = service.findById(existingId);
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
	public void updateShouldReturnClientDtoWhenExistingId() {
		ClientDTO clientDTO = Factory.createClientDTO();
		ClientDTO result = service.update(existingId, clientDTO);
		Assertions.assertNotNull(result);
	}

	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ClientDTO clientDto = Factory.createClientDTO();
			service.update(nonExistingId, clientDto);
		} );
	}

}
