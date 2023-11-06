package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;

import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;

import jakarta.transaction.Transactional;


//@Controller
@RestController
@RequestMapping("/admin")
public class ControladorAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	@Autowired
	public ServicioAdmin servicioAdmin;
	@Autowired
	public ServicioCliente servicioCliente;
	
	//Agregamos un admin a la base de datos
	@PostMapping("/ingresar")
	public String ingresarAdmin(@RequestBody Admin admin) throws SQLException {
		
			try {
				servicioAdmin.registrarAdmin(admin);
				System.out.println("Admin Agregado con exito");
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("no se agrego");
			}
		
		
		return "login";
	}
	
	//Eliminamos un admin de la base de datos
	@PostMapping("/eliminarAdmin")
	public String eliminarAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.borrarAdmin(admin);
			System.out.println("Eliminado Admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/modificarAdmin")
	public String actualizarAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.modificarAdmin(admin);
			System.out.println("Actualizado con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/bajaAdmin")
	public String bajaAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.bajaAdmin(admin);
			System.out.println("baja admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/altaAdmin")
	public String altaAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.altaAdmin(admin);
			System.out.println("alta admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index.html";
	}
	
	@GetMapping("listaAdmin")
    public List<Admin> listaAdmins() throws MiExcepcion {

        List<Admin> administradores = servicioAdmin.listarAdmins();
       // modelo.addAttribute("administradores", administradores);

        return administradores;

    }
	
	@GetMapping("listaCliente")
    public List<Cliente> listaCliente() throws MiExcepcion {

        List<Cliente> clientes = servicioCliente.listarClientes();
       // modelo.addAttribute("administradores", administradores);

        return clientes;

    }
	
	
	@PostMapping("/modificarCliente")
	public String actualizarCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.modificarCliente(cliente);
			System.out.println("Actualizado con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/eliminarCliente")
	public String eliminarCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioAdmin.borrarCliente(cliente);
			System.out.println("Eliminado Cliente con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	

	
	@PostMapping("/bajaCliente")
	public String bajaCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.bajaCliente(cliente);
			System.out.println("baja con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@PostMapping("/altaCliente")
	public String altaCliente(@RequestBody Cliente cliente) {
		
		try {
			servicioCliente.altaCliente(cliente);
			System.out.println("alta con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index.html";
	}
	
	
	
	
}
