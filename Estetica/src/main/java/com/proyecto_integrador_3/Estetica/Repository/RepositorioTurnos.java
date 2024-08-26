package com.proyecto_integrador_3.Estetica.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;

import jakarta.transaction.Transactional;

@Repository
public interface RepositorioTurnos extends JpaRepository<Turnos, String> {

	Optional<Turnos> findById(String idTurno);
	
	//Optional<Turnos> buscarTurnoPorDni(@Param("dni") String dni);
		
	List<Turnos> findByDni(String dniCliente);
	
	List<Turnos> findByEmail(String email);
	
	@Query("SELECT t FROM Turnos t WHERE t.cliente.id = :idCliente")
    List<Turnos> findTurnosByClienteId(@Param("idCliente") String idCliente);
	
	//Buscamos turnos por id del cliente y que esten activos en multa
	Optional <Turnos> findByClienteIdAndMulta(String idCliente, Boolean multa );
	
	//Buscamos turnos por emial que esten inactivos y que no tengan multas
	List<Turnos> findByEmailAndActivoFalseAndMultaFalse(String email);
	
	//Buscamos turnos inactivos y que tengan multas usanso  el id del cliente
	List<Turnos> findByClienteIdAndActivoFalseAndMultaTrue(String idCliente);
	
	//Buscamos turnos por el estado, si estan activos, si tienen multas por el email del cliente y los ordenamos por la fecha de creacion del turno
	List<Turnos> findByEstadoAndActivoAndMultaAndEmailOrderByFechaCreacion(EstadoDelTurno estado, Boolean activo, Boolean multa, String email);
	
	List<Turnos> findByEstadoAndActivoAndMultaAndProfesionalIdOrderByFechaCreacion(EstadoDelTurno estado, Boolean activo, Boolean multa, String idProfesional);
	
	List<Turnos> findByActivoAndEmail(Boolean activo, String email);
	
	List<Turnos> findByEmailOrderByFechaAsc(String email);
	
	List<Turnos> findByEmailAndActivoOrderByFechaAsc(String email, Boolean activo);
	
	List<Turnos> findByProfesionalAndFecha(Profesional profesional, LocalDate fecha);
	
	List<Turnos> findByProfesionalIdAndFecha(String idProfesional, LocalDate fecha);
	
	List<Turnos> findByProfesionalId(String idProfesional);
	
	List<Turnos> findByProfesionalIdAndClienteIdAndEstadoOrderByFechaCreacion(String idProfesiona, String idCliente, EstadoDelTurno estado);
}

