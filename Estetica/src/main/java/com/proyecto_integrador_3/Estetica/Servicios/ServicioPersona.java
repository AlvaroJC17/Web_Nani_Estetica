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
