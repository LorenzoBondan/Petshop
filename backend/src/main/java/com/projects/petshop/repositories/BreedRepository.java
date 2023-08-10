package com.projects.petshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.petshop.entities.Breed;

@Repository
public interface BreedRepository extends JpaRepository<Breed,Long>{

}
