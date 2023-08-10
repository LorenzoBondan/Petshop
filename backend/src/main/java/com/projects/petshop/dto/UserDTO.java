package com.projects.petshop.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.projects.petshop.entities.User;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Campo obrigatório")
	private String cpf;
	@NotBlank(message = "Campo obrigatório")
	private String name;
	
	private List<RoleDTO> roles = new ArrayList<>();

	public UserDTO() {}

	public UserDTO(String name, String cpf) {
		super();
		this.name = name;
		this.cpf = cpf;
	}
	
	public UserDTO(User entity) {
		this.cpf = entity.getCpf();
		this.name = entity.getName();
		
		entity.getRoles().forEach(rol -> this.roles.add(new RoleDTO(rol)));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(cpf, other.cpf);
	}
}
