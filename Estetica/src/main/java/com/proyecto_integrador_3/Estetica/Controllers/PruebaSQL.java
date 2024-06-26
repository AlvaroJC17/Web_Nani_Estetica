package com.proyecto_integrador_3.Estetica.Controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioHorariosDisponibles;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioProfesional;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioTurnos;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioEmail;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioHorario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioProfesional;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioTurnos;
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
	RepositorioTurnos repositorioTurnos;
	
	@Autowired
	ServicioUsuario servicioUsuario;
	
	@Autowired
	ServicioPersona servicioPersona;
	
	@Autowired
	ServicioProfesional servicioProfesional;
	
	@Autowired
	ServicioHorario servicioHorario;
	
	@Autowired
	ServicioTurnos servicioTurnos;
	
	@Autowired
	ServicioEmail servicioEmail;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	

	

	
	
	@Override
	public void run(String... args) throws Exception {
		
		
		//Codigo para crear un administrador default en la base de datos cuando se inicia
		//el programa por primera vez
		if (!repositorioAdmin.existsByRol(Rol.ADMIN)) {
            Admin admin = new Admin();
            admin.setActivo(true);
            admin.setApellido("Cortesia");
            admin.setContrasena("QWEasd12");
            admin.setDni("17911977");
            admin.setDomicilio("El bosque");
            admin.setEmail("admin@admin.com");
            admin.setEmailValidado(true);
            admin.setFechaCreacion(LocalDateTime.now());
            admin.setNombre("Alvaro");
            admin.setRol(Rol.ADMIN);
            admin.setValidacionForm(true);
            admin.setOcupacion("Ingeniero");
            admin.setTelefono("22736382");
            admin.setSexo(Sexo.MASCULINO);
            repositorioAdmin.save(admin);
            System.out.println("Administrador creado: " + admin);
        } else {
            System.out.println("Ya existe un administrador en la base de datos.");
        }
		
//		EmailUsuarios pruebaCorreo = new EmailUsuarios();
//		pruebaCorreo.setDestinatario("alvarocortesia@gmail.com");
//		pruebaCorreo.setAsunto("Prueba de plantilla");
//		pruebaCorreo.setMensaje("Esto es una prueba usando la plantilla para HTML");
//		String plantillaHTML = "emailValidarCorreo";
//		
//		try {
//			
//			servicioEmail.enviarEmailUsuario(pruebaCorreo, plantillaHTML);
//			System.out.println("Mail enviado exitosamente");
//			
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			System.out.println("No se pudo enviar el mail");
//		}
		
		
		// <!-- -->java -jar myapp.jar --spring.profiles.active=dev
		
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
			
//			String fecha = "2024-06-19";
//			String idProfesional = "b2249e1a-597b-4875-92b0-166f701b9437";
//		        
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

		
//			List<Persona> prueba12 = servicioProfesional.buscarProfesionaByRolAndProvinciasYActivo(Rol.PROFESIONAL, Provincias.BUENOS_AIRES, TRUE);
//					
//			for(Persona persona: prueba12) {
//				System.out.println("PROFESIONAL ROL, PROVINCIA, ACTIVO :" + persona.getNombre() + " " + persona.getId() + " " + persona.getActivo());
//				
//			}
		     
		
//		    Optional <Turnos> bucarDatosTurno = repositorioTurnos.findByClienteIdAndMulta("917c171e-966e-47f5-b82a-5857777e29fd", TRUE);
//		    		
//		    if (bucarDatosTurno.isPresent()) {
//				Turnos datosTurno = bucarDatosTurno.get();
//				LocalDate fecha = datosTurno.getFecha();
//				String horario = datosTurno.getHorario();
//				Boolean multa = datosTurno.getMulta();
//				String constoMulta = datosTurno.getCostoMulta();
//				
//				System.out.println("Fecha: " + fecha + " " + "horario: " + horario + " " + "multa: " + multa + " " + "Costo multa: " + constoMulta);
//		    }
//				
				
//		List<Turnos> turnosInactivosSinMulta = repositorioTurnos.findByEmailAndActivoFalseAndMultaFalse("santi@santi.com");
//		
//		for(Turnos turnos: turnosInactivosSinMulta) {
//		System.out.println("Turnos: " +  turnos.getId());
//		}
//			

	
	}
		
		
}
