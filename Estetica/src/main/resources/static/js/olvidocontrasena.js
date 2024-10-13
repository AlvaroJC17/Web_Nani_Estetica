/*Scrip para el modal de mensaje de exito*/
			
    $(document).ready(function() {
        if (showModalExito) {
            $('#exitoModal').modal('show');
        }

        $('#exitoModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });


/*Script para modal de error*/

    $(document).ready(function() {
        if (showModalError) {
            $('#errorModal').modal('show');
        }

        $('#errorModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });

 /*script para el formulario del registro*/
 var formularioOlvidoContrasena = document.getElementById('formularioOlvidoContrasena');
 document.addEventListener("DOMContentLoaded", function() {
     var botonSubmitEnviar = document.getElementById('submitEnviar');
     formularioOlvidoContrasena.addEventListener('submit', function(event) {
         // Deshabilitar el botón inmediatamente después de que se inicie el envío
         botonSubmitEnviar.disabled = true;
         botonSubmitEnviar.classList.add("enviar");
     
         // Rehabilitar el botón después de 5 segundos
         setTimeout(function() {
             botonSubmitEnviar.disabled = false;
             botonSubmitEnviar.classList.remove("enviar");
            }, 5000);
        });
    });
