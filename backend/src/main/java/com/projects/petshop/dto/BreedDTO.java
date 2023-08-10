package com.projects.petshop.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.projects.petshop.entities.Breed;

public class BreedDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String description;
	
	private List<Long> petsId = new ArrayList<>();
	
	public BreedDTO() {}
	
	public BreedDTO(Breed entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		
		entity.getPets().forEach(pet -> this.petsId.add(pet.getId()));
	}

	public BreedDTO(Long id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getPetsId() {
		return petsId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BreedDTO other = (BreedDTO) obj;
		return Objects.equals(id, other.id);
	}
}
