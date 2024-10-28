//Script para modal de exito
$(document).ready(function () {
  if (showModalExito) {
    $('#exitoModal').modal('show');
  }

  $('#exitoModal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
});

//Script para modal de error
$(document).ready(function () {
  if (showModalError) {
    $('#errorModal').modal('show');
  }

  $('#errorModal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
});

//Script para la tabla de proximos turnos activos y colocarla en español
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

//Script para la tabla de turnos con multa y colocarla en español
$(document).ready(function() {
    $('#myTable2').DataTable({
        language: {
            url: 'https://cdn.datatables.net/plug-ins/2.1.0/i18n/es-ES.json'
        },
        columnDefs: [
            { orderable: false, targets: 0 } // Desactiva la ordenación para la columna del checkbox
        ]
    });
});

//Script para habilitar y deshabilitar el boton de cancelar turnos
if(botonCancelarTurno){
	var btnCancelar = document.getElementById('btnCancelar');
	if(btnCancelar){
		btnCancelar.disabled = false;
		btnCancelar.classList.remove("enviar");
	}
	
}else{
	console.log("No hay boton disponible");
}

// Guardar en una lista los valores originales del los select datos personales al cargar la página
  window.onload = guardarValoresOriginales;
  
// Lista para almacenar los valores iniciales
     const valoresOriginales = {};

// Función para guardar los valores iniciales al cargar la página
	    function guardarValoresOriginales() {
	      const campos = document.querySelectorAll("#dni, #nacimiento, #email, #ocupacion, #domicilio, #telefono, #sexo");
	      
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
		      const campos = document.querySelectorAll("#dni, #nacimiento, #email, #ocupacion, #domicilio, #telefono, #sexo");
		      
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
	var guardarButton = document.getElementById("botonGuardar");
	var campoTextoOcupacion = document.getElementById("ocupacion");
	var campoTextoDomicilio = document.getElementById("domicilio");
	var campoTextoSexo = document.getElementById("sexo");
	var campoTextoTelefono = document.getElementById("telefono");
	var campoTextoDni = document.getElementById("dni");
	var campoTextoFechaNacimiento = document.getElementById("nacimiento");
	var campoTextoEmail = document.getElementById("email");

	// habilita el campo de los select
	campoTextoOcupacion.disabled = false;
	campoTextoDomicilio.disabled = false;
	campoTextoSexo.disabled = false;
	campoTextoTelefono.disabled = false;
	campoTextoDni.disabled = false;
	campoTextoFechaNacimiento.disabled = false;
	campoTextoEmail.disabled = false;
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
		var campoTextoDni = document.getElementById("dni");
		var campoTextoFechaNacimiento = document.getElementById("nacimiento");
		var campoTextoEmail = document.getElementById("email");

		// Deshabilita los campos select de datos personales
		campoTextoOcupacion.disabled = true;
		campoTextoDomicilio.disabled = true;
		campoTextoSexo.disabled = true;
		campoTextoTelefono.disabled = true;
		campoTextoDni.disabled = true;
		campoTextoFechaNacimiento.disabled = true;
		campoTextoEmail.disabled = true;
	 

  	// Ocultar el botón de Guardar y Cancelar
  	document.getElementById("botonGuardar").style.display = "none";
  	document.getElementById("botonCancelarModificacion").style.display = "none";

  	// Mostrar el botón "Modificar" nuevamente
  	document.getElementById("botonHabilitar").style.display = "block";
  }
  	
	
//script para deshabilitar el formulario cuando se ejecute el submit
document.addEventListener("DOMContentLoaded", function() {
var formularioActualizarCliente = document.getElementById('formularioActualizarCliente');
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
	
		  
