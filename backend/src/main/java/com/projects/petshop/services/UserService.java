package com.projects.petshop.services;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projects.petshop.dto.RoleDTO;
import com.projects.petshop.dto.UserDTO;
import com.projects.petshop.dto.UserInsertDTO;
import com.projects.petshop.dto.UserUpdateDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.entities.Role;
import com.projects.petshop.entities.User;
import com.projects.petshop.repositories.AddressRepository;
import com.projects.petshop.repositories.ClientRepository;
import com.projects.petshop.repositories.ContactRepository;
import com.projects.petshop.repositories.RoleRepository;
import com.projects.petshop.repositories.UserRepository;
import com.projects.petshop.services.exceptions.DataBaseException;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class); 

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(String name, Pageable pageable) {
		Page<User> list = repository.find(name, pageable);
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findByCpf(String cpf) {
		Optional<User> obj = Optional.ofNullable(repository.findByCpf(cpf));
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		entity.setCpf(dto.getCpf());

	    copyDtoToEntity(dto, entity);
	    entity.setPassword(passwordEncoder.encode(dto.getPassword()));

	    Client client = new Client();
	    Address address = new Address();
	    Contact contact = new Contact();

	    client.setName(dto.getName());
	    client.setRegisterDate(Instant.now());
	    client = clientRepository.save(client);

	    address.setClient(client);
	    address = addressRepository.save(address);

	    contact.setClient(client);
	    contact = contactRepository.save(contact);
	        
	    entity.setClient(client);
	    
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(String cpf, UserUpdateDTO dto) {
		try {
			User entity = repository.findByCpf(cpf);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Cpf not found " + cpf);
		}
	}
	
	public void delete(String cpf) {
		try {
			repository.deleteByCpf(cpf);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Cpf not found " + cpf);
		}

		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setCpf(dto.getCpf());
		entity.setName(dto.getName());
		
		if(dto.getClientId() != null) {
			entity.setClient(clientRepository.getOne(dto.getClientId()));
		}
		
		for (RoleDTO rolDto : dto.getRoles()) {
			Role role = roleRepository.getOne(rolDto.getId());
			entity.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByCpf(username);

		if (user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Cpf not found");
		}
		logger.info("User found: " + username);
		return user;
	}
}
