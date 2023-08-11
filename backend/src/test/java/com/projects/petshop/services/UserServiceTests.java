package com.projects.petshop.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projects.petshop.dto.UserDTO;
import com.projects.petshop.dto.UserInsertDTO;
import com.projects.petshop.dto.UserUpdateDTO;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.User;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.repositories.RoleRepository;
import com.projects.petshop.repositories.UserRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private ContactRepository contactRepository;
	
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
	
	private long existingId;
	private long nonExistingId;
	private String existingCpf;
	private String nonExistingCpf;
	
	private PageImpl<User> page;
	private User user;
	
	private Client client;
	
	@BeforeEach
	void setUp() throws Exception{ 
		
		existingId = 1L;
		nonExistingId = 1000L;
		
		existingCpf = "123.654.789-10";
		nonExistingCpf = "989.919.929-98";
		
		user = Factory.createUser();
		client = Factory.createClient();
		
		page = new PageImpl<>(List.of(user));
		
		Mockito.doNothing().when(repository).deleteByCpf(existingCpf);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteByCpf(nonExistingCpf);

		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);
		
		Mockito.when(repository.findByCpf(existingCpf)).thenReturn(user);
		Mockito.when(repository.findByCpf(nonExistingCpf)).thenReturn(null);
		
		Mockito.when(repository.findByCpf(existingCpf)).thenReturn(user);
		Mockito.when(repository.findByCpf(nonExistingCpf)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(clientRepository.getOne(existingId)).thenReturn(client);
		Mockito.when(clientRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenCpfDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingCpf);
        });
        Mockito.verify(repository).deleteByCpf(nonExistingCpf);
    }

    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO> result = service.findAllPaged("", pageable);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByCpfShouldReturnObjectWhenExistingCpf() {
        UserDTO result = service.findByCpf(existingCpf);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findByCpf(existingCpf);
    }

    @Test
    public void findByCpfShouldThrowResourceNotFoundExceptionWhenNonExistingCpf() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.findByCpf(nonExistingCpf);
        });
        Mockito.verify(repository).findByCpf(nonExistingCpf);
    }

    @Test
    public void insertShouldReturnUserDTO() {
        UserInsertDTO insertDTO = Factory.createUserInsertDTO();
        UserDTO result = service.insert(insertDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldReturnUserDTOWhenExistingCpf() {
        UserDTO userDto = Factory.createUserDTO(); 
        UserUpdateDTO updateDTO = Factory.createUserUpdateDTO(userDto); 
        UserDTO result = service.update(existingCpf, updateDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNonExistingCpf() {
        UserDTO userDto = Factory.createUserDTO(); 
        UserUpdateDTO updateDTO = Factory.createUserUpdateDTO(userDto); 
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingCpf, updateDTO);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependencyViolation() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteByCpf(existingCpf);
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(existingCpf);
        });
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenCpfExists() {
        String existingCpf = "123.456.789-10";
        User user = new User();
        user.setCpf(existingCpf);
        Mockito.when(repository.findByCpf(existingCpf)).thenReturn(user);
        UserDetails userDetails = service.loadUserByUsername(existingCpf);
        Assertions.assertEquals(existingCpf, userDetails.getUsername());
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenCpfDoesNotExist() {
        String nonExistingCpf = "12333.456.789-10";
        Mockito.when(repository.findByCpf(nonExistingCpf)).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingCpf);
        });
    }
}
