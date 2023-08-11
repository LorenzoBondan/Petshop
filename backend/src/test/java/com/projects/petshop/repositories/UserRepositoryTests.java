package com.projects.petshop.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.projects.petshop.entities.User;

@DataJpaTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository repository;
	
	private String existingCpf;
	private String nonExistingCpf;
	
	@BeforeEach
	void setUp() throws Exception{
		existingCpf = "123.456.789-00";
		nonExistingCpf = "999.999.999-99";
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenCpfExists() {
		repository.deleteByCpf(existingCpf);
		User result = repository.findByCpf(existingCpf);
		Assertions.assertNull(result);
	}
	
	@Test
	public void findByCpfShouldReturnNotEmptyObjectWhenCpfExists() {
		User result = repository.findByCpf(existingCpf);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByCpfShouldReturnEmptyObjectWhenCpfNotExists() {
		User result = repository.findByCpf(nonExistingCpf);
		Assertions.assertNull(result);
	}
}
