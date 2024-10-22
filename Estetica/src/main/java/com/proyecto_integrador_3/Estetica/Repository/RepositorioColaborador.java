package com.proyecto_integrador_3.Estetica.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_integrador_3.Estetica.Entidades.Colaborador;

@Repository
public interface RepositorioColaborador extends JpaRepository<Colaborador, String> {
	
	Optional <Colaborador> findOptionalById (String idColaborador);
	
	Optional <Colaborador> findByEmail (String emailColaborador);
	
	//List<Colaborador> findByIdColaborador (String idColaborador);
	
	//List<Colaborador> findByColaboradorEmail (String emailColaborador);

}
