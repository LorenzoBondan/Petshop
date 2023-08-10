package com.projects.petshop.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.projects.petshop.entities.Pet;

public class PetDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Instant birthDate;
	private String imgUrl;
	private Long clientId;
	
	private BreedDTO breed;
	
	public PetDTO() {}
	
	public PetDTO(Pet entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.birthDate = entity.getBirthDate();
		this.imgUrl = entity.getImgUrl();
		this.clientId = entity.getClient().getId();
		this.breed = new BreedDTO(entity.getBreed());
	}

	public PetDTO(Long id, String name, Instant birthDate, String imgUrl, Long clientId, BreedDTO breed) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.imgUrl = imgUrl;
		this.clientId = clientId;
		this.breed = breed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Instant birthDate) {
		this.birthDate = birthDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public BreedDTO getBreed() {
		return breed;
	}

	public void setBreed(BreedDTO breed) {
		this.breed = breed;
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
		PetDTO other = (PetDTO) obj;
		return Objects.equals(id, other.id);
	}
}
