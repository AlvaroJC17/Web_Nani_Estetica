package com.proyecto_integrador_3.Estetica.Servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

@Service
public class ServicioPersona {

	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	
	//Borra una persona de la base de datos
			public void borrarPersona(String id) {
				Optional <Persona> identificarPersona = repositorioPersona.findById(id);
				
				if (identificarPersona.isPresent()) {
					Persona personaDelete = identificarPersona.get();
					repositorioPersona.delete(personaDelete);
				}
			}
	
}
