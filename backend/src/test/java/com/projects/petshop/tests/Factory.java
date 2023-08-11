package com.projects.petshop.tests;

import java.time.Instant;

import com.projects.petshop.dto.AddressDTO;
import com.projects.petshop.dto.AssistanceDTO;
import com.projects.petshop.dto.BreedDTO;
import com.projects.petshop.dto.ClientDTO;
import com.projects.petshop.dto.ContactDTO;
import com.projects.petshop.dto.PetDTO;
import com.projects.petshop.dto.UserDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.entities.Assistance;
import com.projects.petshop.entities.Breed;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Contact;
import com.projects.petshop.entities.Pet;
import com.projects.petshop.entities.User;

public class Factory {

	public static Client createClient() {
		User user = new User("123.654.789-10", "Julia", "123456");
		Address address = new Address();
		Contact contact = new Contact();
		Client client = new Client(1L, "Julia", Instant.parse("2020-07-14T10:00:00Z"), "img", user, address, contact);
		address.setClient(client);
		contact.setClient(client);
		return client;
	} 
	
	public static ClientDTO createClientDTO() {
		Client client = createClient();
		return new ClientDTO(client);
	}
	
	public static User createUser() {
		User user = new User("123.456.789-10", "Bob", "123456", new Client());
		return user;
	}
	
	public static UserDTO createUserDTO() {
		User user = createUser();
		return new UserDTO(user);
	}
	
	public static Address createAddress() {
		User user = new User("123.654.789-10", "Julia", "123456");
		Contact contact = new Contact();
		Address address = new Address(1L, "Rua marques de souza", "Bento Gonçalves", "São Francisco", null, "tag", new Client());
		Client client = new Client(1L, "Julia", Instant.parse("2020-07-14T10:00:00Z"), "img", user, address, contact);
		address.setClient(client);
		return address;
	}
	
	public static AddressDTO createAddressDTO() {
		Address address = createAddress();
		return new AddressDTO(address);
	}
	
	public static Assistance createAssistance() {
		Pet pet = new Pet(1L, "Bob", Instant.parse("2020-07-14T10:00:00Z"), "img", new Client(), new Breed());
		Assistance assistance = new Assistance(1L, "First assistance", 100.0, Instant.parse("2020-07-14T10:00:00Z"), pet);
		return assistance;
	}
	
	public static AssistanceDTO createAssistanceDTO() {
		Assistance assistance = createAssistance();
		return new AssistanceDTO(assistance);
	}
	
	public static Breed createBreed() {
		Breed breed = new Breed(1L, "Bulldog");
		return breed;
	}
	
	public static BreedDTO createBreedDTO() {
		Breed breed = createBreed();
		return new BreedDTO(breed);
	}
	
	public static Contact createContact() {
		Contact contact = new Contact(1L, "Tag", true, "contact@email.com", new Client());
		return contact;
	}
	
	public static ContactDTO createContactDTO() {
		Contact contact = createContact();
		return new ContactDTO(contact);
	}
	
	public static Pet createPet() {
		Pet pet = new Pet(1L, "Bob", Instant.parse("2020-07-14T10:00:00Z"), "img", new Client(), new Breed());
		return pet;
	}
	
	public static PetDTO createPetDTO() {
		Pet pet = createPet();
		return new PetDTO(pet);
	}
}
