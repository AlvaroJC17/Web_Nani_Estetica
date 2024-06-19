package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.HorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

@Component
public class PruebaSQL implements CommandLineRunner {

	@Autowired
	RepositorioUsuario repositorioUsuario;
	
	@Autowired
	RepositorioPersona repositorioPersona;
	
	@Autowired
	RepositorioProfesional repositorioProfesional;
	
	@Autowired
	RepositorioAdmin repositorioAdmin;
	
	@Autowired
	RepositorioCliente repositorioCliente;
	
	@Autowired
	RepositorioHorariosDisponibles repositorioHorariosDisponibles;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioPersona servicioPersona;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	

	

	
	
	@Override
	public void run(String... args) throws Exception {
		
		
		//Codigo para crear un administrador default en la base de datos cuando se inicia
		//el programa por primera vez
		if (!repositorioAdmin.existsByRol(Rol.ADMIN)) {
            Admin admin = new Admin("1791", "QWEasd12", "admin@admin.com", Rol.ADMIN, true, "Legion", "Foxy");
            repositorioAdmin.save(admin);
            System.out.println("Administrador creado: " + admin);
        } else {
            System.out.println("Ya existe un administrador en la base de datos.");
        }
		
		
//		List <Usuario> prueba1 = repositorioUsuario.buscarPorNombre("cliente");
//		prueba1.forEach(System.out::println);
//		
//		
//		List <Usuario> prueba2 = servicioUsuario.buscarRol(Rol.PROFESIONAL);
//		prueba2.forEach(System.out::println);
//		
//		
//		
//		List<Persona> prueba3 = servicioPersona.buscarNombreApellidoPorRol(Rol.PROFESIONAL);
//		prueba3.forEach(System.out::println);
//		
//		for(Persona persona: prueba3) {
//			System.out.println("Nombre :" + persona.getNombre());
//		}
//		
//		
//		List<Persona> prueba4 = servicioProfesional.buscarPacienteByRolAndDni("63565373", Rol.CLIENTE);
//		
//		for(Persona persona: prueba4) {
//			System.out.println("DNI ROL :" + persona.getNombre());
//		}
//		List<Persona> prueba5 = servicioProfesional.buscarPacienteByRolAndNombre("Deyannys", Rol.CLIENTE);
//		
//		for(Persona persona: prueba5) {
//			System.out.println("NOMBRE ROL :" + persona.getDni());
//		}
//		
//		List<Persona> prueba6 = servicioProfesional.buscarPacienteByRolAndEmail2("comida@comida.com", Rol.CLIENTE);
//		
//		for(Persona persona: prueba6) {
//			System.out.println("EMAIL ROL :" + persona.getNombre());
//		}
//		
//		Optional<Cliente> datosCliente = repositorioCliente.obtenerDatosClienteUsuarioPorIdOptional("18156700-d56c-4196-95f1-6e2010ad8974");
//		
//		if (datosCliente.isPresent()) {
//			Cliente nuevoCliente = datosCliente.get();
//			System.out.println(nuevoCliente.getFuma());
//			System.out.println(nuevoCliente.getAmamantando());
//			System.out.println(nuevoCliente.getCuidado_de_piel());
//		}
//		
//		List<Persona> prueba7 = servicioProfesional.buscarProfesionaByRolAndProvincis(Rol.PROFESIONAL, Provincias.BUENOS_AIRES);
//				
//		for(Persona persona: prueba7) {
//			System.out.println("PROFESIONAL PROVINCIA :" + persona.getNombre() + " " + persona.getId());
//			
//		}
		
		
		
//	List <HorariosDisponibles> horariosDisponibles = servicioHorario.obtenerHorariosProfesional("a45c471f-24bd-4fa7-a5ca-59327ca8241d", "2024-06-26");
//		
//		for (HorariosDisponibles horariosDisponibles2 : horariosDisponibles) {
//			System.out.println(horariosDisponibles2.getHorarios());
//		}
		
//		Optional<HorariosDisponibles> optionalHorarios = repositorioHorariosDisponibles.findOptionalHorariosByProfesionalIdAndFecha("b2249e1a-597b-4875-92b0-166f701b9437", "2024-06-28");
//		
//		if (optionalHorarios.isPresent()) {
//			HorariosDisponibles horarios = optionalHorarios.get();
//			List<String> horariosbeta = horarios.getHorarios();
//			
//			for (String horario : horariosbeta) {
//		        System.out.println(horario);
//		    }
//		}
			String fecha = "2024-06-19";
			String idProfesional = "b2249e1a-597b-4875-92b0-166f701b9437";
	
		        // Obtener los horarios disponibles para el profesional y la fecha dada
		       // List<HorariosDisponibles> horariosDisponibles = repositorioHorariosDisponibles.findHorariosByProfesionalIdAndFecha(idProfesional, fecha);
		        

		        // Si no hay horarios disponibles para ese profesional y fecha, retornar una lista vacía
		       

		        // Formatear la fecha proporcionada a LocalDate

		        // Obtener la fecha y hora actual

		        // Lista para almacenar los horarios disponibles
		        //List<String> horariosDisponiblesFormateados = new ArrayList<>();

		        // Recorrer los horarios disponibles y concatenar la fecha y hora en un String
//		        List <String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fecha);
//		        if (horariosDisponibles.isEmpty()) {
//		            System.out.println("la lista esta vacia");
//		        }
//		        for (String horario : horariosDisponibles) {
//		        	LocalDateTime fechaHoraActual = LocalDateTime.now();
//		        	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		            // Crear una variable String para la fecha y hora
//		            String fechaHoraStr = fecha + " " + horario;
//		            LocalDateTime fechaProporcionada = LocalDateTime.parse(fechaHoraStr, dateFormatter);
//
//		            // Parsear la variable String a LocalDateTime
//		           // LocalDateTime fechaHoraProporcionada = LocalDateTime.parse(fechaHoraStr);
//
//		            // Comparar con la fecha y hora actual
//		            if (fechaProporcionada.isAfter(fechaHoraActual)) {
//		                
//		            	System.out.println("imprimir horarios after: " + fechaProporcionada);
//		            	
//		            }else if(fechaProporcionada.isBefore(fechaHoraActual)) {
//		            	System.out.println("imprimir fecha before: " + fechaProporcionada);
//		            	horariosDisponibles.remove(horario);
//		            	servicioHorario.actualizarHorariosDisponibles(fecha, horariosDisponibles, idProfesional);
//		            }
//		        }
			
		        
//		        List<String> horariosDisponibles = servicioHorario.obtenerHorariosDisponiblesPorProfesionalYFecha(idProfesional, fecha);
//		        if (horariosDisponibles.isEmpty()) {
//		            System.out.println("la lista esta vacia");
//		        }
//
//		        List<String> horariosAEliminar = new ArrayList<>();
//		        LocalDateTime fechaHoraActual = LocalDateTime.now();
//		        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//		        for (String horario : horariosDisponibles) {
//		            // Crear una variable String para la fecha y hora
//		            String fechaHoraStr = fecha + " " + horario;
//		            LocalDateTime fechaProporcionada = LocalDateTime.parse(fechaHoraStr, dateFormatter);
//
//		            // Comparar con la fecha y hora actual
//		            if (fechaProporcionada.isAfter(fechaHoraActual)) {
//		                System.out.println("imprimir horarios after: " + fechaProporcionada);
//		            } else if (fechaProporcionada.isBefore(fechaHoraActual)) {
//		                horariosAEliminar.add(horario);
//		                System.out.println("Se actualizo la lista de horarios para la fecha: " + fecha);
//		            }
//		        }
//
//		        // Eliminar los horarios fuera del bucle
//		        horariosDisponibles.removeAll(horariosAEliminar);
//		        servicioHorario.actualizarHorariosDisponibles(fecha, horariosDisponibles, idProfesional);

		
		     
		    
		
	
	}
		
		
}
