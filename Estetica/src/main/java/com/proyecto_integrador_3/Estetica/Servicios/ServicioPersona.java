package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;

import jakarta.transaction.Transactional;

@Service
public class ServicioPersona {

	@Autowired
	public RepositorioPersona repositorioPersona;
	
	
	// Lo uso
	public List <Persona> buscarPacienteByRolAndNombre(String nombre, Rol rol){
		return repositorioPersona.buscarPacientePorRolYNombre(rol, nombre);
	}
	
	 //Lo uso
    public List<Persona> buscarPacienteByRolAndDni(String dni, Rol rol){
    	return repositorioPersona.buscarPacientePorRolYDni(rol, dni);
    }
    
    //Lo uso
	public List <Persona> buscarPacienteByRolAndEmail2(String email, Rol rol){
    	return repositorioPersona.buscarPacientePorRolYEmail(rol, email);
    }
	
	@Transactional
	public Optional<Persona> buscarPersonaPorEmailOptional(String emailUsuario) throws MiExcepcion{
		
		try {
			Optional<Persona> buscarPersona = repositorioPersona.buscarPorEmailOptional(emailUsuario);
			return buscarPersona;
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con la base de datos");
		}
	}
			
	public Persona buscarDatosPersonaPorId(String idPersona) throws MiExcepcion {
		Optional<Persona> buscarPersona = repositorioPersona.findById(idPersona);
		if (buscarPersona.isPresent()) {
			Persona datosDePersona = buscarPersona.get();
			return datosDePersona;
		}else {
			throw new MiExcepcion("No se encontro al profesional");
		}
	}
	
	public Persona buscarDatosPersonaPorEmail(String emailPersona) throws MiExcepcion {
		Optional<Persona> buscarPersona = repositorioPersona.findById(emailPersona);
		if (buscarPersona.isPresent()) {
			Persona datosDePersona = buscarPersona.get();
			return datosDePersona;
		}else {
			throw new MiExcepcion("No se encontro a la persona");
		}
	}
		
	
	//Borra una persona de la base de datos
	@Transactional
	public void borrarPersona(String id) throws MiExcepcion {
	
		try {			
			Optional <Persona> identificarPersona = repositorioPersona.findById(id);
			if (identificarPersona.isPresent()) {
				Persona personaDelete = identificarPersona.get();
				repositorioPersona.delete(personaDelete);
			}
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con la base de datos " + e);
		}
	}
	
	public List<Persona> buscarNombreApellidoPorRol(Rol rol) throws MiExcepcion {
		try {
			List<Persona> nombreApellidoPorRol = repositorioPersona.buscarNombreApellidoPorRol(rol);
			return nombreApellidoPorRol;
		} catch (Exception e) {
			throw new MiExcepcion("Error al conectar con el servidor " + e);
		}
			
	}
}
