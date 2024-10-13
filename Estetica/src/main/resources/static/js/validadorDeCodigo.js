
/*Script para modal de error*/
$(document).ready(function() {
        if (showModalError) {
            $('#errorModal').modal('show');
        }

        $('#errorModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
  });

//script para el formulario del codigo, deshabilita el boton de envio por 5 segundos cuando se presiona
document.addEventListener("DOMContentLoaded", function() {
		 var formularioCodigo = document.getElementById('formularioCodigo');
		 var botonSubmitCodigo = document.getElementById('botonSubmitCodigo');

		    formularioCodigo.addEventListener('submit', function(event) {
		    // Deshabilitar el botón inmediatamente después de que se inicie el envío
		    botonSubmitCodigo.disabled = true;
			botonSubmitCodigo.classList.add("enviar");

		    // Rehabilitar el botón después de 5 segundos
		    setTimeout(function() {
		    botonSubmitCodigo.disabled = false;
			botonSubmitCodigo.classList.remove("enviar");
		}, 5000);
	});
});

/*Script para mostrar el contador y habilitar el boton cuando llegue a cero y tambien para deshabilitarlo por 5 segundos cuando se presione*/
document.addEventListener("DOMContentLoaded", function() {
        var botonSubmitReenvio = document.getElementById("botonSubmitReenvio");
        var formularioReenvio = document.getElementById('formularioReenviarCodigo');
        var isCountdownFinished = false; // Estado para saber si el contador ha terminado
        var isButtonInCooldown = false; // Estado para saber si el botón está en "cooldown" de 5 segundos
		
        if (conteoRegresivo) {
            // Deshabilitar el botón al iniciar el conteo
            botonSubmitReenvio.disabled = true;

            // Función para actualizar el contador
            function startCountdown() {
                var now = new Date();
                var timeDifference = expirationTime - now;

                if (timeDifference > 0) {
                    var minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
                    var seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

                    // Formatear para que siempre tenga 2 dígitos
                    minutes = ('0' + minutes).slice(-2);
                    seconds = ('0' + seconds).slice(-2);

                    document.getElementById("countdown2").innerText = minutes + ":" + seconds;
                } else {
                    document.getElementById("countdown2").innerText = "El código ha expirado.";
                    var divContador = document.getElementById("countdown2");
                    divContador.classList.add("text-danger");

                    isCountdownFinished = true; // Marcar que el contador ha terminado

                    // Habilitar el botón solo si no está en "cooldown"
                    if (!isButtonInCooldown) {
                        botonSubmitReenvio.disabled = false;
                    }
                }
            }

            // Actualizar el contador cada segundo
            setInterval(startCountdown, 1000);

            // Iniciar el contador inmediatamente al cargar la página
            startCountdown();
        } else {
            console.log("No se ha activado el conteo regresivo");
        }

        // Evento de envío de formulario
        formularioReenvio.addEventListener('submit', function(event) {
            if (isCountdownFinished) { // Verificar si el contador ha terminado antes de deshabilitar el botón
                // Deshabilitar el botón inmediatamente después de que se inicie el envío
                botonSubmitReenvio.disabled = true;
                isButtonInCooldown = true; // Marcar el botón en "cooldown"

                // Rehabilitar el botón después de 5 segundos
                setTimeout(function() {
                    isButtonInCooldown = false; // El "cooldown" termina después de 5 segundos
                    if (isCountdownFinished) { // Solo habilitar si el contador ha terminado
                        botonSubmitReenvio.disabled = false;
                    }
                }, 5000);
            }
        });
    });