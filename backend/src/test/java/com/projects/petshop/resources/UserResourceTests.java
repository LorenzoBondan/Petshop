package com.projects.petshop.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.petshop.dto.UserDTO;
import com.projects.petshop.services.UserService;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
public class UserResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService service;
	
	private UserDTO userDTO;
	private PageImpl<UserDTO> page;
	
	private String existingCpf;
	private String nonExistingCpf;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingCpf = "000.123.456-78"; 
		nonExistingCpf = "111.111.111-11";
		
		userDTO = Factory.createUserDTO();
		page = new PageImpl<>(List.of(userDTO));
		
		Mockito.when(service.findAllPaged(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(service.findByCpf(existingCpf)).thenReturn(userDTO);
		Mockito.when(service.findByCpf(nonExistingCpf)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existingCpf), ArgumentMatchers.any())).thenReturn(userDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingCpf), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	
		doNothing().when(service).delete(existingCpf);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingCpf);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(userDTO);
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTM2NDYsInVzZXJfbmFtZSI6IjAwMC4xMjMuNDU2LTc4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQ0xJRU5UIl0sImp0aSI6IjVlMzliYjJmLWY4NDgtNDkyZC1hNmVkLWQ2ZmY1ZmU0ZTgzOSIsImNsaWVudF9pZCI6Im15Y2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.9ErtbIHVmgVaxLbv7sIYdy6Q6P30CAafGA39jOn9fsA";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(get("/users")
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	public void findAllShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTk4ODksInVzZXJfbmFtZSI6IjEyMy40NTYuNzg5LTAwIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DTElFTlQiXSwianRpIjoiNmNiNjE1YWItZmM2NC00ODFkLTkyZmItYzQ3NDQ1MGUxODMxIiwiY2xpZW50X2lkIjoibXljbGllbnRpZCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.HRJM8V_BcR9-lNxQBmAp-EyjnnvEKTX2OTIuOdjq5zU";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(get("/users")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
	
	@Test
	public void findAllShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		mockMvc.perform(get("/users"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void findByIdShouldReturnUserWhenIdExists() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTM2NDYsInVzZXJfbmFtZSI6IjAwMC4xMjMuNDU2LTc4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQ0xJRU5UIl0sImp0aSI6IjVlMzliYjJmLWY4NDgtNDkyZC1hNmVkLWQ2ZmY1ZmU0ZTgzOSIsImNsaWVudF9pZCI6Im15Y2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.9ErtbIHVmgVaxLbv7sIYdy6Q6P30CAafGA39jOn9fsA";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(get("/users/{cpf}", existingCpf)
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cpf").exists()); 
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTM2NDYsInVzZXJfbmFtZSI6IjAwMC4xMjMuNDU2LTc4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQ0xJRU5UIl0sImp0aSI6IjVlMzliYjJmLWY4NDgtNDkyZC1hNmVkLWQ2ZmY1ZmU0ZTgzOSIsImNsaWVudF9pZCI6Im15Y2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.9ErtbIHVmgVaxLbv7sIYdy6Q6P30CAafGA39jOn9fsA";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(get("/users/{cpf}", nonExistingCpf)
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		mockMvc.perform(get("/users/{cpf}", nonExistingCpf))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTk4ODksInVzZXJfbmFtZSI6IjEyMy40NTYuNzg5LTAwIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DTElFTlQiXSwianRpIjoiNmNiNjE1YWItZmM2NC00ODFkLTkyZmItYzQ3NDQ1MGUxODMxIiwiY2xpZW50X2lkIjoibXljbGllbnRpZCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.HRJM8V_BcR9-lNxQBmAp-EyjnnvEKTX2OTIuOdjq5zU";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(get("/users/{cpf}", existingCpf)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
	
	@Test
	public void updateShouldReturnUserWhenIdExists() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTM2NDYsInVzZXJfbmFtZSI6IjAwMC4xMjMuNDU2LTc4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQ0xJRU5UIl0sImp0aSI6IjVlMzliYjJmLWY4NDgtNDkyZC1hNmVkLWQ2ZmY1ZmU0ZTgzOSIsImNsaWVudF9pZCI6Im15Y2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.9ErtbIHVmgVaxLbv7sIYdy6Q6P30CAafGA39jOn9fsA";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(put("/users/{cpf}", existingCpf)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cpf").exists()); 
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTM2NDYsInVzZXJfbmFtZSI6IjAwMC4xMjMuNDU2LTc4IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfQ0xJRU5UIl0sImp0aSI6IjVlMzliYjJmLWY4NDgtNDkyZC1hNmVkLWQ2ZmY1ZmU0ZTgzOSIsImNsaWVudF9pZCI6Im15Y2xpZW50aWQiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.9ErtbIHVmgVaxLbv7sIYdy6Q6P30CAafGA39jOn9fsA";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(put("/users/{cpf}", nonExistingCpf)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(put("/users/{cpf}", existingCpf)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void updateShouldReturnForbbidenWhenNotAdmin() throws Exception {
		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE4OTk4ODksInVzZXJfbmFtZSI6IjEyMy40NTYuNzg5LTAwIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DTElFTlQiXSwianRpIjoiNmNiNjE1YWItZmM2NC00ODFkLTkyZmItYzQ3NDQ1MGUxODMxIiwiY2xpZW50X2lkIjoibXljbGllbnRpZCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.HRJM8V_BcR9-lNxQBmAp-EyjnnvEKTX2OTIuOdjq5zU";
		String jsonBody = objectMapper.writeValueAsString(userDTO);
		
		mockMvc.perform(put("/users/{cpf}", existingCpf)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
}
