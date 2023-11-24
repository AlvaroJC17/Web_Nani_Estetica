package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;

public interface RepositorioUsuario extends JpaRepository<Usuario, String>  {

	//Metodo para buscar por DNI
	@Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
	List<Usuario> buscarPorDni(@Param("dni") String dni);
	
	//Metodo para buscar por Nombre
	@Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre")
	List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	List<Usuario> buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmailOptional(@Param("email") String email);
	
	@Query("SELECT u FROM Usuario u")
	public List<Usuario> listarUsuarios();

}
