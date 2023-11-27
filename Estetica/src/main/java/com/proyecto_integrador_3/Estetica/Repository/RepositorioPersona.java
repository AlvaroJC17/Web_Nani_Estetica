package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;

@Repository
public interface RepositorioPersona extends JpaRepository<Persona, String>{
	
	//Metodo para mostrar una lista de la tabla de personas
	@Query("SELECT p FROM Persona p")
	public List<Persona> listarPersonas();
	
	//Metodo para buscar a una persona por dni
	@Query("SELECT p FROM Persona p WHERE p.dni = :dni")
	List<Persona> buscarPorDni(@Param("dni") String dni);
	
	//Metodo para buscar a una persona por Nombre
	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
	List<Persona> buscarPorNombre(@Param("nombre") String nombre);
	
	//Metodo para buscar a una persona por email
	@Query("SELECT p FROM Persona p WHERE p.email = :email")
	List<Persona> buscarPorEmail(@Param("email") String email);

	//Metodo para buscar a una persona por email usando los metodos de optional
    @Query("SELECT p FROM Persona p WHERE p.email = :email")
    public Optional<Persona> buscarPorEmailOptional(@Param("email") String email);
    

}
