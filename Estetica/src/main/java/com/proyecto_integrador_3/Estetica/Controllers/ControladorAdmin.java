package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;

import jakarta.transaction.Transactional;


@Controller
//@RestController
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
	/*@PostMapping("/eliminarAdmin")
	public String eliminarAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.borrarAdmin(admin);
			System.out.println("Eliminado Admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		return "index";
	}*/
		
	
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
	
	/*@PostMapping("/bajaAdmin")
	public String bajaAdmin(@RequestParam(name = "seleccionados") String seleccionados) {
		
		System.out.println("ID: " + seleccionados);
		
		try {
			servicioAdmin.bajaAdmin(seleccionados);
			System.out.println("baja admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "/pagina_admin/portalAdmin";
	}
	
	@PostMapping("/altaAdmin")
	public String altaAdmin(@RequestParam(name = "seleccionados") String seleccionados) {
		
		try {
			servicioAdmin.altaAdmin(seleccionados);
			System.out.println("alta admin con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		return "/pagina_admin/portalAdmin";
	}*/
		
	
	@GetMapping("listarAdmin")
    public String listarAdmins(Model model) throws MiExcepcion {

        List<Admin> administradores = servicioAdmin.listarAdmins();
        model.addAttribute("administradores", administradores);

        return "/pagina_admin/portalAdmin";
	}
	
	//Este metodo trabaja conjunto con el de buscar nombre o dni, este se encarga de mostrar la pag con la tabla oculta
	// y luego cuando apretamos el boton buscar este nos conecta con la pag de buscarDNIoNombre
	@GetMapping("ocultarLista")
    public String ocultarLista(Model model) throws MiExcepcion {
        List<Admin> administradores = servicioAdmin.listarAdmins();
        model.addAttribute("administradores", false);

        return "/pagina_admin/portalAdmin";
	}
	
	//Metodo para buscar por nombre o dni
	@PostMapping("buscarDNIoNombre")
	public String buscarDNI(@RequestParam(name = "dato") String dato, Model model) {
		List<Admin> administradoresDni = servicioAdmin.buscarDni(dato);
		List<Admin>	administradoresNombre = servicioAdmin.buscarNombre(dato);
		
		if (administradoresDni.isEmpty()) {
			administradoresDni = null;
		}
		
		if (administradoresNombre.isEmpty()) {
			administradoresNombre = null;
		}
		
		if (administradoresDni != null) {
			model.addAttribute("administradores", administradoresDni); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento 
		}
		
		if (administradoresNombre != null) {
			model.addAttribute("administradores", administradoresNombre);
		}
		
		return "/pagina_admin/portalAdmin";
	}
	
	// En este metodo unificamos la edicion y eliminacion de un usuario a traves de un solo formulario
	// usanso el action como valor para las diferentes condiciones
	@PostMapping("modificarRol")
	public String modificarRol(@RequestParam(name = "seleccionados") String id,
            @RequestParam(name = "nuevoRol") String nuevoRolNombre, @RequestParam(name = "action") String action,
            Model model) {
		
		Rol nuevoRol = null;
		
		if (!"Modifical rol".equals(nuevoRolNombre)) {
			try {
	            // Convertir el nuevoRol solo si no es "Modificar Rol"
	            nuevoRol = Rol.valueOf(nuevoRolNombre.toUpperCase());
	            
	        } catch (IllegalArgumentException e) {
	            // Manejar el caso donde nuevoRolNombre no es un valor válido de Rol
	            model.addAttribute("error", "Valor de rol no válido.");
	        }
		}
		
		if (action.equals("modificarRol")) {
			//metodo para moficar el rol
			servicioAdmin.modificarRol(id, nuevoRol);
		}
		
		if (action.equals("altaAdmin")) {
			try {
				//metodo para modificar el activo del usuario a True
				servicioAdmin.altaAdmin(id);
				System.out.println("alta admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
		}
		
		if (action.equals("bajaAdmin")) {
			try {
				//metodo para modificar el activo del usuario a False
				servicioAdmin.bajaAdmin(id);
				System.out.println("baja admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
			
		}
			
		if (action.equals("eliminarAdmin")) {
			//Meotod para eliminar a un usuario de la base de datos
			try {
				servicioAdmin.borrarAdmin(id);
				System.out.println("Eliminado Admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
		}
		
		return "/pagina_admin/portalAdmin";
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
