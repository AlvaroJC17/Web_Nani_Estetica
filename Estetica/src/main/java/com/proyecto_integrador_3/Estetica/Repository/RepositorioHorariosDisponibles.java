package com.proyecto_integrador_3.Estetica.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;

public interface RepositorioHorariosDisponibles extends JpaRepository<HorariosDisponibles, String> {

		List<String> findByProfesionalAndFecha(Profesional profesional, String fecha);
		
		Optional <HorariosDisponibles> findByProfesionalId (String idProfesional);
		
		Optional <HorariosDisponibles> findByProfesionalIdAndFecha (String idProfesional, String fecha);
	    
	    List<HorariosDisponibles> findHorariosByProfesionalIdAndFecha(String idProfesional, String fecha);
	    
	    @Query("SELECT h.horarios FROM HorariosDisponibles h WHERE h.profesional.id = :idProfesional AND h.fecha = :fecha")
	    Optional<HorariosDisponibles> findOptionalHorariosByProfesionalIdAndFecha(@Param("idProfesional") String idProfesional, @Param("fecha") String fecha);
}
