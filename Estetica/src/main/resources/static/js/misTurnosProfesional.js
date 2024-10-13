
//Script para modal de error
$(document).ready(function() {
	if (showModalError) {
		$('#errorModal').modal('show');
	}

	$('#errorModal').on('hidden.bs.modal', function () {
		$(this).remove();
	});
});
				

//Scrip para el modal de mensaje de exito-->
$(document).ready(function() {
	if (showModalExito) {
		$('#exitoModal').modal('show');
	}

	$('#exitoModal').on('hidden.bs.modal', function () {
		$(this).remove();
	});
});

	
//script para deshabilitar el formulario cuando se ejecute el submit para Historico de turnos
document.addEventListener("DOMContentLoaded", function() {
var formularioHistorio = document.getElementById('formularioHistorio');
var botonsubmitHistorico = document.getElementById('submitHistorico');

formularioHistorio.addEventListener('submit', function(event) {
// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
setTimeout(function() {
botonsubmitHistorico.disabled = true;
botonsubmitHistorico.classList.add("enviar");
// Rehabilitar el botón después de 5 segundos
setTimeout(function() {
botonsubmitHistorico.disabled = false;
botonsubmitHistorico.classList.remove("enviar");
}, 5000);
}, 0);
});
});
						  										
		
//script para deshabilitar el formulario cuando se ejecute el submit para turnosPorFecha
document.addEventListener("DOMContentLoaded", function() {
var formularioTurnosFechas = document.getElementById('formularioTurnosFechas');
var botonsubmitFechas = document.getElementById('submitFechas');

formularioTurnosFechas.addEventListener('submit', function(event) {
// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
setTimeout(function() {
botonsubmitFechas.disabled = true;
botonsubmitFechas.classList.add("enviar");
// Rehabilitar el botón después de 5 segundos
setTimeout(function() {
botonsubmitFechas.disabled = false;
botonsubmitFechas.classList.remove("enviar");
}, 5000);
}, 0);
});
});
													  										
		
//script para deshabilitar el formulario cuando se ejecute el submit para resumen de turnos
document.addEventListener("DOMContentLoaded", function() {
var formularioResumenTurnos = document.getElementById('formularioResumenTurnos');
var botonsubmitResumen = document.getElementById('submitResumen');

formularioResumenTurnos.addEventListener('submit', function(event) {
// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
setTimeout(function() {
botonsubmitResumen.disabled = true;
botonsubmitResumen.classList.add("enviar");
// Rehabilitar el botón después de 5 segundos
setTimeout(function() {
botonsubmitResumen.disabled = false;
botonsubmitResumen.classList.remove("enviar");
}, 5000);
}, 0);
});
});
															  										
													  
