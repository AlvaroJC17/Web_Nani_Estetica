package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;
import com.proyecto_integrador_3.Estetica.Entidades.Tratamiento;

@Repository
public interface RepositorioTratamiento extends JpaRepository<Tratamiento, String> {

}
