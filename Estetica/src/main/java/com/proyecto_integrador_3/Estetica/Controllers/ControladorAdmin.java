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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioCliente;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
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
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public ServicioPersona servicioPersona;
	
	@Autowired
	public ServicioCliente servicioCliente;
	
	
	@GetMapping("/portalAdmin")
	public String portalAdmin() {
	return "/pagina_admin/portalAdmin";	
	}
	
	
	
	@GetMapping("/cambiarContrasenaAdmin")
	public String cambiarContrasenaAdmin() {
	return "/pagina_admin/cambiarContrasenaAdmin";	
	}
	
	
/*	@PostMapping("/modificarAdmin")
	public String actualizarAdmin(@RequestBody Admin admin) {
		
		try {
			servicioAdmin.modificarAdmin(admin);
			System.out.println("Actualizado con exito!!!");
		} catch (Exception e) {
			System.out.println("No se actualizo....");
			e.printStackTrace();
		}
		
		return "index";
	}*/
	
	@GetMapping("/misdatosAdmin")
	public String misdatosAdmin(@RequestParam(name = "email") String email, ModelMap model) {
		List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosPersonaUsuario);
		return "/pagina_admin/misdatosAdmin";	
	}
	
	@GetMapping("/homeAdmin")
	public String homeAdmin(@RequestParam(name = "email") String email, ModelMap model) {
		List <Usuario> datosAdmin = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosAdmin);
		return "/pagina_admin/homeAdmin";	
	}
			
	
	//A este metodo le paso la variable mail y la lista por dos model diferentes para que cuando cargue
	//la pagina se pueda visualizar el nav y la lista de usuarios, sino le paso el mail el nav no se ve
	@GetMapping("/listarUsuariosVisibles")
    public String listarUsuariosVisibles(@RequestParam(name = "email") String email, ModelMap model) throws MiExcepcion {
		List<Usuario> usuarios = repositorioUsuario.joinUsuarioPersona();
		model.addAttribute("usuariosEmail", email);
		model.addAttribute("usuarios", usuarios);
		return "/pagina_admin/portalAdmin";
	}
	
	//Muestra una pagina con la lista de usuarios oculta, esto porque queremos que el admin
	//busque los usuarios por nombre, dni o email y no que los busque en una lista
	@GetMapping("/listarUsuariosOcultos")
    public String listarUsuarios(
    		@RequestParam(name = "email") String email,
    		@RequestParam(name = "exito", required = false) String exito, //El requiered=false le indica al metodo que el valor de esta variable es opcional y puede llegar null, asi no se rempe el programa
    		@RequestParam(name = "error", required = false) String error,
    		Model model) throws MiExcepcion {

		//List<Usuario> usuariosEmail = repositorioUsuario.buscarPorEmail(email);
		model.addAttribute("usuariosEmail", email); //Con este mail mostramos el nav en este metodo
		model.addAttribute("exito", exito); //inyectamos mensaje de exito si es necesario
		model.addAttribute("error", error); //inyectamos mensaje de error si es necesario
		return "/pagina_admin/portalAdmin";
	}
	
	//Buscamos usuario por dni, nombre o email, la variable emailAdmin es para pasar el mail de admin y poder visualizar el nav
	@PostMapping("/buscarDNIoNombre")
	public String buscarDniNombreEmail(@RequestParam(name = "dato") String dato, @RequestParam(name = "emailAdmin") String emailAdmin, Model model) {

		//Validamos que la busqueda no se haya hecho en blanco y mostramos un mensaje
		if (dato.isEmpty() || dato == null) {
			model.addAttribute("error", "Indique un numero de DNI, nombre o email para realizar la busqueda");
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}
					
		List<Usuario> usuarioDni = servicioUsuario.buscarDni(dato);
		List<Usuario>	usuarioNombre = servicioUsuario.buscarNombre(dato);
		List<Usuario> usuarioEmail = servicioUsuario.buscarPorEmail(dato);
		
		if (!usuarioDni.isEmpty()) {
			model.addAttribute("usuarios", usuarioDni); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}	
		if (!usuarioNombre.isEmpty()) {
			model.addAttribute("usuarios", usuarioNombre);
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}	
		if (!usuarioEmail.isEmpty()) {
			model.addAttribute("usuarios", usuarioEmail);
			model.addAttribute("usuariosEmail", emailAdmin);
			return "/pagina_admin/portalAdmin";
		}
		model.addAttribute("usuariosEmail", emailAdmin);
		return "/pagina_admin/portalAdmin";
	}
		
	
	// En este metodo unificamos la edicion y eliminacion de un usuario a traves de un solo formulario
	// usando el action como valor para las diferentes condiciones
	@PostMapping("/modificarUsuario")
	public String modificarUsuario(@RequestParam(name = "idUsuario", required = false) String id,
								   @RequestParam(name = "nuevoRol") String nuevoRolNombre,
            					   @RequestParam(name = "emailAdministrador") String emailAdministrador,
            					   @RequestParam(name = "action") String action,
            					   Model model) {
		
		//Validamos que el id no venga vacio, esto pasa cuando le dan a una opcion sin seleccionar a un usuario
		if (id == null) {
			model.addAttribute("error", "Es necesario seleccionar un usuario!!");
			model.addAttribute("usuariosEmail", emailAdministrador);
			return "/pagina_admin/portalAdmin";
		}
		
		//Validamos que la opcion rol no sea "Modifical rol" y pasamos el valor de String a Rol
		Rol nuevoRol = null;
		if (!"Modifical rol".equals(nuevoRolNombre)) {
			try {
	            // Convertir el nuevoRol solo si no es "Modificar Rol"
	            nuevoRol = Rol.valueOf(nuevoRolNombre.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            System.out.println(e.getMessage());
	        }
		}
		
		//Con este bloque de codigo, uso el id para encontrar el mail de usuario
		String emailUsuario = null;
		Optional<Usuario> datosUsuario = repositorioUsuario.findById(id);
		if (datosUsuario.isPresent()) {
			Usuario usuarioEmail = datosUsuario.get();
			emailUsuario = usuarioEmail.getEmail();
		}
			
	     //Ya teniendo el mail de usuario puedo inyectarlo en el resultado de cada if
		// para que ademas de cumplir la funcion de cada condicional tambien la pag
		// me recargue con la barra de nav. Recordar que como la barra de nav este en un
		//condicional, sino le paso un valor de mail esta no se me activo y no puedo verla
		if (action.equals("modificarRol") && nuevoRol != null) {
			//metodo para moficar el rol
			servicioUsuario.modificarRol(id, nuevoRol);
			return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailUsuario;
		}
															 
		if (action.equals("altaUsuario")) {
			try {
				servicioUsuario.altaUsuario(id);
				return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailUsuario;
				
			} catch (Exception e) {
				System.out.println("No se actualizo el alta del Usuario....");
				e.printStackTrace();
			}
		}
		
		if (action.equals("bajaUsuario")) {
			try {
				//metodo para modificar el activo del usuario a False
				servicioUsuario.bajaUsuario(id);
				return "redirect:/listarUsuariosPorId?id=" + id + "&email=" + emailUsuario;
				
			} catch (Exception e) {
				System.out.println("No se actualizo la baja del usuario....");
				e.printStackTrace();
			}
		}
						
		if (action.equals("eliminarUsuario")) {
			//Meotod para eliminar a un usuario de la base de datos
			try {
				servicioUsuario.borrarUsuario(id);
				servicioPersona.borrarPersona(id);
				String exito = "Usuario eliminado correctamente";
				return "redirect:/listarUsuariosOcultos?email=" + emailUsuario + "&exito=" + exito; //Enviamos mail y mensaje de exito
			} catch (Exception e) {
				String error = "Usuario no se pudo eliminar";
				return "redirect:/listarUsuariosOcultos?email=" + emailUsuario + "&error=" + error; //Enviamos mail y mensaje de error;
			}
		}
		return "/pagina_admin/portalAdmin";
	}
		
	
	//Lista usuarios y persona haciendo un join de tablas por ID
	//Este metodo esta relacionado con el de modificarUsuario
	@GetMapping("/listarUsuariosPorId")
	public String listarUsuariosPorId(String id, String email, Model model) {
		List<Usuario> usuarios = servicioUsuario.buscarId(id);
		model.addAttribute("usuariosEmail", email);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("exito", "Modificación realizada exitosamente!!");
		return "/pagina_admin/portalAdmin";
	}
	
		
	
	
		
}
		
		
			
		
		
	
	
