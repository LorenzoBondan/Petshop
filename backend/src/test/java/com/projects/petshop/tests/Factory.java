package com.projects.petshop.tests;

import java.time.Instant;

import com.projects.petshop.dto.AddressDTO;
import com.projects.petshop.dto.ClientDTO;
import com.projects.petshop.entities.Address;
import com.projects.petshop.entities.Client;
import com.projects.petshop.entities.Contact;
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
	
	public static Address createAddress() {
		Client client = new Client();
		Address address = new Address(1L, "Rua marques de souza", "Bento Gonçalves", "São Francisco", null, "tag", client);
		return address;
	}
	
	public static AddressDTO createAddressDTO() {
		Address address = createAddress();
		return new AddressDTO(address);
	}
}
