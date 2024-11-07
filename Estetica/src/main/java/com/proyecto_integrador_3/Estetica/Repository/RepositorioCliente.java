package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;


@Repository
public interface RepositorioCliente extends JpaRepository<Cliente, String> {

	//Metodo para buscar todos los campos de cliente y usuario usando el id
	@Query("SELECT c, u FROM Cliente c INNER JOIN Usuario u ON c.id = u.id WHERE c.id = :id")
	public Optional<Cliente> obtenerDatosClienteUsuarioPorIdOptional(@Param("id") String id);
	
	@Query("SELECT c FROM Cliente c WHERE c.id = :id")
	List<Cliente> buscarPorId(@Param("id") String idCliente);
	
	Optional <Cliente> findClienteById(String idCliente);
	
	Optional <Cliente> findByEmail(String email);
	
	Optional <Cliente> findById(String idCliente);
	
	
}
