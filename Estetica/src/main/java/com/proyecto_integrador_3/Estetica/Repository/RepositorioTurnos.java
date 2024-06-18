package com.proyecto_integrador_3.Estetica.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;

@Repository
public interface RepositorioTurnos extends JpaRepository<Turnos, String> {

	
//	//Metodo para buscar por DNI
//		@Query("SELECT u FROM Turno u WHERE t.dni = :dni")
//		Optional<Turnos> buscarTurnoPorDni(@Param("dni") String dni);
		
	List<Turnos> findByDni(String dniCliente);
	
	List<Turnos> findByEmail(String email);
	
	@Query("SELECT t FROM Turnos t WHERE t.cliente.id = :idCliente")
    List<Turnos> findTurnosByClienteId(@Param("idCliente") String idCliente);
	
	List<Turnos> findByActivoAndEmail(Boolean activo, String email);
	
	List<Turnos> findByEmailOrderByFechaAsc(String email);
	
	List<Turnos> findByEmailAndActivoOrderByFechaAsc(String email, boolean activo);
	
	List<Turnos> findByProfesionalAndFecha(Profesional profesional, LocalDate fecha);
}

