package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;

@Repository
public interface RepositorioAdmin extends JpaRepository<Admin, String> {
	

}