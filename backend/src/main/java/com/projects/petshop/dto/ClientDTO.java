package com.projects.petshop.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.projects.petshop.entities.Client;

public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Instant registerDate;
	private String imgUrl;
	private String userCpf;
	private AddressDTO address;
	private ContactDTO contact;
	
	private List<PetDTO> pets = new ArrayList<>(); 
	
	public ClientDTO() {}
	
	public ClientDTO(Client entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.imgUrl = entity.getImgUrl();
		this.registerDate = entity.getRegisterDate();
		this.userCpf = entity.getUser().getCpf();
		this.address = new AddressDTO(entity.getAddress());
		this.contact = new ContactDTO(entity.getContact());
		
		entity.getPets().forEach(pet -> this.getPets().add(new PetDTO(pet)));
	}

	public ClientDTO(Long id, String name, Instant registerDate, String imgUrl, String userCpf, AddressDTO address,
			ContactDTO contact) {
		super();
		this.id = id;
		this.name = name;
		this.registerDate = registerDate;
		this.imgUrl = imgUrl;
		this.userCpf = userCpf;
		this.address = address;
		this.contact = contact;
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

	public Instant getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Instant registerDate) {
		this.registerDate = registerDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUserCpf() {
		return userCpf;
	}

	public void setUserCpf(String userCpf) {
		this.userCpf = userCpf;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public ContactDTO getContact() {
		return contact;
	}

	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}

	public List<PetDTO> getPets() {
		return pets;
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
		ClientDTO other = (ClientDTO) obj;
		return Objects.equals(id, other.id);
	}
}
