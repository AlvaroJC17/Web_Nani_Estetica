package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Profesional;
import com.proyecto_integrador_3.Estetica.Enums.Especialidad;
import com.proyecto_integrador_3.Estetica.Enums.Provincias;
import com.proyecto_integrador_3.Estetica.Enums.Rol;
import com.proyecto_integrador_3.Estetica.Enums.TipoDeEspecialidad;


@Repository
public interface RepositorioProfesional extends JpaRepository<Profesional, String> {

   Optional <Profesional> findById(String id);
   
   List<Profesional>findByProvincia(Provincias provincia);
    
   List<Profesional> findByRolAndProvinciaAndEspecialidadAndActivo(Rol rol, Provincias provincia, Especialidad especialidad, Boolean activo);
   
   List<Profesional> findByRolAndProvinciaAndTipoEspecialidadAndActivo(Rol rol, Provincias provincia, TipoDeEspecialidad tipoEspecialidad, Boolean activo);
   
 
   
   
}
