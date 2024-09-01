document.getElementById("botonEditar").addEventListener("click", function() {
	  	    var campoFuma = document.getElementById("fuma");
	  	    var campoDrogas = document.getElementById("drogas");
	  		var campoAlcohol = document.getElementById("alcohol");
			var campoDeportes = document.getElementById("deportes");
			var campoEjercicios = document.getElementById("ejercicios");
			var campoCicloMenstrual = document.getElementById("ciclo_menstrual");
			var campoMedicamentos = document.getElementById("medicamentos");
			var campoNombreMedicamento = document.getElementById("nombreMedicamento");
			var campoEmbarazo = document.getElementById("embarazo");
			var campoAmamantendo = document.getElementById("amamantando");
			var campoHormonal = document.getElementById("alteracion_hormonal");
			var campoVitaminas = document.getElementById("vitaminas");
			var campoCorticoides = document.getElementById("corticoides");
			var campoHormonas = document.getElementById("hormonas");
			var campoTiroides = document.getElementById("tiroides");
			var campoAnticonceptivo = document.getElementById("metodo_anticonceptivo");
			var campoEnfermedad = document.getElementById("sufre_enfermedad");
			var campoCualEnfermedad = document.getElementById("cual_enfermedad");
			var campoOncologico = document.getElementById("paciente_oncologica");
			var campoCarbohidratos = document.getElementById("consumo_carbohidratos");
			var campoFractura = document.getElementById("fractura_facial");
			var campoImplantes = document.getElementById("tiene_implantes");
			var campoCirugia = document.getElementById("cirugia_estetica");
			var campoIndiqueCirugia = document.getElementById("indique_cirugia_estetica");
			var campoMarcaPasos = document.getElementById("marca_pasos");
			var campoSuenos = document.getElementById("horas_sueno");
			var campoExposicionSol = document.getElementById("exposicion_sol");
			var campoProtectorSolar = document.getElementById("protector_solar");
			var campoReaplicaProtector = document.getElementById("reaplica_protector");
			var campoMotivoConsulta = document.getElementById("motivo_consulta");
			var campoTratamientosFacialesAnteriores = document.getElementById("tratamientosFacialesAnteriores");
			var campoResultadoTratamientos = document.getElementById("resultados_tratamiento_anterior");
			var campoCuidadoPiel = document.getElementById("cuidado_de_piel");

	  	    // Alterna la propiedad 'disabled' del campo
	  	    campoFuma.disabled = !campoFuma.disabled;
	  	    campoDrogas.disabled = !campoDrogas.disabled;
			campoAlcohol.disabled = !campoAlcohol.disabled;
			campoDeportes.disabled = !campoDeportes.disabled;
			campoEjercicios.disabled = !campoEjercicios.disabled;
			campoCicloMenstrual.disabled = !campoCicloMenstrual.disabled;
			campoMedicamentos.disabled = !campoMedicamentos.disabled;
			campoNombreMedicamento.disabled = !campoNombreMedicamento.disabled;
			campoEmbarazo.disabled = !campoEmbarazo.disabled;
			campoAmamantendo.disabled = !campoAmamantendo.disabled;
			campoHormonal.disabled = !campoHormonal.disabled;
			campoVitaminas.disabled = !campoVitaminas.disabled;
			campoCorticoides.disabled = !campoCorticoides.disabled;
			campoHormonas.disabled = !campoHormonas.disabled;
			campoTiroides.disabled = !campoTiroides.disabled;
			campoAnticonceptivo.disabled = !campoAnticonceptivo.disabled;
			campoEnfermedad.disabled = !campoEnfermedad.disabled;
			campoCualEnfermedad.disabled = !campoCualEnfermedad.disabled;
			campoOncologico.disabled = !campoOncologico.disabled;
			campoCarbohidratos.disabled = !campoCarbohidratos.disabled;
			campoFractura.disabled = !campoFractura.disabled;
			campoImplantes.disabled = !campoImplantes.disabled;
			campoCirugia.disabled = !campoCirugia.disabled;
			campoIndiqueCirugia.disabled = !campoIndiqueCirugia.disabled;
			campoMarcaPasos.disabled = !campoMarcaPasos.disabled;
			campoSuenos.disabled = !campoSuenos.disabled;
			campoExposicionSol.disabled = !campoExposicionSol.disabled;
			campoProtectorSolar.disabled = !campoProtectorSolar.disabled;
			campoReaplicaProtector.disabled = !campoReaplicaProtector.disabled;
			campoMotivoConsulta.disabled = !campoMotivoConsulta.disabled;
			campoTratamientosFacialesAnteriores.disabled = !campoTratamientosFacialesAnteriores.disabled;
			campoResultadoTratamientos.disabled = !campoResultadoTratamientos.disabled;
			campoCuidadoPiel.disabled = !campoCuidadoPiel.disabled;
	  	    
			botonGuardar.disabled = !botonGuardar.disabled;
	  		
	  		// Agrega o quita la clase según el estado de deshabilitación
	  		if (botonGuardar.disabled) {
	  		  botonGuardar.classList.add("enviar");
	  		} else {
	  		  botonGuardar.classList.remove("enviar");
	  		}
	  	  });
	
		  
