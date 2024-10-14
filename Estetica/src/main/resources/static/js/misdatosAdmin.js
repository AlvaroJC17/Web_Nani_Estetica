//Script para modal de exito
$(document).ready(function() {
    if (showModalExito) {
        $('#exitoModal').modal('show');
    }
    $('#exitoModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});
	

//Script para modal de error
$(document).ready(function() {
    if (showModalError) {
        $('#errorModal').modal('show');
    }
    $('#errorModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});
				

//Script para habilitar y deshabilitar los input y el boton
document.getElementById("botonHabilitar").addEventListener("click", function() {
	var guardarButton = document.getElementById("botonGuardar");
	var campoTextoOcupacion = document.getElementById("ocupacion");
	var campoTextoDomicilio = document.getElementById("domicilio");
	var campoTextoSexo = document.getElementById("sexo");
	var campoTextoTelefono = document.getElementById("telefono");

	// Alterna la propiedad 'disabled' del campo de texto
	guardarButton.disabled = !guardarButton.disabled;
	campoTextoOcupacion.disabled = !campoTextoOcupacion.disabled;
	campoTextoDomicilio.disabled = !campoTextoDomicilio.disabled;
	campoTextoSexo.disabled = !campoTextoSexo.disabled;
	campoTextoTelefono.disabled = !campoTextoTelefono.disabled;
	// Agrega o quita la clase según el estado de deshabilitación
	if (guardarButton.disabled) {
	  guardarButton.classList.add("enviar");
	} else {
	  guardarButton.classList.remove("enviar");
	}
  });
	
//script para deshabilitar el formulario cuando se ejecute el submit
document.addEventListener("DOMContentLoaded", function() {
var formularioActualizarCliente = document.getElementById('formularioActualizarAdmin');
var botonGuardar = document.getElementById('botonGuardar');

formularioActualizarCliente.addEventListener('submit', function(event) {
// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
setTimeout(function() {
botonGuardar.disabled = true;
botonGuardar.classList.add("enviar");
// Rehabilitar el botón después de 5 segundos
setTimeout(function() {
botonGuardar.disabled = false;
botonGuardar.classList.remove("enviar");
		}, 5000);
		}, 0);
	});
});
