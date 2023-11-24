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
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;

import jakarta.transaction.Transactional;


@Controller
//@RestController
public class ControladorAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	@Autowired
	public ServicioAdmin servicioAdmin;
	@Autowired
	public ServicioUsuario servicioUsuario;
	@Autowired
	public RepositorioUsuario repositorioUsuario;
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
	
	
	@GetMapping("listarUsuarios")
    public String listarUsuarios(Model model) throws MiExcepcion {

        List<Usuario> usuarios = repositorioUsuario.listarUsuarios();
        System.out.println(usuarios.size());
        model.addAttribute("usuarios", usuarios);

        return "/pagina_admin/portalAdmin";
	}
	
	//Este metodo trabaja conjunto con el de buscar nombre o dni, este se encarga de mostrar la pag con la tabla oculta
	// y luego cuando apretamos el boton buscar este nos conecta con la pag de buscarDNIoNombre
	@GetMapping("ocultarLista")
    public String ocultarLista(Model model) throws MiExcepcion {
        List<Usuario> usuarios = repositorioUsuario.listarUsuarios();
        model.addAttribute("usuarios", false);

        return "/pagina_admin/portalAdmin";
	}
	
	//Metodo para buscar por nombre o dni
	@PostMapping("buscarDNIoNombre")
	public String buscarDNI(@RequestParam(name = "dato") String dato, Model model) {
		List<Usuario> usuarioDni = servicioUsuario.buscarDni(dato);
		List<Usuario>	usuarioNombre = servicioUsuario.buscarNombre(dato);
		List<Usuario> usuarioEmail = servicioUsuario.buscarPorEmail(dato);
		
		if (usuarioDni.isEmpty()) {
			usuarioDni = null;
		}
		
		if (usuarioNombre.isEmpty()) {
			usuarioNombre = null;
		}
		
		if (usuarioEmail.isEmpty()) {
			usuarioEmail = null;
		}
		
		if (usuarioDni != null) {
			model.addAttribute("usuarios", usuarioDni); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento 
		}
		
		if (usuarioNombre != null) {
			model.addAttribute("usuarios", usuarioNombre);
		}
		
		if (usuarioEmail != null) {
			model.addAttribute("usuarios", usuarioEmail);
		}
		
		return "/pagina_admin/portalAdmin";
	}
	
	// En este metodo unificamos la edicion y eliminacion de un usuario a traves de un solo formulario
	// usanso el action como valor para las diferentes condiciones
	@PostMapping("modificarUsuario")
	public String modificarUsuario(@RequestParam(name = "seleccionados") String id,
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
			servicioUsuario.modificarRol(id, nuevoRol);
		}
		
		if (action.equals("altaUsuario")) {
			try {
				//metodo para modificar el activo del usuario a True
				servicioUsuario.altaUsuario(id);
				System.out.println("alta admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
		}
		
		if (action.equals("bajaUsuario")) {
			try {
				//metodo para modificar el activo del usuario a False
				servicioUsuario.bajaUsuario(id);
				System.out.println("baja admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
			
		}
			
		if (action.equals("eliminarUsuario")) {
			//Meotod para eliminar a un usuario de la base de datos
			try {
				servicioUsuario.borrarUsuario(id);
				System.out.println("Eliminado Admin con exito!!!");
			} catch (Exception e) {
				System.out.println("No se actualizo....");
				e.printStackTrace();
			}
		}
		
		return "/pagina_admin/portalAdmin";
	}
	
	
	
	
}
