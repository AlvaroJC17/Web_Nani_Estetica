/*Script para el modal de exitos*/
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

/*script para el formulario del login*/
	 document.addEventListener("DOMContentLoaded", function() {
		var formularioLogin = document.getElementById('formularioLogin');
		var botonSubmitLogin = document.getElementById('submitLogin');

		formularioLogin.addEventListener('submit', function(event) {
			 // Deshabilitar el botón inmediatamente después de que se inicie el envío
			 botonSubmitLogin.disabled = true;
			 botonSubmitLogin.classList.add("enviar");

			 // Rehabilitar el botón después de 5 segundos
			setTimeout(function() {
				botonSubmitLogin.disabled = false;
				botonSubmitLogin.classList.remove("enviar");
				}, 5000);
		});
	});

