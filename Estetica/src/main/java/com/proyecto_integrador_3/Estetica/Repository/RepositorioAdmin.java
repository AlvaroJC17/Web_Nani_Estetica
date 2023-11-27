package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;

@Repository
public interface RepositorioAdmin extends JpaRepository<Admin, String> {
	
	/*
	//Metodo para buscar un admin por DNI
	@Query("SELECT p FROM Persona p WHERE p.dni = :dni")
    List<Admin> buscarPorDni(@Param("dni") String dni);
	
	//Metodo para buscar un admin por Nombre
	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
    List<Admin> buscarPorNombre(@Param("nombre") String nombre);
	
	//Metodos para listar a los admins
	@Query("SELECT u FROM Usuario u")
	List<Admin> listarAdmin();
	*/
}