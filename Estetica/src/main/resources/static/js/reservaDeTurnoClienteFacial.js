
//Script para modal de error
$(document).ready(function() {
    if (showModalError) {
        $('#errorModal').modal('show');
    }
    $('#errorModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});
				

//Script para activar los autosubmit de los select
function autoSubmitProvincia() {
    document.getElementById("formularioProvincia").submit();
}

function autoSubmitEspecialidad() {
           document.getElementById("formularioEspecialidad").submit();
       }

function autoSubmitProfesional() {
    document.getElementById("formularioProfesional").submit();
}

 function autoSubmitFecha() {
    document.getElementById("formularioFecha").submit();
}

 function autoSubmitHorario() {
    document.getElementById("formularioHorario").submit();
}

//Scrip para la seleccion de tratamientos-->
function agregarTratamiento() {
const tratamientoSelect = document.getElementById('tratamiento');
const tratamientoId = tratamientoSelect.value;
const tratamientoNombre = tratamientoSelect.options[tratamientoSelect.selectedIndex].text;
const lista = document.getElementById('listaTratamientos');

if (!Array.from(lista.options).some(option => option.value === tratamientoId)) {
const optionElement = document.createElement('option');
optionElement.value = tratamientoId;
optionElement.text = tratamientoNombre;
lista.add(optionElement);
}
actualizarCampoTratamientos();
}
     
function actualizarCampoTratamientos() {
    const lista = document.getElementById('listaTratamientos');
    const tratamientosSeleccionados = Array.from(lista.options).map(option => option.value);
    document.getElementById('tratamientosSeleccionados').value = tratamientosSeleccionados.join(',');
}

function eliminarTratamiento() {
    const lista = document.getElementById('listaTratamientos');
    lista.remove(lista.selectedIndex);
    actualizarCampoTratamientos();
}

//script para el formulario del registo
document.addEventListener("DOMContentLoaded", function() {
var formularioConfirmarTurno = document.getElementById('formularioConfirmarTurno');
var botonSubmitEnviar = document.getElementById('submitEnviar');

formularioConfirmarTurno.addEventListener('submit', function(event) {
// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
setTimeout(function() {
botonSubmitEnviar.disabled = true;
botonSubmitEnviar.classList.add("enviar");
// Rehabilitar el botón después de 5 segundos
setTimeout(function() {
botonSubmitEnviar.disabled = false;
botonSubmitEnviar.classList.remove("enviar");
        }, 5000);
        }, 0);
    });
});
										
