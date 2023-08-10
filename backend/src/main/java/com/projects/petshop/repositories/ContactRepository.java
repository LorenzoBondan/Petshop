package com.projects.petshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.petshop.entities.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long>{

}
