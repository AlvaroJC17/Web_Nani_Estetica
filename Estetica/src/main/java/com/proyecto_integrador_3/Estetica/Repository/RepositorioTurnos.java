package com.proyecto_integrador_3.Estetica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Cliente;
import com.proyecto_integrador_3.Estetica.Entidades.Turnos;

@Repository
public interface RepositorioTurnos extends JpaRepository<Turnos, String> {

}
