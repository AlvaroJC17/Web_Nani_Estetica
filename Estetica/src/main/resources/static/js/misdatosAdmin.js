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
				

// Guardar en una lista los valores originales del los select datos personales al cargar la página
  window.onload = guardarValoresOriginales;
  
// Lista para almacenar los valores iniciales
     const valoresOriginales = {};

// Función para guardar los valores iniciales al cargar la página
	    function guardarValoresOriginales() {
	      const campos = document.querySelectorAll("#ocupacion, #domicilio, #telefono, #sexo");
	      
	      campos.forEach(campo => {
	        if (campo.tagName === "SELECT") {
	          valoresOriginales[campo.id] = campo.value; // Guarda el valor seleccionado en el select
	        } else {
	          valoresOriginales[campo.id] = campo.value; // Guarda el valor de los input
	        }
	      });
	    }
	 
// Función para restaurar los valores originales, 
		    function restaurarValores() {
		      const campos = document.querySelectorAll("#ocupacion, #domicilio, #telefono, #sexo");
		      
		      campos.forEach(campo => {
		        if (campo.tagName === "SELECT") {
		          campo.value = valoresOriginales[campo.id]; // Restaura el valor seleccionado en el select
		        } else {
		          campo.value = valoresOriginales[campo.id]; // Restaura el valor de los input
		        }
		      });
		    }

//Script para habilitar los select de datos personales
document.getElementById("botonHabilitar").addEventListener("click", function() { //La funcion se activa al hacer click en el boton habilitar
	var campoTextoOcupacion = document.getElementById("ocupacion");
	var campoTextoDomicilio = document.getElementById("domicilio");
	var campoTextoSexo = document.getElementById("sexo");
	var campoTextoTelefono = document.getElementById("telefono");

	// habilita el campo de los select
	campoTextoOcupacion.disabled = false;
	campoTextoDomicilio.disabled = false;
	campoTextoSexo.disabled = false;
	campoTextoTelefono.disabled = false;
  });
  
  
// Función para habilitar ocultar el boton habilitar y mostrar los botones Guardar y Cancelar
  function habilitarEdicion() {
  	
  	// Mostrar el botón de Guardar y Cancelar
  	document.getElementById("botonGuardar").style.display = "inline-block";
  	document.getElementById("botonCancelarModificacion").style.display = "inline-block";

  	// Ocultar el botón "Modificar"
  	document.getElementById("botonHabilitar").style.display = "none";
  	
  }
  
// Función para mostrar el boton de Modificar y ocultar los botones de Guardar y Cancelar además deshabilita los select de los datos personales y restaura los
//valores de los select al original, tomandolo de la lista que se creo al cargar la pagina.
  function cancelarEdicion() {
	
  		restaurarValores(); //Restaura los valores originales de los campos select de datos personales
	
		var campoTextoOcupacion = document.getElementById("ocupacion");
		var campoTextoDomicilio = document.getElementById("domicilio");
		var campoTextoSexo = document.getElementById("sexo");
		var campoTextoTelefono = document.getElementById("telefono");

		// Deshabilita los campos select de datos personales
		campoTextoOcupacion.disabled = true;
		campoTextoDomicilio.disabled = true;
		campoTextoSexo.disabled = true;
		campoTextoTelefono.disabled = true;

  	// Ocultar el botón de Guardar y Cancelar
  	document.getElementById("botonGuardar").style.display = "none";
  	document.getElementById("botonCancelarModificacion").style.display = "none";

  	// Mostrar el botón "Modificar" nuevamente
  	document.getElementById("botonHabilitar").style.display = "block";
  }
  
  

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
