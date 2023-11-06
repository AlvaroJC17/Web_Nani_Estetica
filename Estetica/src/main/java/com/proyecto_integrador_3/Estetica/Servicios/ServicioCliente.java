package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;

import jakarta.transaction.Transactional;

@Service
public class ServicioCliente {

	@Autowired
	private RepositorioCliente repositorioCliente;
	
	@Transactional
	public void registrarCliente(Cliente cliente_nuevo) throws MiExcepcion {
		
			/*SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Date dataFormateada = formato.parse();*/   
	        
		Cliente cliente = new Cliente();
 
        cliente.setEmail(cliente_nuevo.getEmail());
        cliente.setRol(cliente_nuevo.getRol());
        cliente.setActivo(cliente_nuevo.getActivo());
        cliente.setDni(cliente_nuevo.getDni());
        cliente.setNombre(cliente_nuevo.getNombre());
        cliente.setApellido(cliente_nuevo.getApellido());
        cliente.setEdad(cliente_nuevo.getEdad());
        cliente.setTelefono(cliente_nuevo.getTelefono());
        cliente.setDomicilio(cliente_nuevo.getDomicilio());
        cliente.setFechaNacimiento(cliente_nuevo.getFechaNacimiento());
        cliente.setSexo(cliente_nuevo.getSexo());
        repositorioCliente.save(cliente);
	}
	
	@Transactional
	public void modificarCliente(Cliente cliente) throws MiExcepcion {
		
			System.out.println(cliente.getApellido());
			System.out.println(cliente.getDni());
			System.out.println(cliente.getId());
		    
		    if (cliente.getId() != null) {
				
        	Cliente cliente_actualizado = new Cliente();
        	cliente_actualizado.setDni(cliente.getDni());
        	cliente_actualizado.setEmail(cliente.getEmail());
        	cliente_actualizado.setRol(cliente.getRol());
        	cliente_actualizado.setActivo(cliente.getActivo());
        	cliente_actualizado.setNombre(cliente.getNombre());
        	cliente_actualizado.setApellido(cliente.getApellido());
        	cliente_actualizado.setEdad(cliente.getEdad());
        	cliente_actualizado.setSexo(cliente.getSexo());
        	cliente_actualizado.setFechaNacimiento(cliente.getFechaNacimiento());
        	cliente_actualizado.setDomicilio(cliente.getDomicilio());
        	cliente_actualizado.setTelefono(cliente.getTelefono());
        	repositorioCliente.save(cliente_actualizado);
        	repositorioCliente.delete(cliente);
		    }
	}
        		
	@Transactional
    public void borrarCliente(Cliente cliente) {
		if (cliente.getId() != null) {
			repositorioCliente.delete(cliente);
		}
	}
		
	
	 @Transactional
	    public void bajaCliente(Cliente cliente) {
	        
		 if (cliente.getId() != null) {
				
	        	Cliente cliente_actualizado = new Cliente();
	        	cliente_actualizado.setDni(cliente.getDni());
	        	cliente_actualizado.setEmail(cliente.getEmail());
	        	cliente_actualizado.setRol(cliente.getRol());
	        	cliente_actualizado.setActivo(FALSE);
	        	cliente_actualizado.setNombre(cliente.getNombre());
	        	cliente_actualizado.setApellido(cliente.getApellido());
	        	cliente_actualizado.setEdad(cliente.getEdad());
	        	cliente_actualizado.setSexo(cliente.getSexo());
	        	cliente_actualizado.setFechaNacimiento(cliente.getFechaNacimiento());
	        	cliente_actualizado.setDomicilio(cliente.getDomicilio());
	        	cliente_actualizado.setTelefono(cliente.getTelefono());
	        	repositorioCliente.save(cliente_actualizado);
	        	repositorioCliente.delete(cliente);
			    }
		}

	       
	 
	 //Metodo para cambiar a usuarios que estaban inactivos a activos
	 @Transactional
	    public void altaCliente(Cliente cliente) {

		 if (cliente.getId() != null) {
				
	        	Cliente cliente_actualizado = new Cliente();
	        	cliente_actualizado.setDni(cliente.getDni());
	        	cliente_actualizado.setEmail(cliente.getEmail());
	        	cliente_actualizado.setRol(cliente.getRol());
	        	cliente_actualizado.setActivo(TRUE);
	        	cliente_actualizado.setNombre(cliente.getNombre());
	        	cliente_actualizado.setApellido(cliente.getApellido());
	        	cliente_actualizado.setEdad(cliente.getEdad());
	        	cliente_actualizado.setSexo(cliente.getSexo());
	        	cliente_actualizado.setFechaNacimiento(cliente.getFechaNacimiento());
	        	cliente_actualizado.setDomicilio(cliente.getDomicilio());
	        	cliente_actualizado.setTelefono(cliente.getTelefono());
	        	repositorioCliente.save(cliente_actualizado);
	        	repositorioCliente.delete(cliente);
			    }
	    }
	

}
