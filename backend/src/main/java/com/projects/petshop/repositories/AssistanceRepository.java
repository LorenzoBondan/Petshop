package com.projects.petshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.petshop.entities.Assistance;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance,Long>{

}
