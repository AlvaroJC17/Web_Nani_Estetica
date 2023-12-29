package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;


@Repository
public interface RepositorioCliente extends JpaRepository<Cliente, String> {

	//Metodo para buscar todos los campos de cliente y usuario usando el id
	@Query("SELECT c, u FROM Cliente c INNER JOIN Usuario u ON c.id = u.id WHERE c.id = :id")
	public Optional<Cliente> obtenerDatosClienteUsuarioPorIdOptional(@Param("id") String id);
	
}
