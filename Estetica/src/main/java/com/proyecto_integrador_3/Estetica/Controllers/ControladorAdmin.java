package com.proyecto_integrador_3.Estetica.Controllers;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.Sexo;
import com.proyecto_integrador_3.Estetica.Repository.RepositorioAdmin;
import com.proyecto_integrador_3.Estetica.Servicios.ServicioAdmin;

@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

	@Autowired
	public RepositorioAdmin repositorioAdmin;
	@Autowired
	public ServicioAdmin servicioAdmin;
	
	@PostMapping("/ingresar")
	public String ingresarAdmin(@RequestParam(value="dni") String dni,@RequestParam(value="emial") String email, @RequestParam(value="rol") Rol rol,@RequestParam(value="activo") Boolean activo,
			@RequestParam(value="nombre") String nombre, @RequestParam(value="apellido") String apellido,
			@RequestParam(value="sexo") Sexo sexo,@RequestParam(value="fecha_nacimiento") Date fechaNacimiento,
			@RequestParam(value="domicilio") String domicilio, @RequestParam(value="telefono") Integer telefono) throws SQLException {
		
			try {
				servicioAdmin.registrarAdmin(dni, email, rol, activo, nombre, apellido, sexo, fechaNacimiento, domicilio, telefono);
				System.out.println("Agregado con exito");
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("no se agrego");
			}
		
		
		return "login";
	}
	
	
}
