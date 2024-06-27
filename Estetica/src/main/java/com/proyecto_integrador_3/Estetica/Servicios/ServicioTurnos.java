package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.EstadoDelTurno;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;

import jakarta.transaction.Transactional;




@Service
public class ServicioTurnos {
	
	@Autowired
	RepositorioTurnos repositorioTurnos;
	
	@Autowired
	RepositorioCliente repositorioCliente;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioCliente servicioCliente;
	
	
//	@Transactional
//    public void actualizarMultas(String nuevoValorMulta) {
//		repositorioTurnos.updateAllMultas(nuevoValorMulta);
//    }
	
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
			actualizarTurnoActivo.setEstado(EstadoDelTurno.CANCELADO);
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

    	
    //Cando hay mas de tres turnos en la bandeja, busca el mas antiguo que no tenga multa y lo borra de la base de datos
    public void eliminarTurnoMasAntiguoNoActivo(String email) throws MiExcepcion {
    	List<Turnos> turnos = getTurnosByEmail(email);
        if (turnos.size() > 3) {
            List<Turnos> turnosNoActivosSinMulta = repositorioTurnos.findByEmailAndActivoFalseAndMultaFalse(email);
            while (turnos.size() > 3 && !turnosNoActivosSinMulta.isEmpty()) {
                Turnos turnoMasAntiguoNoActivo = turnosNoActivosSinMulta.get(0);
                repositorioTurnos.delete(turnoMasAntiguoNoActivo);
                turnosNoActivosSinMulta = repositorioTurnos.findByEmailAndActivoFalseAndMultaFalse(email);  // Refresca la lista de turnos no activos sin multa
                turnos = getTurnosByEmail(email);  // Refresca la lista de todos los turnos
            }
        }
    }
    
    
    //pasa los turnos activos con fecha anterior a la actual a inactivo y les asigna una multa al turno y al cliente
    public void actualizarTurnosAntiguos(String email) {
        List<Turnos> turnos = getTurnosByEmail(email);
          LocalDateTime fechaActual = LocalDateTime.now();
        
        for (Turnos turno : turnos) {
            String fechaTurno = turno.getFecha().toString();
            String horarioTurno = turno.getHorario().toString();
            String fechaAndHorario = fechaTurno + " " + horarioTurno;
            LocalDateTime fechaTurnoLocalDateTime = null;
			try {
				fechaTurnoLocalDateTime = servicioCliente.pasarFechaStringToLocalDateTime(fechaAndHorario);
				if (fechaTurnoLocalDateTime.isBefore(fechaActual.plusMinutes(15)) && turno.getActivo()) { //comparamos la fecha del tunos con la fecha actual mas 15 min
					turno.setMulta(TRUE); //Le colocamos una multa al turno que tiene fecha pasada
					turno.setActivo(false); // Lo pasamos a inactivo
					turno.setEstado(EstadoDelTurno.CANCELADO);
					repositorioTurnos.save(turno);
					
					Optional<Cliente> obtenerDatosDeMultas = repositorioCliente.findByEmail(email);
					if (obtenerDatosDeMultas.isPresent()) {
						Cliente multasDelCliente = obtenerDatosDeMultas.get();
						 multasDelCliente.setMulta(TRUE); // Le colocamos una multa al cliente
						 repositorioCliente.save(multasDelCliente);
					}
				}
			} catch (MiExcepcion e) {
				System.out.println("no se pudo convertir la fecha a date en el metodo actulizarTurnosAntiguo");
			}
        }
    }
    
    	
    public void multarTurnoAndClienteMenosDe24Horas(String email, String idTurno) {
    	Optional<Turnos> turnoOptional = repositorioTurnos.findById(idTurno);
        if (turnoOptional.isPresent()) {
            Turnos turno = turnoOptional.get();
            
            if (turno.getActivo()) {
            	turno.setMulta(TRUE); // Le colocamos una multa al turno que tiene fecha pasada
            	turno.setActivo(false); // Lo pasamos a inactivo
            	turno.setEstado(EstadoDelTurno.CANCELADO);
            	repositorioTurnos.save(turno);
            	
            	Optional<Cliente> obtenerDatosDeMultas = repositorioCliente.findByEmail(email);
            	if (obtenerDatosDeMultas.isPresent()) {
            		Cliente multasDelCliente = obtenerDatosDeMultas.get();
            		multasDelCliente.setMulta(TRUE); // Le colocamos una multa al cliente
            		repositorioCliente.save(multasDelCliente);
            	}
            }
        }
    }
    
            
                    
               
		
    public List<Turnos> obtenerTurnosPorProfesionalYFecha(Profesional profesional, LocalDate fecha) {
        return repositorioTurnos.findByProfesionalAndFecha(profesional, fecha);
    }
    
   
}
    
				 
				 
           

			
		
		
	
	
