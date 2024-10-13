/*Scrip para el modal de mensaje de exito*/

    $(document).ready(function() {
        if (showModalExito) {
            $('#exitoModal').modal('show');
        }

        $('#exitoModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });

/*Scrip para el modal de mensaje de error*/

    $(document).ready(function() {
        if (showModalError) {
            $('#errorModal').modal('show');
        }

        $('#errorModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });

	
/*script para el boton  del formulario del registro*/
 document.addEventListener("DOMContentLoaded", function() {
	var formularioRegistro = document.getElementById('formularioRegistro');
	var botonSubmitRegistrarse = document.getElementById('submitRegistrarse');

		formularioRegistro.addEventListener('submit', function(event) {
		/*Deshabilitar el botón inmediatamente después de que se inicie el envío*/
		botonSubmitRegistrarse.disabled = true;
		botonSubmitRegistrarse.classList.add("enviar");

		/*Rehabilitar el botón después de 5 segundos*/
		setTimeout(function() {
		botonSubmitRegistrarse.disabled = false;
		botonSubmitRegistrarse.classList.remove("enviar");
		}, 5000);
	});
 });