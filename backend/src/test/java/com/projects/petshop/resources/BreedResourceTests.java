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
import com.projects.petshop.dto.BreedDTO;
import com.projects.petshop.services.BreedService;
import com.projects.petshop.services.exceptions.ResourceNotFoundException;
import com.projects.petshop.tests.Factory;
import com.projects.petshop.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class BreedResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BreedService service;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private BreedDTO breedDTO;
	private PageImpl<BreedDTO> page;
	
	private Long existingId;
	private Long nonExistingId;
	
	private String username;
	private String password;
	private String usernameClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L; 
		nonExistingId = 2L;
		
		username = "000.123.456-78"; // Maria, admin
		password = "123456";
		
		usernameClient = "123.456.789-00";
		
		breedDTO = Factory.createBreedDTO();
		page = new PageImpl<>(List.of(breedDTO));
		
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(service.findById(existingId)).thenReturn(breedDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(breedDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(breedDTO);
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(get("/breeds")
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	public void findAllShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, usernameClient, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(get("/breeds")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
	
	@Test
	public void findAllShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		mockMvc.perform(get("/breeds"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void findByIdShouldReturnBreedWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(get("/breeds/{id}", existingId)
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(get("/breeds/{id}", nonExistingId)
			.header("Authorization", "Bearer " + accessToken)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		mockMvc.perform(get("/breeds/{id}", nonExistingId))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, usernameClient, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(get("/breeds/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
	
	@Test
	public void updateShouldReturnBreedWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(put("/breeds/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()); 
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(put("/breeds/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnUnauthorizedWhenNotLogged() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(put("/breeds/{id}", existingId)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void updateShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, usernameClient, password);
		String jsonBody = objectMapper.writeValueAsString(breedDTO);
		
		mockMvc.perform(put("/breeds/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden()); 
	}
}
