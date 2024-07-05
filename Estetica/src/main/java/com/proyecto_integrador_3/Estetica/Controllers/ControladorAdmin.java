package com.proyecto_integrador_3.Estetica.Controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Persona;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioPersona;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioPersona;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioUsuario;


@Controller
//@RestController
public class ControladorAdmin {


	@Autowired
	public RepositorioAdmin repositorioAdmin;
	
	@Autowired
	public RepositorioUsuario repositorioUsuario;
	
	@Autowired
	public RepositorioPersona repositorioPersona;
	
	@Autowired
	public ServicioAdmin servicioAdmin;
	
	@Autowired
	public ServicioUsuario servicioUsuario;
	
	@Autowired
	public ServicioPersona servicioPersona;
	
	
	@GetMapping("/portalAdmin")
	public String portalAdmin() {
	return "/pagina_admin/portalAdmin";	
	}
	
	
	@GetMapping("/misdatosAdmin")
	public String misdatosAdmin(
			@RequestParam(name = "email") String email,
			@RequestParam(name = "exito", required = false) String exito,
			@RequestParam(name = "error", required = false) String error,
			ModelMap model) {
		
		List <Usuario> datosPersonaUsuario = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosPersonaUsuario);
		model.addAttribute("exito", exito);
		model.addAttribute("error", error);
		return "/pagina_admin/misdatosAdmin";	
	}
	
	
	@GetMapping("/homeAdmin")
	public String homeAdmin(@RequestParam(name = "email") String email, ModelMap model) { //El valor de esta variable viene del metodo login en controladorPagina
		List <Usuario> datosAdmin = servicioUsuario.buscarPorEmail(email);
		model.addAttribute("datosAdmin", datosAdmin);
		return "/pagina_admin/homeAdmin";	
	}
	
	
	@PostMapping("/guardarDatosAdmin")
	public String guardarDatosAdmin(
			@RequestParam(name = "sexo") String sexo,
			@RequestParam(name = "telefono") String telefono,
			@RequestParam(name = "direccion") String direccion,
			@RequestParam(name = "ocupacion") String ocupacion,
			@RequestParam(name = "emailUsuario") String emailUsuario, //Esta valor viene del input oculto de la hoja completarDatos, que a su vez viene del meotodo Login en ControladorPagina
			ModelMap model) throws MiExcepcion {
		
		try {
			//Guardamos los datos del form que lleno el nuevo admin
			servicioAdmin.registrarAdmin(emailUsuario, ocupacion, direccion, telefono, sexo);	
		} catch (MiExcepcion e) {
			System.out.println(e.getMessage());
			model.put("error", e.getMessage());
			model.addAttribute("sexo", sexo);
			model.addAttribute("telefono", telefono);
			model.addAttribute("direccion", direccion);
			model.addAttribute("ocupacion", ocupacion);
			model.addAttribute("emailUsuario", emailUsuario);
			model.addAttribute("showModalError", true);
			return "/pagina_admin/completarDatosAdmin";
		}
		return "redirect:/homeAdmin?email=" + emailUsuario; //redirecionamos al metodo homeAdmin enviando la varibale mail
	}
			
	
	
	//A este metodo le paso la variable mail y la lista por dos model diferentes para que cuando cargue
	//la pagina se pueda visualizar el nav y la lista de usuarios, sino le paso el mail el nav no se ve
	@GetMapping("/listarUsuariosVisibles")
    public String listarUsuariosVisibles(@RequestParam(name = "email") String email, ModelMap model) throws MiExcepcion {
		List<Usuario> usuarios = repositorioUsuario.joinUsuarioPersona();
		
		//Excluye del resultado al usuario que este usando el listado
		//esto es para que no se pueda modificar a si mismo
		 usuarios = usuarios.stream()
                 .filter(usuario -> !usuario.getEmail().equals(email))
                 .collect(Collectors.toList());
		
		model.addAttribute("usuariosEmail", email);
		model.addAttribute("usuarios", usuarios);
		return "/pagina_admin/portalAdmin";
	}
	
	
	//Muestra una pagina con la lista de usuarios oculta, esto porque queremos que el admin
	//busque los usuarios por nombre, dni o email y no que los busque en una lista
	@GetMapping("/listarUsuariosOcultos")
    public String listarUsuarios(
    		@RequestParam(name = "email") String email, //Esta variable proviene de homeAdmin
    		Model model) throws MiExcepcion {

		model.addAttribute("usuariosEmail", email); //Con este mail mostramos el nav en este metodo
		return "/pagina_admin/portalAdmin";
	}
	
	
	//Buscamos usuario por dni, nombre o email, la variable emailAdmin es para pasar el mail de admin y poder visualizar el nav
	@PostMapping("/buscarDNIoNombre")
	public String buscarDniNombreEmail(
			@RequestParam(name = "dato") String dato,  //la variable dato puede ser un nombre, dni o mail
			@RequestParam(name = "emailAdmin") String emailAdmin,
			@RequestParam(name ="dato2", required = false) String dato2, //proviene del metodo modificarUsuario
			@RequestParam(name ="usuarioModificado", required = false) String usuarioModificado, //proviene del metodo modificarUsuario
			//@RequestParam(name = "activarMensaje", required = false) Boolean activarMensaje,
			Model model) {
		
		/*Dato2 lo usamos como una variable para recibir la informacion del modal de exito de portalAdmin
		 * no puedo usar le mismo nobre de dato porque me da error por lo que tuve que crear dato2, una vez
		 * recibida la variable primero verifica si es vacia, si esta vacia se ejecuta el metodo normalmente, sino
		 * esta vacia entonces le asigna el valor de dato2 a dato y se ejecuta el metodo normalmente */
		if (dato2 != null) {
			dato = dato2;
		}
		
		System.out.println("dato: " + dato);
					
		String error = null;
		String datoSinEspacios = dato.trim(); //Le quitamos los espacios en blanco al principio y final de la palabra

		//Validamos que la busqueda no se haya hecho en blanco y mostramos un mensaje
		if (datoSinEspacios.isEmpty() || Objects.equals(datoSinEspacios, null)) {
			error = "Indique un numero de DNI, nombre o email para realizar la busqueda";
			model.addAttribute("usuariosEmail", emailAdmin);
			model.addAttribute("error", error);
			model.addAttribute("showModalError", true);
			return "/pagina_admin/portalAdmin";
		}
		
		//Validamos que el usuario buscado solo por dni o emial exista en la base de datos, sino mandamos un mensaje de error.
		//Solo validamos email y dni porque si resultado siempre va a ser un unico registro y el metodo isPresent solo acepta un solo registro como resultado
		if (!servicioUsuario.buscarPorDniOptional(datoSinEspacios).isPresent() &&  !servicioUsuario.buscarPorEmailOptional(datoSinEspacios).isPresent() && servicioUsuario.buscarNombre(datoSinEspacios).isEmpty()) {
			error = "Usuario no se encuentra registrado";
			model.addAttribute("usuariosEmail", emailAdmin);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_admin/portalAdmin";
		}
		
		// Si cumple las condiciones para pasar los condicionales de arriba, entonces
		//usamos el valor de dato, segun corresponda
		List<Usuario> usuarioDni = servicioUsuario.buscarDni(datoSinEspacios); //buscamos la lista de usuarios por dni, con esto verificamos que el usuario existe
		List<Usuario>	usuarioNombre = servicioUsuario.buscarNombre(datoSinEspacios); //Buscamos usuario por nombre
		List<Usuario> usuarioEmail = servicioUsuario.buscarPorEmail(datoSinEspacios); //buscamos al usuario por mail, no es necesario tambien buscar por Persona porque el email esta en la tabla usuario
		
		//Si la lista no esta vacia (usuario existe) y el dato que ingreso no es igual a el del mismo usuario entonces entra en el condicional
		//con esto evitamos que el usuario se busque a si mismo y se modifique a si mismo
		if (!usuarioDni.isEmpty()) {
			usuarioDni = usuarioDni.stream()
	                 .filter(usuario -> !usuario.getEmail().equals(emailAdmin)) //Filtramos del resultados de las listas al usuario que esta haciendo la busqueda, para que no pueda encontrarse a si mismo
	                 .collect(Collectors.toList());
			model.addAttribute("usuarios", usuarioDni); // asignamos el valor de la variable administradoresDni a la variable html administradores y asi poder iterarla en el documento
			model.addAttribute("usuariosEmail", emailAdmin);
			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
			return "/pagina_admin/portalAdmin";
		}	
			
		//Igual que el anterior
		if (!usuarioNombre.isEmpty()) {
			usuarioNombre = usuarioNombre.stream()
	                 .filter(usuario -> !usuario.getEmail().equals(emailAdmin)) //Filtramos del resultados de las listas al usuario que esta haciendo la busqueda, para que no pueda encontrarse a si mismo
	                 .collect(Collectors.toList());
			model.addAttribute("usuarios", usuarioNombre);
			model.addAttribute("usuariosEmail", emailAdmin);
			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
			return "/pagina_admin/portalAdmin";
		}	

		//Igual que el anterior
		/*Le enviamos el valor de dato2 al modal de exito. Solo lo colocamos en este pedazo de codigo porque solo estamos
		 * usando el mail de los usuario como dato2*/
		if (!usuarioEmail.isEmpty()) {
			usuarioEmail = usuarioEmail.stream()
	                 .filter(usuario -> !usuario.getEmail().equals(emailAdmin)) //Filtramos del resultados de las listas al usuario que esta haciendo la busqueda, para que no pueda encontrarse a si mismo
	                 .collect(Collectors.toList());
			model.addAttribute("usuarios", usuarioEmail);
			model.addAttribute("usuariosEmail", emailAdmin);
			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
			model.addAttribute("dato2", datoSinEspacios);  
			/*Esta variable dato2 se usa en la segunda vuelta del metodo, osea despues de que un usuario
			 * busca a la persona y le sale el noombre con los datos, este selecciona una opcion de alta, baja,
			 * elimiar o rol, pero si no selecciona un usario con el check entonces se usa esta variable para
			 * activar el modal con el mensaje de advertencia*/
			return "/pagina_admin/portalAdmin";
		}			
		
//		System.out.println("antes de entrar: " + usuarioModificado);
//		if (!usuarioEmail.isEmpty() && usuarioModificado.equalsIgnoreCase("usuarioModificado") ) {
//			System.out.println("Esta entrando aqui: " + usuarioModificado);
//			model.addAttribute("usuarios", usuarioEmail);
//			model.addAttribute("usuariosEmail", emailAdmin);
//			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
//			model.addAttribute("dato2", datoSinEspacios);
//			return "/pagina_admin/portalAdmin";
//		}
			model.addAttribute("usuariosEmail", emailAdmin);
			model.addAttribute("dato2", datoSinEspacios);
			model.addAttribute("dato", dato); // le pasamos el valor de dato a la variable dato del formulario modificarUsuario, para despues usarla en el metodo mensajeErrorNoId
			return "/pagina_admin/portalAdmin";
	}
			
	/*Este metodo es para cuando el usuario no selecciona ningun usuario con el check,
	 * entonces se muestre un modal de advertencia y al cerrarse quede sobre la misma pagina
	 * donde estan los datos del usuario encontrado y asi no tener que volver a buscarlo en el buscador*/
	@GetMapping("/mensajeErrorNoID")
	public String mensajeErrorNoID(
			@RequestParam(name = "emailAdmin", required = false) String emailAdministrador,
			@RequestParam(name = "datoIngresadoUsuario") String datoIngresadoUsuario,
			Model model){
		
		List<Usuario> usuarioDni = servicioUsuario.buscarDni(datoIngresadoUsuario);
		List<Usuario>	usuarioNombre = servicioUsuario.buscarNombre(datoIngresadoUsuario);
		List<Usuario> usuarioEmail = servicioUsuario.buscarPorEmail(datoIngresadoUsuario);
		
		String error = "Es necesario seleccionar un usuario";
		model.addAttribute("usuariosEmail", emailAdministrador); // sirve para mostrar el nav
		model.addAttribute("dato2", datoIngresadoUsuario); //Esta variable es para el modal
		model.addAttribute("dato", datoIngresadoUsuario); // esta variable dato es para el hiden imput del formulario modificarUsuario 
		model.addAttribute("error", error);
		model.addAttribute("showModalError", true);
		if (!usuarioDni.isEmpty()) {
			model.addAttribute("usuarios", usuarioDni); // Esta variable hace la iteracion de los usuarios a mostrar
			return "/pagina_admin/portalAdmin";	
		}else if(!usuarioNombre.isEmpty()) {
			model.addAttribute("usuarios", usuarioNombre);
			return "/pagina_admin/portalAdmin";
		}else if(!usuarioEmail.isEmpty()) {
			model.addAttribute("usuarios", usuarioEmail);
			return "/pagina_admin/portalAdmin";
		}
		return "/pagina_admin/portalAdmin";
	}
		
			
	// En este metodo unificamos la edicion y eliminacion de un usuario a traves de un solo formulario
	// usando el action como valor para las diferentes condiciones
	@PostMapping("/modificarUsuario")
	public String modificarUsuario(
			@RequestParam(name = "idUsuario", required = false) String id, // esta variable recibe el id del usuario seleccionado, no se pone requerido para poder manejar la excepcion
			@RequestParam(name = "nuevoRol") String nuevoRolNombre,
            @RequestParam(name = "emailAdministrador") String emailAdministrador,
            @RequestParam(name = "action") String action,
            @RequestParam(name = "usuarioEmail", required = false) String usuarioEmail,
            @RequestParam(name = "dato", required = false) String dato, //viene del formulario modificarUsuario y esta su vez viene del metodo buscarDNIoNombre
            Model model) {
		
		String exito;
		String error;
		
		//Validamos que el id no venga vacio, esto pasa cuando le dan a una opcion sin seleccionar a un usuario
		if (Objects.equals(id, null)) {
			/*Pasamos los datos del mail de usuario y mail de admin al metodo mensajeError para poder
			 * visualizar el usuario buscado y que se despliegue bien el nav y la pagina.*/
			return "redirect:/mensajeErrorNoID?emailAdmin=" + emailAdministrador + "&datoIngresadoUsuario=" + dato ;
		}
			
		
		//Validamos que la opcion rol no sea "Modifical rol" y pasamos el valor de String a Rol
		Rol nuevoRol = null;
		if (!"Modifical rol".equals(nuevoRolNombre)) {
			try {
	            // Convertir el nuevoRol solo si no es "Modificar Rol"
	            nuevoRol = Rol.valueOf(nuevoRolNombre.toUpperCase());
	        } catch (IllegalArgumentException e) {
	        	System.out.println("No se puede convertir el rol a string");
	            System.out.println(e.getMessage());
	        }
		}
					
	     //Ya teniendo el mail de usuario puedo inyectarlo en el resultado de cada if
		// para que ademas de cumplir la funcion de cada condicional tambien la pag
		// me recargue con la barra de nav. Recordar que como la barra de nav este en un
		//condicional, sino le paso un valor de mail esta no se me activo y no puedo verla
		
		
		// Buscamos el activo y el rol actual para compararlo con el que selecciono el usuario y en caso que sean iguales mostrar un mensaje de error
		Rol rolActual = null;
		Boolean activoActual = null;
		String email = null;
		Optional <Usuario> rolEmailActualUsuario = servicioUsuario.buscarPorIdOptional(id);
		if (rolEmailActualUsuario.isPresent()) {
			Usuario rolUsuarioActual = rolEmailActualUsuario.get();
			rolActual = rolUsuarioActual.getRol();
			activoActual = rolUsuarioActual.getActivo();
			email = rolUsuarioActual.getEmail();
		}
		
		List<Usuario> usuarios = servicioUsuario.buscarId(id);
		if (action.equals("modificarRol") && nuevoRol != null) {
			if (!rolActual.equals(nuevoRol)) { // si los roles son direfentes ejecuta esto
				//metodo para moficar el rol
				servicioUsuario.modificarRol(id, nuevoRol);
				//String usuarioModificado = "usuarioModificado";
				exito = "Actializacion realizada correctamente";
				model.addAttribute("usuariosEmail", emailAdministrador);
				model.addAttribute("showModalExito", true);
				model.addAttribute("usuarios", usuarios);
				model.addAttribute("exito", exito);
				model.addAttribute("dato2",email); // enviamos el mismo mail del usuario como dato para el formulario de buscarDNIoNombre
				//model.addAttribute("usuarioModificado", usuarioModificado);
				return "/pagina_admin/portalAdmin";
			}else {
				error = "El usuario ya posee un rol de " + nuevoRol;
				model.addAttribute("usuariosEmail", emailAdministrador);
				model.addAttribute("usuarios", usuarios);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_admin/portalAdmin";
			}
		}
				
															 
		if (action.equals("altaUsuario")) {
			try {
				if (!activoActual.equals(true)) {
					exito = "Alta realizada correctamente";
					servicioUsuario.altaUsuario(id);
					model.addAttribute("usuariosEmail", emailAdministrador);
					model.addAttribute("usuarios", usuarios);
					model.addAttribute("exito", exito);
					model.addAttribute("showModalExito", true);
					model.addAttribute("dato2",email);
					return "/pagina_admin/portalAdmin";
				}else {
					error = "El usuario ya se encuentra activo";
					model.addAttribute("usuariosEmail", emailAdministrador);
					model.addAttribute("usuarios", usuarios);
					model.addAttribute("error", error);
					model.addAttribute("showModalError", true);
					return "/pagina_admin/portalAdmin";
				}					
			} catch (Exception e) {
				System.out.println("No se actualizo el alta del Usuario....");
				e.printStackTrace();
			}
		}
		
		if (action.equals("bajaUsuario")) {
			try {
				//metodo para modificar el activo del usuario a False
				if (!activoActual.equals(false)) {
					exito = "Baja realizada correctamente";
					servicioUsuario.bajaUsuario(id);
					model.addAttribute("usuariosEmail", emailAdministrador);
					model.addAttribute("usuarios", usuarios);
					model.addAttribute("exito", exito);
					model.addAttribute("showModalExito", true);
					model.addAttribute("dato2",email);
					return "/pagina_admin/portalAdmin";
				}else {
					error = "El usuario ya se encuentra inactivo";
					model.addAttribute("usuariosEmail", emailAdministrador);
					model.addAttribute("usuarios", usuarios);
					model.addAttribute("error", error);
					model.addAttribute("showModalError", true);
					return "/pagina_admin/portalAdmin";
				}
				
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
				exito = "Usuario eliminado correctamente";
				model.addAttribute("usuariosEmail", emailAdministrador);
				model.addAttribute("exito", exito);
				model.addAttribute("showModalBorradoExitoso", true);
				return "/pagina_admin/portalAdmin";

			} catch (Exception e) {
				error = "Usuario no se pudo eliminar";
				model.addAttribute("usuariosEmail", emailAdministrador);
				model.addAttribute("error", error);
				model.addAttribute("showModalError", true);
				return "/pagina_admin/portalAdmin";
			}
		}
		return "/pagina_admin/portalAdmin";
	}
				
	//Metodo para que el admin puede modificar los datos permitidos
	@PostMapping("/actualizarDatosAdmin")
	public String actualizarDatosAdmin(
			@RequestParam(name="idAdmin") String idAdmin, //este atributo es enviado en un input oculto de la pag misdatosCliente
			@RequestParam(name="ocupacion", required = false) String ocupacion, // Este y los demas atributos los puse como no requeridos para poder personalizar las excepciones
		    @RequestParam(name="email", required = false) String email,
			@RequestParam(name="domicilio", required = false) String domicilio,
			@RequestParam(name="sexo", required = false) String sexo,
			@RequestParam(name="telefono", required = false) String telefono,
			ModelMap model) throws MiExcepcion {
		
		//Buscamos mediante el id el mail anterior del admin y lo guardamos en la variable emailAnterior por si acaso deja el campo de email vacio o coloca un email no valido
		// entonces usamos este mail anterior para poder pasarlo al controlador de misdatosClientes y poder visualizar los datos del cliente
		// Tambien buscamos los valores previamente guardados en la base de datos para poder compararlos con los nuevos
		String ocupacionAnterior = null;
		String emailAnterior = null;
		String domicilioAnterior = null;
		Sexo sexoAnterior = null;
		String nuevoSexo = null;
		String telefonoAnterior = null;
		
		Optional<Admin> identificarAdmin = repositorioAdmin.findById(idAdmin);
		if (identificarAdmin.isPresent()) {
			Admin datosAdminAnterior = identificarAdmin.get();
			emailAnterior = datosAdminAnterior.getEmail();
			ocupacionAnterior = datosAdminAnterior.getOcupacion();
			domicilioAnterior = datosAdminAnterior.getDomicilio();
			sexoAnterior = datosAdminAnterior.getSexo();
			nuevoSexo = sexoAnterior.toString();
			telefonoAnterior = datosAdminAnterior.getTelefono();
		}
		
		List <Usuario> datosAdmin = servicioUsuario.buscarPorEmail(email);
		//Teniendo el valos de los datos guardados y los que envian al presionar guardar en el formualario podemos comparar si se hiz alguna modificaicon
		//de los datos, si presiona guardar y no se modifico nada, recargar la misma pagina y no muestra ningun mensaje
		if (ocupacionAnterior.equals(ocupacion) && emailAnterior.equals(email) && domicilioAnterior.equals(domicilio) && nuevoSexo.equals(sexo) && telefonoAnterior.equals(telefono)) {
			model.addAttribute("email", email);
			model.addAttribute("datosAdmin",datosAdmin);
			return "/pagina_admin/misdatosAdmin";
		}
		
		try {
			//este metodo verifica valida el mail y los nuevos datos del cliente y los remplaza en la base de datos
			servicioAdmin.modificarAdmin(idAdmin, ocupacion, email, emailAnterior, domicilio, sexo, telefono );
			List <Usuario> datosAdminActualizados = servicioUsuario.buscarPorEmail(email);
			String exito = "Datos actualizados correctamente";
			model.addAttribute("datosAdmin",datosAdminActualizados);
			model.addAttribute("exito",exito);
			model.addAttribute("showModalExito", true);
			return "/pagina_admin/misdatosAdmin"; //si todo sale bien redireccionamos al metodo misdatosAdmin con el mail actualizado y un mensaje de exito 
			
		} catch (MiExcepcion e) {
			String error = e.getMessage(); // en la exepcion e.getmessage obtenenos el valor de la exepcion personalizada que se de y la enviamos al controlador de misdatosAdmin para ser monstrada en pantalla
			List <Usuario> datosAdminAnterior = servicioUsuario.buscarPorEmail(emailAnterior);
			model.addAttribute("datosAdmin",datosAdminAnterior);
			model.addAttribute("error",error);
			model.addAttribute("showModalError", true);
			return "/pagina_admin/misdatosAdmin"; // si se produce alguna exepcion en algun campo enviamos el mail anterior del usuario y un mensaje de error al metodo misdatosAdmi
		}
	}
			
	
	//metodo relacionado con actualizarContrasenaProfesional
		@GetMapping("/cambiarContrasenaAdmin")
		public String cambiarContrasenaAdmin(
				@RequestParam(name = "email") String email,
				@RequestParam(name = "exito", required = false) String exito,
				@RequestParam(name = "error", required = false) String error,
				ModelMap model) {
			
			List <Usuario> datosAdmin = servicioUsuario.buscarPorEmail(email);
			
			model.addAttribute("datosAdmin", datosAdmin);
			model.addAttribute("exito", exito);
			model.addAttribute("error", error);
			return "/pagina_admin/cambiarContrasenaAdmin";
		}
		
		
		//Metodo relacionado con cambiarContrasenaProfesional
		@PostMapping("actualizarContrasenaAdmin")
		public String actualizarContrasenaAdmin(
				@RequestParam(name = "emailAdmin") String emailAdmin, //Esta variable viene de un input oculto de la pag cambiarContrasenaProfesional
				@RequestParam(name = "idAdmin") String idAdmin, //Esta variable viene de un input oculto de la pag de la pag cambiarContrasenaCliente
				@RequestParam(name = "oldPass") String oldPass, //A partir de estas viene del formulario
				@RequestParam(name = "newPass") String newPass,
				@RequestParam(name = "repeatNewPass") String repeatNewPass,
				ModelMap model) throws MiExcepcion {
			
			String error = null;
			try {
				servicioUsuario.modificarContrasena(idAdmin,oldPass, newPass, repeatNewPass);
				return "/index";
			} catch (Exception e) {
				error = e.getMessage();
				return "redirect:/cambiarContrasenaAdmin?email=" + emailAdmin + "&error=" + error;
			}
			
		}
}

	
		
	
	
		
		
		
			
		
		
	
	
