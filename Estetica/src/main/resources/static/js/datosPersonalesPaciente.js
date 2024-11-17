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


// Script para habilitar y deshabilitar el campo de recomendaciones y para manejar los botones de modificar, guardar y cancelar del turno 0
const valoresOriginalesNotas = {};
document.addEventListener("DOMContentLoaded", function() {
    // Verifica si el botón "modificar" existe en la página
    var botonNotas = document.getElementById("botonNotas");
    if (botonNotas) {
      
        // Función para guardar los valores iniciales del textarea
        function guardarValoresOriginalesNotas() {
            const notas_profesional = document.getElementById("notas_profesional");
            if (notas_profesional) {
                valoresOriginalesNotas[notas_profesional.id] = notas_profesional.value; // Guarda el valor inicial del textarea
            }
        }

        // Ejecuta la función de guardado de valores originales al cargar la página
        guardarValoresOriginalesNotas();
    }
});

// Función para restaurar los valores originales
function restaurarValoresNotas() {
    const notas_profesional = document.getElementById("notas_profesional");
    if (notas_profesional) {
        notas_profesional.value = valoresOriginalesNotas[notas_profesional.id]; // Restaura el valor inicial del textarea
    }
}

// Script para habilitar el textarea de notas del profesional
document.getElementById("botonNotas").addEventListener("click", function() { 
    var notas_profesional = document.getElementById("notas_profesional");
    if (notas_profesional) {
        notas_profesional.disabled = false; // Habilita el textarea
    }
});

// Función para mostrar los botones de Guardar y Cancelar y ocultar el de Modificar
function habilitarEdicionNotas() {

    document.getElementById("botonGuardarNotas").style.display = "inline-block"; //muestra el boton
    document.getElementById("botonCancelarNotas").style.display = "inline-block"; //muestra el boton
    document.getElementById("botonNotas").style.display = "none"; // Oculta el botón "Modificar"
}

// Función para cancelar la edición, restaurar valores originales y deshabilitar el textarea
function cancelarEdicionNotas() {
	
    restaurarValoresNotas(); // Restaura los valores originales del textarea

    var notas_profesional = document.getElementById("notas_profesional");
    if (notas_profesional) {
        notas_profesional.disabled = true; // Deshabilita el textarea
    }

    // Oculta los botones de Guardar y Cancelar
    document.getElementById("botonGuardarNotas").style.display = "none";
    document.getElementById("botonCancelarNotas").style.display = "none";

    // Muestra el botón "Modificar" nuevamente
    document.getElementById("botonNotas").style.display = "block";
}


//Contador de caracteres del campo cuidados de la piel
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


// Script para manejar los botones de modificar, guardar y cancelar del turno 0
// Lista para almacenar los valores iniciales
const valoresOriginales0 = {};
document.addEventListener("DOMContentLoaded", function() {
    // Verifica si el botón "Modificar" existe en la página
    var turnoEstaPresenteOne = document.getElementById("botonHabilitar0");
    if (turnoEstaPresenteOne) {
      
        // Función para guardar los valores iniciales del textarea
        function guardarValoresOriginales0() {
            const consultaTurno0 = document.getElementById("consultaTurno0"); 
            if (consultaTurno0) {
                valoresOriginales0[consultaTurno0.id] = consultaTurno0.value; // Guarda el valor inicial del textarea
            }
        }

        // Ejecuta la función de guardado de valores originales al cargar la página
        guardarValoresOriginales0();
    }
});

// Función para restaurar los valores originales
function restaurarValores0() {
    const consultaTurno0 = document.getElementById("consultaTurno0");
    if (consultaTurno0) {
        consultaTurno0.value = valoresOriginales0[consultaTurno0.id]; // Restaura el valor inicial del textarea
    }
}

// Script para habilitar el textarea de recomendaciones
document.getElementById("botonHabilitar0").addEventListener("click", function() { 
    var consultaTurno0 = document.getElementById("consultaTurno0");
    if (consultaTurno0) {
        consultaTurno0.disabled = false; // Habilita el textarea
    }
});

// Función para mostrar los botones de Guardar y Cancelar y ocultar el de Modificar
function habilitarEdicion0() {
	cancelarEdicion1();
	cancelarEdicion2();
    document.getElementById("guardarButton0").style.display = "inline-block"; //muestra el boton
    document.getElementById("botonCancelarModificacion0").style.display = "inline-block"; //muestra el boton
    document.getElementById("botonHabilitar0").style.display = "none"; // Oculta el botón "Modificar"
}

// Función para cancelar la edición, restaurar valores originales y deshabilitar el textarea
function cancelarEdicion0() {
    restaurarValores0(); // Restaura los valores originales del textarea

    var consultaTurno0 = document.getElementById("consultaTurno0");
    if (consultaTurno0) {
        consultaTurno0.disabled = true; // Deshabilita el textarea
    }

    // Oculta los botones de Guardar y Cancelar
    document.getElementById("guardarButton0").style.display = "none";
    document.getElementById("botonCancelarModificacion0").style.display = "none";

    // Muestra el botón "Modificar" nuevamente
    document.getElementById("botonHabilitar0").style.display = "block";
}


// Script para habilitar y deshabilitar el campo de recomendaciones y para manejar los botones de modificar, guardar y cancelar del turno 1
const valoresOriginales1 = {};
document.addEventListener("DOMContentLoaded", function() {
    // Verifica si el botón "Modificar" existe en la página
    var turnoEstaPresenteTwo = document.getElementById("botonHabilitar1");
    if (turnoEstaPresenteTwo) {
      
        // Lista para almacenar los valores iniciales

        // Función para guardar los valores iniciales del textarea
        function guardarValoresOriginales1() {
            const consultaTurno1 = document.getElementById("consultaTurno1"); 
            if (consultaTurno1) {
                valoresOriginales1[consultaTurno1.id] = consultaTurno1.value; // Guarda el valor inicial del textarea
            }
        }

        // Ejecuta la función de guardado de valores originales al cargar la página
        guardarValoresOriginales1();
    }
});

// Función para restaurar los valores originales
function restaurarValores1() {
    const consultaTurno1 = document.getElementById("consultaTurno1");
    if (consultaTurno1) {
        consultaTurno1.value = valoresOriginales1[consultaTurno1.id]; // Restaura el valor inicial del textarea
    }
}

// Script para habilitar el textarea de recomendaciones
document.getElementById("botonHabilitar1").addEventListener("click", function() { 
    var consultaTurno1 = document.getElementById("consultaTurno1");
    if (consultaTurno1) {
        consultaTurno1.disabled = false; // Habilita el textarea
    }
});

// Función para mostrar los botones de Guardar y Cancelar y ocultar el de Modificar
function habilitarEdicion1() {
	cancelarEdicion0();
	cancelarEdicion2();
    document.getElementById("guardarButton1").style.display = "inline-block";
    document.getElementById("botonCancelarModificacion1").style.display = "inline-block";
    document.getElementById("botonHabilitar1").style.display = "none"; // Oculta el botón "Modificar"
}

// Función para cancelar la edición, restaurar valores originales y deshabilitar el textarea
function cancelarEdicion1() {
    restaurarValores1(); // Restaura los valores originales del textarea

    var consultaTurno1 = document.getElementById("consultaTurno1");
    if (consultaTurno1) {
        consultaTurno1.disabled = true; // Deshabilita el textarea
    }

    // Oculta los botones de Guardar y Cancelar
    document.getElementById("guardarButton1").style.display = "none";
    document.getElementById("botonCancelarModificacion1").style.display = "none";

    // Muestra el botón "Modificar" nuevamente
    document.getElementById("botonHabilitar1").style.display = "block";
}

  


// Script para habilitar y deshabilitar el campo de recomendaciones y para manejar los botones de modificar, guardar y cancelar del turno 2
const valoresOriginales2 = {};
document.addEventListener("DOMContentLoaded", function() {
    // Verifica si el botón "Modificar" existe en la página
    var turnoEstaPresenteThree = document.getElementById("botonHabilitar2");
    if (turnoEstaPresenteThree) {
      
        // Lista para almacenar los valores iniciales

        // Función para guardar los valores iniciales del textarea
        function guardarValoresOriginales2() {
            const consultaTurno2 = document.getElementById("consultaTurno2"); 
            if (consultaTurno2) {
                valoresOriginales2[consultaTurno2.id] = consultaTurno2.value; // Guarda el valor inicial del textarea
            }
        }

        // Ejecuta la función de guardado de valores originales al cargar la página
        guardarValoresOriginales2();
    }
});

// Función para restaurar los valores originales
function restaurarValores2() {
    const consultaTurno2 = document.getElementById("consultaTurno2");
    if (consultaTurno2) {
        consultaTurno2.value = valoresOriginales2[consultaTurno2.id]; // Restaura el valor inicial del textarea
    }
}

// Script para habilitar el textarea de recomendaciones
document.getElementById("botonHabilitar2").addEventListener("click", function() { 
    var consultaTurno2 = document.getElementById("consultaTurno2");
    if (consultaTurno2) {
        consultaTurno2.disabled = false; // Habilita el textarea
    }
});

// Función para mostrar los botones de Guardar y Cancelar y ocultar el de Modificar
function habilitarEdicion2() {
	cancelarEdicion0();
	cancelarEdicion1();
    document.getElementById("guardarButton2").style.display = "inline-block";
    document.getElementById("botonCancelarModificacion2").style.display = "inline-block";
    document.getElementById("botonHabilitar2").style.display = "none"; // Oculta el botón "Modificar"
}

// Función para cancelar la edición, restaurar valores originales y deshabilitar el textarea
function cancelarEdicion2() {
    restaurarValores2(); // Restaura los valores originales del textarea

    var consultaTurno2 = document.getElementById("consultaTurno2");
    if (consultaTurno2) {
        consultaTurno2.disabled = true; // Deshabilita el textarea
    }

    // Oculta los botones de Guardar y Cancelar
    document.getElementById("guardarButton2").style.display = "none";
    document.getElementById("botonCancelarModificacion2").style.display = "none";

    // Muestra el botón "Modificar" nuevamente
    document.getElementById("botonHabilitar2").style.display = "block";
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


// Guardar en una lista los valores originales del los select datos personales al cargar la página
  window.onload = guardarValoresOriginales;
  
// Lista para almacenar los valores iniciales
     const valoresOriginales = {};

// Función para guardar los valores iniciales al cargar la página
	    function guardarValoresOriginales() {
			const campos = document.querySelectorAll("#fuma, #drogas, #alcohol, #deportes, #ejercicios, #ciclo_menstrual," +
					   "#medicamentos, #embarazo, #amamantando, #alteracion_hormonal, #vitaminas, #corticoides, #hormonas, #tiroides," +
					   "#metodo_anticonceptivo, #sufre_enfermedad, #cual_enfermedad, #paciente_oncologica, #consumo_carbohidratos," +
					   "#fractura_facial, #tiene_implantes, #cirugia_estetica, #indique_cirugia_estetica, #marca_pasos, #horas_sueno," +
					   "#exposicion_sol, #protector_solar, #reaplica_protector, #motivo_consulta, #tratamientosFacialesAnteriores," +
					    "#resultados_tratamiento_anterior, #cuidado_de_piel");
	      
	      campos.forEach(campo => {
	        if (campo.tagName === "SELECT") {
	          valoresOriginales[campo.id] = campo.value; // Guarda el valor seleccionado en el select
	        } else {
	          valoresOriginales[campo.id] = campo.value; // Guarda el valor de los input
	        }
	      });
	    }
	 
// Función para restaurar los valores originales, 
		    function restaurarValores() {
		      const campos = document.querySelectorAll("#fuma, #drogas, #alcohol, #deportes, #ejercicios, #ciclo_menstrual," +
			   "#medicamentos, #embarazo, #amamantando, #alteracion_hormonal, #vitaminas, #corticoides, #hormonas, #tiroides," +
			   "#metodo_anticonceptivo, #sufre_enfermedad, #cual_enfermedad, #paciente_oncologica, #consumo_carbohidratos," +
			   "#fractura_facial, #tiene_implantes, #cirugia_estetica, #indique_cirugia_estetica, #marca_pasos, #horas_sueno," +
			   "#exposicion_sol, #protector_solar, #reaplica_protector, #motivo_consulta, #tratamientosFacialesAnteriores," +
			    "#resultados_tratamiento_anterior, #cuidado_de_piel");
		      
		      campos.forEach(campo => {
		        if (campo.tagName === "SELECT") {
		          campo.value = valoresOriginales[campo.id]; // Restaura el valor seleccionado en el select
		        } else {
		          campo.value = valoresOriginales[campo.id]; // Restaura el valor de los input
		        }
		      });
		    }

//Script para habilitar los select de datos personales
document.getElementById("botonHabilitarFormulario").addEventListener("click", function() { //La funcion se activa al hacer click en el boton habilitar
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
			  	    campoFuma.disabled = false;
			  	    campoDrogas.disabled = false;
					campoAlcohol.disabled = false;
					campoDeportes.disabled = false;
					campoEjercicios.disabled = false;
					campoCicloMenstrual.disabled = false;
					campoMedicamentos.disabled = false;
					campoNombreMedicamento.disabled = false;
					campoEmbarazo.disabled = false;
					campoAmamantendo.disabled = false;
					campoHormonal.disabled = false;
					campoVitaminas.disabled = false;
					campoCorticoides.disabled = false;
					campoHormonas.disabled = false;
					campoTiroides.disabled = false;
					campoAnticonceptivo.disabled = false;
					campoEnfermedad.disabled = false;
					campoCualEnfermedad.disabled = false;
					campoOncologico.disabled = false;
					campoCarbohidratos.disabled = false;
					campoFractura.disabled = false;
					campoImplantes.disabled = false;
					campoCirugia.disabled = false;
					campoIndiqueCirugia.disabled = false;
					campoMarcaPasos.disabled = false;
					campoSuenos.disabled = false;
					campoExposicionSol.disabled = false;
					campoProtectorSolar.disabled = false;
					campoReaplicaProtector.disabled = false;
					campoMotivoConsulta.disabled = false;
					campoTratamientosFacialesAnteriores.disabled = false;
					campoResultadoTratamientos.disabled = false;
					campoCuidadoPiel.disabled = false;
  });
  
  
// Función para habilitar ocultar el boton habilitar y mostrar los botones Guardar y Cancelar
  function habilitarEdicion() {
  	
  	// Mostrar el botón de Guardar y Cancelar
  	document.getElementById("botonGuardarFormulario").style.display = "inline-block";
  	document.getElementById("botonCancelarModificacion").style.display = "inline-block";

  	// Ocultar el botón "Modificar"
  	document.getElementById("botonHabilitarFormulario").style.display = "none";
  	
  }
  
// Función para mostrar el boton de Modificar y ocultar los botones de Guardar y Cancelar además deshabilita los select de los datos personales y restaura los
//valores de los select al original, tomandolo de la lista que se creo al cargar la pagina.
  function cancelarEdicion() {
	
  		restaurarValores(); //Restaura los valores originales de los campos select de datos personales
	
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

		// Deshabilita los campos select de datos personales
							campoFuma.disabled = true;
					  	    campoDrogas.disabled = true;
							campoAlcohol.disabled = true;
							campoDeportes.disabled = true;
							campoEjercicios.disabled = true;
							campoCicloMenstrual.disabled = true;
							campoMedicamentos.disabled = true;
							campoNombreMedicamento.disabled = true;
							campoEmbarazo.disabled = true;
							campoAmamantendo.disabled = true;
							campoHormonal.disabled = true;
							campoVitaminas.disabled = true;
							campoCorticoides.disabled = true;
							campoHormonas.disabled = true;
							campoTiroides.disabled = true;
							campoAnticonceptivo.disabled = true;
							campoEnfermedad.disabled = true;
							campoCualEnfermedad.disabled = true;
							campoOncologico.disabled = true;
							campoCarbohidratos.disabled = true;
							campoFractura.disabled = true;
							campoImplantes.disabled = true;
							campoCirugia.disabled = true;
							campoIndiqueCirugia.disabled = true;
							campoMarcaPasos.disabled = true;
							campoSuenos.disabled = true;
							campoExposicionSol.disabled = true;
							campoProtectorSolar.disabled = true;
							campoReaplicaProtector.disabled = true;
							campoMotivoConsulta.disabled = true;
							campoTratamientosFacialesAnteriores.disabled = true;
							campoResultadoTratamientos.disabled = true;
							campoCuidadoPiel.disabled = true;

  	// Ocultar el botón de Guardar y Cancelar
  	document.getElementById("botonGuardarFormulario").style.display = "none";
  	document.getElementById("botonCancelarModificacion").style.display = "none";

  	// Mostrar el botón "Modificar" nuevamente
  	document.getElementById("botonHabilitarFormulario").style.display = "block";
  }	
		  
