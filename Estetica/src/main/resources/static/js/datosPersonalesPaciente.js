//Script para modal de exito
$(document).ready(function () {
  if (showModalExito) {
    $('#exitoModal').modal('show');
  }

  $('#exitoModal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
});

//Script para modal de error
$(document).ready(function () {
  if (showModalError) {
    $('#errorModal').modal('show');
  }

  $('#errorModal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
});

//Scrip para habilitar y deshabilitar elementos
//Script para habilitar y deshabilitar las notas del profesional y su boton de guardar
document.getElementById("botonNotas").addEventListener("click", function () {
  var campoTexto = document.getElementById("notas_profesional");
  var guardarButton = document.getElementById("guardarButton");

  // Alterna la propiedad 'disabled' del campo de texto
  campoTexto.disabled = !campoTexto.disabled;

  // Alterna la propiedad 'disabled' del botón 'Guardar'
  guardarButton.disabled = !guardarButton.disabled;

  // Agrega o quita la clase según el estado de deshabilitación
  if (guardarButton.disabled) {
    guardarButton.classList.add("enviar");
  } else {
    guardarButton.classList.remove("enviar");
  }
});

//Contador de caracteres notas profesional
document.addEventListener("DOMContentLoaded", function () {
  const maxCaracteresPiel = 500;
  const textareaPiel = document.getElementById('cuidado_de_piel');
  const contadorPiel = document.getElementById('contadorCuidadoPiel');

  textareaPiel.addEventListener('input', function () {
    const caracteresRestantesPiel = maxCaracteresPiel - textareaPiel.value.length;
    contadorPiel.textContent = `Máximo de caracteres ${caracteresRestantesPiel}/500`;
  });
});


//Contador de caracteres notas profesional
document.addEventListener("DOMContentLoaded", function () {
  const maxCaracteres = 500;
  const textarea = document.getElementById('notas_profesional');
  const contador = document.getElementById('contador');

  textarea.addEventListener('input', function () {
    const caracteresRestantes = maxCaracteres - textarea.value.length;
    contador.textContent = `Máximo de caracteres ${caracteresRestantes}/500`;
  });
});

//Contador de caracteres turno 0
var tunoEstaPresente0 = document.getElementById("consultaTurno0");
if (tunoEstaPresente0) {

  document.addEventListener("DOMContentLoaded", function () {
    const maxCaracteresTurno0 = 500;
    const textareaTurno0 = document.getElementById('consultaTurno0');
    const contadorTurno0 = document.getElementById('contadorTurno0');

    textareaTurno0.addEventListener('input', function () {
      const caracteresRestantesTurno0 = maxCaracteresTurno0 - textareaTurno0.value.length;
      contadorTurno0.textContent = `Máximo de caracteres ${caracteresRestantesTurno0}/500`;
    });
  });
} else {
  console.log("El contador 0 no esta presente");
}

//Contador de caracteres turno 1
var tunoEstaPresente1 = document.getElementById("consultaTurno1");
if (tunoEstaPresente1) {

  document.addEventListener("DOMContentLoaded", function () {
    const maxCaracteresTurno1 = 500;
    const textareaTurno1 = document.getElementById('consultaTurno1');
    const contadorTurno1 = document.getElementById('contadorTurno1');

    textareaTurno1.addEventListener('input', function () {
      const caracteresRestantesTurno1 = maxCaracteresTurno1 - textareaTurno1.value.length;
      contadorTurno1.textContent = `Máximo de caracteres ${caracteresRestantesTurno1}/500`;
    });
  });
} else {
  console.log("El contador 1 no esta presente");
}

//Contador de caracteres turno 2
var tunoEstaPresente2 = document.getElementById("consultaTurno2");

if (tunoEstaPresente2) {
  document.addEventListener("DOMContentLoaded", function () {
    const maxCaracteresTurno2 = 500;
    const textareaTurno2 = document.getElementById('consultaTurno2');
    const contadorTurno2 = document.getElementById('contadorTurno2');

    textareaTurno2.addEventListener('input', function () {
      const caracteresRestantesTurno2 = maxCaracteresTurno2 - textareaTurno2.value.length;
      contadorTurno2.textContent = `Máximo de caracteres ${caracteresRestantesTurno2}/500`;
    });
  });
} else {
  console.log("El contador 2 no esta presente");
}

// Script para habilitar y deshabilitar el campo de recomendaciones y su boton guardar del primer turno de la columna
var tunoEstaPresenteOne = document.getElementById("toggleButton0");
if (tunoEstaPresenteOne) {

  document.getElementById("toggleButton0").addEventListener("click", function () {
    var campoTexto0 = document.getElementById("consultaTurno0");
    var guardarButton0 = document.getElementById("guardarButton0");

    // Alterna la propiedad 'disabled' del campo de texto
    campoTexto0.disabled = !campoTexto0.disabled;

    // Alterna la propiedad 'disabled' del botón 'Guardar'
    guardarButton0.disabled = !guardarButton0.disabled;

    // Agrega o quita la clase según el estado de deshabilitación
    if (guardarButton0.disabled) {
      guardarButton0.classList.add("enviar");
    } else {
      guardarButton0.classList.remove("enviar");
    }
  });
} else {
  console.log("Elemento no presente");
}

// Script para habilitar y deshabilitar el campo de recomendaciones y su boton guardar del segundo turno de la columna 
var turnoEstaPresenteTwo = document.getElementById("toggleButton1");

if (turnoEstaPresenteTwo) {

  document.getElementById("toggleButton1").addEventListener("click", function () {
    var campoTexto1 = document.getElementById("consultaTurno1");
    var guardarButton1 = document.getElementById("guardarButton1");

    // Alterna la propiedad 'disabled' del campo de texto
    campoTexto1.disabled = !campoTexto1.disabled;

    // Alterna la propiedad 'disabled' del botón 'Guardar'
    guardarButton1.disabled = !guardarButton1.disabled;

    // Agrega o quita la clase según el estado de deshabilitación
    if (guardarButton1.disabled) {
      guardarButton1.classList.add("enviar");
    } else {
      guardarButton1.classList.remove("enviar");
    }
  });
} else {
  console.log("Elemento dos no presente");
}

// Script para habilitar y deshabilitar el campo de recomendaciones y su boton guardar del ultimo turno de la columna  
var turnoEstaPresenteThree = document.getElementById("toggleButton2");

if (turnoEstaPresenteThree) {

  document.getElementById("toggleButton2").addEventListener("click", function () {
    var campoTexto2 = document.getElementById("consultaTurno2");
    var guardarButton2 = document.getElementById("guardarButton2");

    // Alterna la propiedad 'disabled' del campo de texto
    campoTexto2.disabled = !campoTexto2.disabled;

    // Alterna la propiedad 'disabled' del botón 'Guardar'
    guardarButton2.disabled = !guardarButton2.disabled;

    // Agrega o quita la clase según el estado de deshabilitación
    if (guardarButton2.disabled) {
      guardarButton2.classList.add("enviar");
    } else {
      guardarButton2.classList.remove("enviar");
    }
  });

} else {
  console.log("Elemento tres no esta presente");
}

//script para deshabilitar el formulario de datos cuando se ejecute el submit

document.addEventListener("DOMContentLoaded", function () {
  var formualarioDatos = document.getElementById('formualarioDatos');
  var botonGuardar = document.getElementById('botonGuardar');

  formualarioDatos.addEventListener('submit', function (event) {
    // Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
    setTimeout(function () {
      botonGuardar.disabled = true;
      botonGuardar.classList.add("enviar");
      // Rehabilitar el botón después de 5 segundos
      setTimeout(function () {
        botonGuardar.disabled = false;
        botonGuardar.classList.remove("enviar");
      }, 5000);
    }, 0);
  });
});

//script para deshabilitar el boton del formulario de notas del profesional cuando se ejecute el submit
document.addEventListener("DOMContentLoaded", function () {
  var formularioNotas = document.getElementById('formularioNotas');
  var guardarButton = document.getElementById('guardarButton');

  formularioNotas.addEventListener('submit', function (event) {
    // Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
    setTimeout(function () {
      guardarButton.disabled = true;
      guardarButton.classList.add("enviar");
      // Rehabilitar el botón después de 5 segundos
      setTimeout(function () {
        guardarButton.disabled = false;
        guardarButton.classList.remove("enviar");
      }, 5000);
    }, 0);
  });
});

//script para deshabilitar el boton guardar del formulario del turno0 cuando se ejecute el submit
var turnoPresente0 = document.getElementById("guardarButton0");
if (turnoPresente0) {
      document.addEventListener("DOMContentLoaded", function () {
        var formularioTurnos0 = document.getElementById('formularioTurnos0');
        var buttonSubmitTurnos0 = document.getElementById('guardarButton0');

        formularioTurnos0.addEventListener('submit', function (event) {
          // Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
          setTimeout(function () {
            buttonSubmitTurnos0.disabled = true;
            buttonSubmitTurnos0.classList.add("enviar");
            // Rehabilitar el botón después de 5 segundos
            setTimeout(function () {
              buttonSubmitTurnos0.disabled = false;
              buttonSubmitTurnos0.classList.remove("enviar");
            }, 5000);
          }, 0);
        });
      });
    } else {
      console.log("turno 0 para deshabilitar no encontrado");
    }

//script para deshabilitar el boton guardar del formulario del turno1 cuando se ejecute el submit
var turnoPresente1 = document.getElementById("guardarButton1");
    if (turnoPresente1) {
      document.addEventListener("DOMContentLoaded", function () {
        var formularioTurnos1 = document.getElementById('formularioTurnos1');
        var buttonSubmitTurnos1 = document.getElementById('guardarButton1');

        formularioTurnos1.addEventListener('submit', function (event) {
          // Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
          setTimeout(function () {
            buttonSubmitTurnos1.disabled = true;
            buttonSubmitTurnos1.classList.add("enviar");
            // Rehabilitar el botón después de 5 segundos
            setTimeout(function () {
              buttonSubmitTurnos1.disabled = false;
              buttonSubmitTurnos1.classList.remove("enviar");
            }, 5000);
          }, 0);
        });
      });
    } else {
      console.log("turno 1 para deshabilitar no encontrado");
    }

//script para deshabilitar el boton guardar del formulario del turno2 cuando se ejecute el submit
var turnoPresente2 = document.getElementById("guardarButton2");
    if (turnoPresente2) {
      document.addEventListener("DOMContentLoaded", function () {
        var formularioTurnos2 = document.getElementById('formularioTurnos2');
        var buttonSubmitTurnos2 = document.getElementById('guardarButton2');

        formularioTurnos2.addEventListener('submit', function (event) {
          // Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
          setTimeout(function () {
            buttonSubmitTurnos2.disabled = true;
            buttonSubmitTurnos2.classList.add("enviar");
            // Rehabilitar el botón después de 5 segundos
            setTimeout(function () {
              buttonSubmitTurnos2.disabled = false;
              buttonSubmitTurnos2.classList.remove("enviar");
            }, 5000);
          }, 0);
        });
      });
    } else {
      console.log("turno 2 para deshabilitar no encontrado");
    }

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
	
		  
