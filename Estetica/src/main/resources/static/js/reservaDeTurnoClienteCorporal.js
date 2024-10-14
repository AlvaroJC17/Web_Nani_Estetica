//Script para modal de error
$(document).ready(function() {
    if (showModalError) {
        $('#errorModal').modal('show');
    }
    $('#errorModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});
				

//Script para los autosubmit
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



										
