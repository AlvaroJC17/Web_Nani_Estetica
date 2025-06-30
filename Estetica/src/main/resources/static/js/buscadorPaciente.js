
//Este jquery lo saque de chatgp, cumple la funcion de abrir el modal al recibir la variable de 
//showModal y tambien se encarga de cerrar el modal cuando le damos al boton cerrar

//Script para modal de exito
$(document).ready(function () {
  if (showModalExito) {
    $('#exitoModal').modal('show');
  }

  $('#exitoModal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
});

//Scrip para el modal de mensaje de error-->
$(document).ready(function() {
	if (showModalError) {
		$('#errorModal').modal('show');
	}

	$('#errorModal').on('hidden.bs.modal', function () {
		$(this).remove();
	});
});


//Scrip para darle funcionalidad a la tabla y colocarla en español-->
$(document).ready(function() {
	$('#myTable').DataTable({
		language: {
			url: 'https://cdn.datatables.net/plug-ins/2.1.0/i18n/es-ES.json'
		},
		columnDefs: [
			{ orderable: false, targets: 0 } // Desactiva la ordenación para la columna del checkbox
		]
	});
});

//Script para deshabilitar 5 segundos el boton de buscar
	document.addEventListener("DOMContentLoaded", function() {
		var formularioBuscador = document.getElementById('formularioBuscador');
		var botonsubmitBuscar = document.getElementById('submitBuscar');

		formularioBuscador.addEventListener('submit', function(event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function() {
		botonsubmitBuscar.disabled = true;
		botonsubmitBuscar.classList.add("enviar");
		// Rehabilitar el botón después de 5 segundos
		setTimeout(function() {
		botonsubmitBuscar.disabled = false;
		botonsubmitBuscar.classList.remove("enviar");
		}, 5000);
		}, 0);
	});
});


//Script para deshabilitar el boton durante 5 segundos de ver perfil
document.addEventListener("DOMContentLoaded", function() {
		var formularioTabla = document.getElementById('formularioTabla');
		var botonSubmitVerPerfil = document.getElementById('submitVerPerfil');

		formularioTabla.addEventListener('submit', function(event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function() {
		botonSubmitVerPerfil.disabled = true;
		botonSubmitVerPerfil.classList.add("enviar");
		// Rehabilitar el botón después de 5 segundos
		setTimeout(function() {
		botonSubmitVerPerfil.disabled = false;
		botonSubmitVerPerfil.classList.remove("enviar");
		}, 5000);
		}, 0);
	});
});
