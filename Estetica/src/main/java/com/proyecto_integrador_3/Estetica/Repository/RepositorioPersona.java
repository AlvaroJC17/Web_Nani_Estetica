package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;

@Repository
public interface RepositorioPersona extends JpaRepository<Persona, String>{
	
	@Query("SELECT p FROM Persona p WHERE p.id = :id")
	public Optional <Persona> buscarPersonaPorId(@Param("id") String id);
	
	//Metodo para mostrar una lista de la tabla de personas
	@Query("SELECT p FROM Persona p")
	public List<Persona> listarPersonas();
	
	//Metodo para buscar a una persona por dni
	@Query("SELECT p FROM Persona p WHERE p.dni = :dni")
	List<Persona> buscarPorDni(@Param("dni") String dni);
	
	@Query("SELECT p FROM Persona p WHERE p.dni = :dni")
	Optional<Persona> buscarPorDniOptional(@Param("dni") String dni);
	
	//Metodo para buscar a una persona por Nombre
	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
	List<Persona> buscarPorNombre(@Param("nombre") String nombre);
	
	//Metodo para buscar a una persona por email
	@Query("SELECT p FROM Persona p WHERE p.email = :email")
	List<Persona> buscarPorEmail(@Param("email") String email);

	//Metodo para buscar a una persona por email usando los metodos de optional
    @Query("SELECT p FROM Persona p WHERE p.email = :email")
    public Optional<Persona> buscarPorEmailOptional(@Param("email") String email);
    
    @Query("SELECT u, p FROM Usuario u INNER JOIN Persona p ON u.id = p.id WHERE u.rol = :rol")
	List<Persona> buscarNombreApellidoPorRol(@Param("rol") Rol rol);
    
    //Sirve para buscar persona y usuario medinate numero de DNi y rol
    @Query("SELECT p FROM Usuario u INNER JOIN Persona p ON u.id = p.id WHERE u.rol = :rol AND p.dni = :dni")
    List<Persona> buscarPacientePorRolYDni(@Param("rol") Rol rol, @Param("dni") String dni);
    
  //Sirve para buscar persona y usuario medinate nombre y rol
    @Query("SELECT p FROM Usuario u INNER JOIN Persona p ON u.id = p.id WHERE u.rol = :rol AND p.nombre = :nombre")
    List<Persona> buscarPacientePorRolYNombre(@Param("rol") Rol rol, @Param("nombre") String nombre);
    
  //Sirve para buscar persona y usuario medinate email y rol
    @Query("SELECT p FROM Usuario u INNER JOIN Persona p ON u.id = p.id WHERE u.rol = :rol AND u.email = :email")
    List<Persona> buscarPacientePorRolYEmail(@Param("rol") Rol rol, @Param("email") String email);

    @Query("SELECT p, pr FROM Persona p JOIN Profesional pr ON p.id = pr.id WHERE pr.provincia IN :provincias AND p.rol = :rol")
    List<Persona> buscarNombreApellidoPorRolYProvincia(@Param("rol") Rol rol, @Param("provincias") Provincias provincias);
    
    @Query("SELECT p, pr FROM Persona p JOIN Profesional pr ON p.id = pr.id WHERE pr.provincia IN :provincias AND p.rol = :rol AND p.activo = :activo")
    List<Persona> buscarNombreApellidoPorRolProvinciaYActivo(@Param("rol") Rol rol, @Param("provincias") Provincias provincias, @Param("activo") boolean activo);


}
