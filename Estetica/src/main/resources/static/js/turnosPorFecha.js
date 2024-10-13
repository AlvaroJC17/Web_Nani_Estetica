

//Script para modal de error
				
$(document).ready(function() {
	if (showModalError) {
		$('#errorModal').modal('show');
	}

	$('#errorModal').on('hidden.bs.modal', function () {
		$(this).remove();
	});
});


//Scrip para el modal de mensaje de exito
	
$(document).ready(function() {
	if (showModalExito) {
		$('#exitoModal').modal('show');
	}

	$('#exitoModal').on('hidden.bs.modal', function () {
		$(this).remove();
	});
});
			

/*script para deshabilitar el formulario cuando se ejecute el submit para Historico de turnos*/
document.addEventListener("DOMContentLoaded", function() {
	var formularioBuscar = document.getElementById('formularioBuscar');
	var botonsubmitBuscar = document.getElementById('botonBuscar');
	
	formularioBuscar.addEventListener('submit', function(event) {
		/*Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío*/
		setTimeout(function() {
			botonsubmitBuscar.disabled = true;
			botonsubmitBuscar.classList.add("enviar");
			/*Rehabilitar el botón después de 5 segundos*/
			setTimeout(function() {
				botonsubmitBuscar.disabled = false;
				botonsubmitBuscar.classList.remove("enviar");
			}, 5000);
		}, 0);
	});
});
								  										
/*script para deshabilitar el formulario cuando se ejecute el submit para Historico de turnos*/
document.addEventListener("DOMContentLoaded", function() {
	var formularioDeshabilitarFecha = document.getElementById('formularioDeshabilitarFecha');
	var botonDeshabilitarFecha = document.getElementById('botonDeshabilitarFecha');
	formularioDeshabilitarFecha.addEventListener('submit', function(event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function() {
			botonDeshabilitarFecha.disabled = true;
			botonDeshabilitarFecha.classList.remove("cancelar");
			botonDeshabilitarFecha.classList.add("enviar");
			// Rehabilitar el botón después de 5 segundos
			setTimeout(function() {
			botonDeshabilitarFecha.disabled = false;
			botonDeshabilitarFecha.classList.remove("enviar");
		  botonDeshabilitarFecha.classList.add("cancelar");
					}, 5000);
					}, 0);
				});
			});

			
//script para deshabilitar el formulario cuando se ejecute el submit para Historico de turnos
document.addEventListener("DOMContentLoaded", function() {
	var formularioDeshabilitarHora = document.getElementById('formularioDeshabilitarHora');
	var botonDeshabilitarHora = document.getElementById('botonDeshabilitarHora');

	formularioDeshabilitarHora.addEventListener('submit', function(event) {
	// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
	setTimeout(function() {
	botonDeshabilitarHora.disabled = true;
  botonDeshabilitarHora.classList.remove("cancelar");
	botonDeshabilitarHora.classList.add("enviar");
	// Rehabilitar el botón después de 5 segundos
	setTimeout(function() {
	botonDeshabilitarHora.disabled = false;
	botonDeshabilitarHora.classList.remove("enviar");
  botonDeshabilitarHora.classList.add("cancelar");
			}, 5000);
			}, 0);
		});
	});
	
	
	
							  										


										
										
										
																  										
												

