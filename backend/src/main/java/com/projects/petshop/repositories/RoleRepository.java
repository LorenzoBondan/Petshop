package com.projects.petshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projects.petshop.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{

}
