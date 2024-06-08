package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;

@Repository
public interface RepositorioTurnos extends JpaRepository<Turnos, String> {

	
//	//Metodo para buscar por DNI
//		@Query("SELECT u FROM Turno u WHERE t.dni = :dni")
//		Optional<Turnos> buscarTurnoPorDni(@Param("dni") String dni);
		
	List<Turnos> findByDni(String dniCliente);
	
	List<Turnos> findByEmail(String email);
	
	Optional <Turnos> findById(String id);
	
	List<Turnos> findByActivoAndEmail(Boolean activo, String email);
	
	List<Turnos> findByEmailOrderByFechaAsc(String email);
	
	List<Turnos> findByEmailAndActivoOrderByFechaAsc(String email, boolean activo);
}
