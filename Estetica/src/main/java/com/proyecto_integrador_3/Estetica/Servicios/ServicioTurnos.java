package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;

import static java.lang.Boolean.FALSE;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;

import jakarta.transaction.Transactional;




@Service
public class ServicioTurnos {
	
	@Autowired
	RepositorioTurnos repositorioTurnos;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	
	public List <Turnos> buscarTurnoPorClienteId(String idCliente){
		return repositorioTurnos.findTurnosByClienteId(idCliente);
	}

	public Optional <Turnos> buscarTurnoPorId(String id){
		return repositorioTurnos.findById(id);
	}
	
	public List<Turnos> obtenerTurnosPorDni(String dni) {
        return repositorioTurnos.findByDni(dni);
    }
	
	public List<Turnos> obtenerTurnosPorEmail(String email){
		return repositorioTurnos.findByEmail(email);
	}
	
	 public boolean checkActiveTurnos(String email) {
	        List<Turnos> activeTurnos = repositorioTurnos.findByActivoAndEmail(true, email);
	        return activeTurnos.size() > 2;
	    }
	
	
	public void actualizarEstadoDelTurno(String id) {
		Optional<Turnos> turnoPorId = buscarTurnoPorId(id);
		if (turnoPorId.isPresent()) {
			Turnos actualizarTurnoActivo = turnoPorId.get();
			actualizarTurnoActivo.setActivo(FALSE);
			repositorioTurnos.save(actualizarTurnoActivo);
		}
	}
	
	//Busca los turnos por email en orden ascendente
	public List<Turnos> getTurnosByEmail(String email) {
        return repositorioTurnos.findByEmailOrderByFechaAsc(email);
    }

	//busca los turnos en orden de mas viejo a mas nuevo y cuando el tamaño de los turnos del usuario
	// es mayor a 3, elimina el mas antigua de la base de datos
    public void eliminarTurnoMasAntiguo(String email) {
        List<Turnos> turnos = getTurnosByEmail(email);
        if (turnos.size() > 3 ) {
            Turnos turnoMasAntiguo = turnos.get(0);
            repositorioTurnos.delete(turnoMasAntiguo);
        }
    }
    
    public List<Turnos> getTurnosNoActivosByEmail(String email) {
        return repositorioTurnos.findByEmailAndActivoOrderByFechaAsc(email, false);
    }

    	
    public void eliminarTurnoMasAntiguoNoActivo(String email) throws MiExcepcion {
        List<Turnos> turnos = getTurnosByEmail(email);
        if (turnos.size() > 3) {
            List<Turnos> turnosNoActivos = getTurnosNoActivosByEmail(email);
            while (turnos.size() > 3 && !turnosNoActivos.isEmpty()) {
                Turnos turnoMasAntiguoNoActivo = turnosNoActivos.get(0);
                repositorioTurnos.delete(turnoMasAntiguoNoActivo);
                turnosNoActivos = getTurnosNoActivosByEmail(email);  // Refresca la lista de turnos no activos
                turnos = getTurnosByEmail(email);  // Refresca la lista de todos los turnos
            }
        }
    }
    
    
    //pasa los turnos activos con fecha anterior a la actual a inactivo
    public void actualizarTurnosAntiguos(String email) {
        List<Turnos> turnos = getTurnosByEmail(email);
        LocalDate fechaActual = LocalDate.now();
        
        for (Turnos turno : turnos) {
            String fechaTurno = turno.getFecha().toString();
            LocalDate fechaTurnoLocalDate = null;
			try {
				fechaTurnoLocalDate = servicioCliente.pasarFechaStringToLocalDate(fechaTurno);
				if (fechaTurnoLocalDate.isBefore(fechaActual) && turno.getActivo()) {
					turno.setActivo(false);
					repositorioTurnos.save(turno);
				}
			} catch (MiExcepcion e) {
				System.out.println("no se pudo convertir la fecha a date en el metodo actulizarTurnosAntiguo");
			}
        }
    }
		
    public List<Turnos> obtenerTurnosPorProfesionalYFecha(Profesional profesional, LocalDate fecha) {
        return repositorioTurnos.findByProfesionalAndFecha(profesional, fecha);
    }
    
   
}
    
				 
				 
           

			
		
		
	
	
