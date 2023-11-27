package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;


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
		if (cliente_nuevo != null) {
			
			Cliente cliente = new Cliente();
			cliente.setEmail(cliente_nuevo.getEmail());
			cliente.setRol(cliente_nuevo.getRol());
			cliente.setActivo(cliente_nuevo.getActivo());
			cliente.setDni(cliente_nuevo.getDni());
			cliente.setNombre(cliente_nuevo.getNombre());
			cliente.setApellido(cliente_nuevo.getApellido());
			cliente.setOcupacion(cliente_nuevo.getOcupacion());
			cliente.setTelefono(cliente_nuevo.getTelefono());
			cliente.setDomicilio(cliente_nuevo.getDomicilio());
			cliente.setFechaNacimiento(cliente_nuevo.getFechaNacimiento());
			cliente.setSexo(cliente_nuevo.getSexo());
			repositorioCliente.save(cliente);
		}
	}
			
	
	@Transactional
	public void modificarCliente(Cliente cliente ) {
		Optional<Cliente> identificarCliente = repositorioCliente.findById(cliente.getId());
		
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get(); // Atribuye el objeto presente a esta nueva variable
			cliente_actualizado.setDni(cliente.getDni());
        	cliente_actualizado.setEmail(cliente.getEmail());
        	cliente_actualizado.setRol(cliente.getRol());
        	cliente_actualizado.setActivo(cliente.getActivo());
        	cliente_actualizado.setNombre(cliente.getNombre());
        	cliente_actualizado.setApellido(cliente.getApellido());
        	cliente.setOcupacion(cliente.getOcupacion());
        	cliente_actualizado.setSexo(cliente.getSexo());
        	cliente_actualizado.setFechaNacimiento(cliente.getFechaNacimiento());
        	cliente_actualizado.setDomicilio(cliente.getDomicilio());
        	cliente_actualizado.setTelefono(cliente.getTelefono());
        	repositorioCliente.save(cliente_actualizado);
		}
	}
        		
	@Transactional
    public void borrarCliente(Cliente cliente) {
		
		Optional<Cliente> identificarCliente = repositorioCliente.findById(cliente.getId());
		if (identificarCliente.isPresent()) {
			Cliente cliente_actualizado = identificarCliente.get();
			repositorioCliente.delete(cliente_actualizado);
		}
	}
		
	 @Transactional
	    public void bajaCliente(Cliente cliente) {
	        
		 Optional<Cliente> identificarCliente = repositorioCliente.findById(cliente.getId());
				
		 if (identificarCliente.isPresent()) {
			 Cliente cliente_actualizado = identificarCliente.get();
			 cliente_actualizado.setActivo(FALSE);
			 repositorioCliente.save(cliente_actualizado);
		 }
	 }
			
	      
	 @Transactional
	    public void altaCliente(Cliente cliente) {
	        
		 Optional<Cliente> identificarCliente = repositorioCliente.findById(cliente.getId());
				
		 if (identificarCliente.isPresent()) {
			 Cliente cliente_actualizado = identificarCliente.get();
			 cliente_actualizado.setActivo(TRUE);
			 repositorioCliente.save(cliente_actualizado);
		 }
	 }
	 
	 @Transactional()
	    public List<Cliente> listarClientes() {

	        List<Cliente> clientes = new ArrayList();

	        clientes = repositorioCliente.findAll();

	        return clientes;
	    }

}
