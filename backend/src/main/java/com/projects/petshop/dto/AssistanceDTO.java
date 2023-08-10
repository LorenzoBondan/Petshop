package com.projects.petshop.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.projects.petshop.entities.Assistance;

public class AssistanceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String description;
	private Double value;
	private Instant date;
	private Long petId;
	
	public AssistanceDTO() {}
	
	public AssistanceDTO(Assistance entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.value = entity.getValue();
		this.date = entity.getDate();
		this.petId = entity.getPet().getId();
	}

	public AssistanceDTO(Long id, String description, Double value, Instant date, Long petId) {
		super();
		this.id = id;
		this.description = description;
		this.value = value;
		this.date = date;
		this.petId = petId;
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Long getPetId() {
		return petId;
	}

	public void setPetId(Long petId) {
		this.petId = petId;
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
		AssistanceDTO other = (AssistanceDTO) obj;
		return Objects.equals(id, other.id);
	}
}
