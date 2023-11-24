package com.proyecto_integrador_3.Estetica.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Usuario;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.MiExcepcion.MiExcepcion;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioUsuario;

import jakarta.transaction.Transactional;

@Service
public class ServicioUsuario {

	  @Autowired
	    public RepositorioUsuario repositorioUsuario;
	  
	  @Transactional
	    public void guardarUsuario(String email, String password, String password2) throws MiExcepcion {

	        verificarEmail(email);
	        verificarPassword(password, password2);
	        Usuario usuario = new Usuario();
	        usuario.setEmail(email);
	        usuario.setContrasena(password);
	        usuario.setActivo(TRUE);
	        usuario.setRol(Rol.CLIENTE);
	        repositorioUsuario.save(usuario);
	    }
	  
	  @Transactional
	    public void modificarContrasena(String email, String password, String password2, String id) throws MiExcepcion {
	        verificarPassword(password, password2);

	        Optional<Usuario> presente = repositorioUsuario.findById(id);

	        if (presente.isPresent()) {
	            Usuario usuario = presente.get();
	            usuario.setContrasena(password);
	            repositorioUsuario.save(usuario);
	        }

	    }
	  
	  @Transactional()
	    public List<Usuario> listarAdmins() {
		  List<Usuario> usuario = new ArrayList();
		  usuario = repositorioUsuario.listarUsuarios();
		  return usuario;
	  }


		@Transactional
		public List<Usuario> buscarDni(String dni) {
			List<Usuario> dniUsuario = repositorioUsuario.buscarPorDni(dni);
		return dniUsuario;
		}
		
		@Transactional
		public List<Usuario> buscarNombre(String nombre) {
			List<Usuario> nombreUsuario = repositorioUsuario.buscarPorNombre(nombre);
		return nombreUsuario;
		}
		
		@Transactional
		public List<Usuario> buscarPorEmail(String email) {
			List<Usuario> emailUsuario = repositorioUsuario.buscarPorEmail(email);
		return emailUsuario;
		}

		//Borra un admin de la base de datos
		public void borrarUsuario(String id) {
			Optional <Usuario> identificarUsuario = repositorioUsuario.findById(id);
			
			if (identificarUsuario.isPresent()) {
				Usuario usuarioDelete = identificarUsuario.get();
				repositorioUsuario.delete(usuarioDelete);
				
			}
		}
		
		 @Transactional
		    public void bajaUsuario(String id) {
		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_baja = new Usuario();
		        	usuario_baja = presente.get();
		        	usuario_baja.setActivo(FALSE); //El valor false se importa de un clase propia de java
		        	repositorioUsuario.save(usuario_baja);
		        }
		    }
		 
		 //Metodo para cambiar a usuarios que estaban inactivos a activos
		 @Transactional
		    public void altaUsuario(String id) {

		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_alta = new Usuario();
		        	usuario_alta = presente.get();
		        	usuario_alta.setActivo(TRUE);
		        	repositorioUsuario.save(usuario_alta);
		        }
		    }
		 
		 @Transactional
		    public void modificarRol(String id, Rol rol) {

		        Optional<Usuario> presente = repositorioUsuario.findById(id);

		        if (presente.isPresent()) {
		        	Usuario usuario_rol = new Admin();
		        	usuario_rol = presente.get();
		        	usuario_rol.setRol(rol);
		        	repositorioUsuario.save(usuario_rol);
		        }
		    }
		 
		

	  
	  //VALIDACIONES
	  public void verificarEmail(String email) throws MiExcepcion {
	        if (email.isEmpty()) {
	            throw new MiExcepcion("El email no puede estar vacío");
	        }
	        if (repositorioUsuario.buscarPorEmailOptional(email).isPresent()) {
	            throw new MiExcepcion("El email ya está registrado");
	        }

	    }

	    public boolean verificarPassword(String password, String password2) throws MiExcepcion {
	        if (password.isEmpty()) {
	            throw new MiExcepcion("La constraseña no puede estar vacía");
	        }
	        if (password.trim().isEmpty()) {
	            throw new MiExcepcion("La contraseña no puede estar vacía");
	        }
	        if (!password.equals(password2)) {
	            throw new MiExcepcion("Las contraseñas no coinciden");
	        }
	        return true;
	    }
}
