package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;


@Repository
public interface RepositorioCliente extends JpaRepository<Persona, String> {

	/*@Query("SELECT p FROM persona p WHERE p.email = :email")
    public List<Persona> buscarPorEmail(@Param("email") String email);*/
	
	/* @Query("SELECT c FROM registro_usuario c WHERE c.dni = :dni")
	    public Cliente buscarPorDni(@Param("dni") String dni);
	 
	 @Query("SELECT c FROM registro_usuario c WHERE c.dni = :dni")
	 public Optional<Cliente> buscarPorDniOptional(@Param("dni") String dni);
	    
	 @Query("SELECT c FROM registro_usuario c WHERE c.id = :id")
	    public Cliente buscarPorId(@Param("id") String id);*/
    
	
}
