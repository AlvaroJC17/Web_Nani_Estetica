package com.proyecto_integrador_3.Estetica.Servicios;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioCliente;

import jakarta.transaction.Transactional;

@Service
public class ServicioAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	
	@Autowired
	public RepositorioCliente repositorioCliente;
	
	//Registra un admin en la base de datos
	@Transactional
	public void registrarAdmin(Admin admin) throws MiExcepcion {
		
		validarDatos(admin.getDni(), admin.getEmail(), admin.getNombre(), admin.getApellido(), admin.getDomicilio(), admin.getTelefono());

			Admin nuevo_admin = new Admin();
				
			nuevo_admin.setEmail(admin.getEmail());
			nuevo_admin.setRol(admin.getRol().ADMIN);
			nuevo_admin.setActivo(admin.getActivo());
			nuevo_admin.setDni(admin.getDni());
			nuevo_admin.setNombre(admin.getNombre());
			nuevo_admin.setApellido(admin.getApellido());
			nuevo_admin.setTelefono(admin.getTelefono());
			nuevo_admin.setDomicilio(admin.getDomicilio());
			nuevo_admin.setFechaNacimiento(admin.getFechaNacimiento());
			nuevo_admin.setSexo(admin.getSexo());
			repositorioAdmin.save(nuevo_admin);
		
		}
	
	
	//Actualiza los datos de un Admin en la base de datos
	@Transactional
	public void modificarAdmin(Admin admin ) {
		
		Optional<Admin> identificarAdmin = repositorioAdmin.findById(admin.getId());
		if (identificarAdmin.isPresent()) {
			Admin admin_modificado = identificarAdmin.get(); // Atribuye el objeto presente a esta nueva variable
			admin_modificado.setDni(admin.getDni());
			admin_modificado.setEmail(admin.getEmail());
			admin_modificado.setRol(admin.getRol().ADMIN);
			admin_modificado.setActivo(admin.getActivo());
			admin_modificado.setNombre(admin.getNombre());
			admin_modificado.setApellido(admin.getApellido());
			admin_modificado.setSexo(admin.getSexo());
			admin_modificado.setFechaNacimiento(admin.getFechaNacimiento());
			admin_modificado.setDomicilio(admin.getApellido());
			admin_modificado.setTelefono(admin.getTelefono());
			repositorioAdmin.save(admin_modificado);
		}
	}
			
	//Borra un admin de la base de datos
	public void borrarAdmin(Admin admin) {
		Optional <Admin> identificarAdmin = repositorioAdmin.findById(admin.getId());
		
		if (identificarAdmin.isPresent()) {
			Admin adminDelete = identificarAdmin.get();
			repositorioAdmin.delete(adminDelete);
			
		}
	}
	
	 @Transactional
	    public void bajaAdmin(Admin admin) {
	        Optional<Admin> presente = repositorioAdmin.findById(admin.getId());

	        if (presente.isPresent()) {
	        	Admin admin_baja = new Admin();
	        	admin_baja = presente.get();
	        	admin_baja.setActivo(FALSE); //El valor false se importa de un clase propia de java
	        	repositorioAdmin.save(admin_baja);
	        }
	    }
	 
	 //Metodo para cambiar a usuarios que estaban inactivos a activos
	 @Transactional
	    public void altaAdmin(Admin admin) {

	        Optional<Admin> presente = repositorioAdmin.findById(admin.getId());

	        if (presente.isPresent()) {
	        	Admin admin_alta = new Admin();
	            admin_alta = presente.get();
	            admin_alta.setActivo(TRUE);
	            repositorioAdmin.save(admin_alta);
	        }
	    }
	 
	 @Transactional()
	    public List<Admin> listarAdmins() {

	        List<Admin> admins = new ArrayList();

	        admins = repositorioAdmin.findAll();

	        return admins;
	    }
	 
	 
	 //SERVICIOS PARA LOS CLIENTES
	 
	 @Transactional
	    public void borrarCliente(Cliente cliente) throws MiExcepcion {
			if (cliente.getId() != null && cliente instanceof Cliente) {
				repositorioCliente.delete(cliente);
			}else {
				throw new MiExcepcion("No se pueden borrar admin");
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

		  
		 //Metodo para cambiar a usuarios que estaban inactivos a activos
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
	 
	 //Metodos para validar
	 
	 public static void validarDatos(String dni, String email, String nombre, String apellido,
				 String domicilio, Integer telefono) throws MiExcepcion {
		 
		 if (dni == null || dni.isEmpty() || dni.trim().isEmpty()) {
			throw new MiExcepcion("El dni no puede estar vacio");
		}
		 if (email == null || email.isEmpty() || email.trim().isEmpty()) {
				throw new MiExcepcion("El email no puede estar vacio");
			}
		 if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
				throw new MiExcepcion("El nombre no puede estar vacio");
			}
		 if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
				throw new MiExcepcion("El apellido no puede estar vacio");
			}
		 if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
				throw new MiExcepcion("El domicilio no puede estar vacio");
			}
		 if (telefono == null || telefono.toString().isEmpty() || telefono.toString().isEmpty()) {
				throw new MiExcepcion("EL telefono no puede estar vacio");
			}
		 
		 
	 }
		
	
}
