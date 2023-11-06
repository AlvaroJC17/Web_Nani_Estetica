package com.proyecto_integrador_3.Estetica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Admin;

@Repository
public interface RepositorioAdmin extends JpaRepository<Admin, String> {

}
