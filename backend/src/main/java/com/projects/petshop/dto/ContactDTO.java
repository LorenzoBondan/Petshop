package com.projects.petshop.dto;

import java.io.Serializable;
import java.util.Objects;

import com.projects.petshop.entities.Contact;

public class ContactDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String tag;
	private Boolean type; // true for email and false for phone number
	private String value;
	private Long clientId;
	
	public ContactDTO() {}

	public ContactDTO(Contact entity) {
		this.id = entity.getId();
		this.tag = entity.getTag();
		this.type = entity.getType();
		this.value = entity.getValue();
		if(entity.getClient() != null) {
			this.clientId = entity.getClient().getId();
		}
		
	}
	
	public ContactDTO(Long id, String tag, Boolean type, String value, Long clientId) {
		super();
		this.id = id;
		this.tag = tag;
		this.type = type;
		this.value = value;
		this.clientId = clientId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
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
		ContactDTO other = (ContactDTO) obj;
		return Objects.equals(id, other.id);
	}
}
