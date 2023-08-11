package com.projects.petshop.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.petshop.entities.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long>{

	@EntityGraph(attributePaths = {"client", "breed", "assistances"})
    Pet getById(Long id);
}
