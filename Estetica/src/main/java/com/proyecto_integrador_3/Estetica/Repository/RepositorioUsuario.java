package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String>  {

	//Metodo para buscar por DNI
	@Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
	List<Usuario> buscarPorDni(@Param("dni") String dni);
	
	//Metodo para buscar por Nombre
	@Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre")
	List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
	
	//Metodo para buscar por email
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	List<Usuario> buscarPorEmail(@Param("email") String email);

	//Metodo para buscar por Email usando los metodos de Optional
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmailOptional(@Param("email") String email);
    
  //Metodo para buscar por id usando los metodos de Optional
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    public Optional<Usuario> buscarPorIdOptional(@Param("id") String id);
	
    //Metodo para buscar contrasena, este lo uso para comparar las contraseñas al loguearse
    @Query("SELECT u FROM Usuario u WHERE u.contrasena = :contrasena")
    public Optional<Usuario> buscarContrasena(@Param("contrasena") String contrasena);
    
    //Metodo para buscar una lista de rol
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> findByRol(Rol rol);
    
    //Metodo para mostrar la lista de usuarios
	@Query("SELECT u FROM Usuario u")
	public List<Usuario> listarUsuarios();

	//Metodo para mostrar una lista de todos los campos de usuario y persona jusntos
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id ORDER BY u.rol ASC")
	List<Usuario> joinUsuarioPersona();
	
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.id = :id")
	List<Usuario> obtenerDatosPersonaUsuarioPorId(@Param("id") String id);
	
	//Metodo para buscar todos los campos de persona y usuario usando el dni
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.dni = :dni")
	public List<Usuario> obtenerDatosPersonaUsuarioPorDNI(@Param("dni") String dni);
	
	//Metodo para buscar todos los campos de persona y usuario usando el nombre
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.nombre = :nombre")
	public List<Usuario> obtenerDatosPersonaUsuarioPorNombre(@Param("nombre") String nombre);
	
	//Metodo para buscar todos los campos de persona y usuario usando el email
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.email = :email")
	public List<Usuario> obtenerDatosPersonaUsuarioPorEmail(@Param("email") String email);
	
	//Metodo para buscar todos los campos de persona y usuario usando el email
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.email = :email")
	public Optional<Usuario> obtenerDatosPersonaUsuarioPorEmailOptional(@Param("email") String email);
	
	//Metodo para buscar todos los campos de persona y usuario usando el dni
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.dni = :dni")
	public Optional<Usuario> obtenerDatosPersonaUsuarioPorDniOptional(@Param("dni") String dni);
		
	//Metodo para buscar todos los campos de persona y usuario usando el nombre
	@Query("SELECT p, u FROM Persona p INNER JOIN Usuario u ON p.id = u.id WHERE p.nombre = :nombre")
	public Optional<Usuario> obtenerDatosPersonaUsuarioPorNombreOptional(@Param("nombre") String nombre);
	
	  List<Usuario> findByRolAndEmail(Rol rol, String email);

}
