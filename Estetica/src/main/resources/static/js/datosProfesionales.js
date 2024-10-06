

// Variables para almacenar los valores originales del select de dias
let valoresOriginales = [];

//Funciona para cargar con la pagina los diferentes campos del usuario, dias laborales, horas y tratamientos
//con esta funcion cargamos al select al cargar la pagina las opciones que viene de la base de datos, esto sirve para que si el usuario le da guardar
//sin hacer ninguna modificacion, se envie al controlador la lista original del que proviene de la base de datos
window.onload = function () {

	actualizarCampoDias('listaSeleccionadas3', 'diasSeleccionadosCampo');
	guardarValoresOriginales(); //guarda una copia de la lista original de la base de datos, sirve para restaurar la lista cuando se presiona cancelar

	actualizarCampoHoras('listaSeleccionadas2', 'horasSeleccionadosCampo');
	guardarValoresOriginalesHoras(); //guarda una copia de la lista original de la base de datos, sirve para restaurar la lista cuando se presiona cancelar

	actualizarCampoTratamientos('listaSeleccionadas1', 'tratamientosSeleccionadosCampo');
	guardarValoresOriginalesTratamientos(); //guarda una copia de la lista original de la base de datos, sirve para restaurar la lista cuando se presiona cancelar
};

// Función para guardar los valores originales del select de días seleccionados
function guardarValoresOriginales() {
	const lista = document.getElementById("listaSeleccionadas3");
	// Guardar cada opción actual como un objeto con `value` y `text`
	valoresOriginales = Array.from(lista.options).map(option => ({
		value: option.value,
		text: option.text
	}));
}

// Función para restaurar los valores originales del select de días seleccionados
function restaurarValoresOriginales() {
	const lista = document.getElementById("listaSeleccionadas3");
	lista.innerHTML = '';  // Limpiamos el select

	// Restauramos las opciones originales
	valoresOriginales.forEach(item => {
		const option = document.createElement('option');
		option.value = item.value;
		option.text = item.text;
		lista.add(option);
	});

	actualizarCampoDias('listaSeleccionadas3', 'diasSeleccionadosCampo'); // Actualizamos el campo oculto
}

// Función para agregar un día desde el select de días al select de días seleccionados
function agregarDia(selectId, listaId, campoDiasId) {
	const select = document.getElementById(selectId);
	const selectedOption = select.options[select.selectedIndex].text;
	const lista = document.getElementById(listaId);

	// Verificamos que el día no esté ya en la lista para poder agregarlo
	if (!Array.from(lista.options).some(option => option.value === selectedOption)) {
		const optionElement = document.createElement('option');
		optionElement.value = selectedOption;
		optionElement.text = selectedOption;
		lista.add(optionElement);
	}

	// Actualizamos el campo oculto para enviar los días seleccionados al backend
	actualizarCampoDias(listaId, campoDiasId);
}

// Función para eliminar un día del select de días seleccionados
function eliminarDia(listaId, campoDiasId) {
	const lista = document.getElementById(listaId);
	lista.remove(lista.selectedIndex);  // Elimina el día seleccionado
	actualizarCampoDias(listaId, campoDiasId);  // Actualiza el campo oculto
}

// Función para actualizar el campo oculto que contiene los días seleccionados
function actualizarCampoDias(listaId, campoDiasId) {
	const lista = document.getElementById(listaId);
	const diasSeleccionados = Array.from(lista.options).map(option => option.value);
	document.getElementById(campoDiasId).value = diasSeleccionados.join(',');  // Actualiza el valor del campo oculto
}

// Función para habilitar la edición (mostrar elementos y habilitar selects) de los dias Laborales
function habilitarEdicion() {
	
	// Mostrar y habilitar el select de días laborales
	document.getElementById("divSeleccionDias").style.display = "block";
	document.getElementById("selectDiasLaborales").disabled = false;

	// Habilitar el select de días seleccionados
	document.getElementById("listaSeleccionadas3").disabled = false;

	// Mostrar los botones de agregar/eliminar días
	document.getElementById("divBotones").style.display = "block";

	// Mostrar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar").style.display = "inline-block";
	document.getElementById("btnCancelar").style.display = "inline-block";

	// Ocultar el botón "Modificar"
	document.getElementById("divModificar").style.display = "none";
	
	
}

// Función para cancelar la edición (volver a ocultar y deshabilitar)
function cancelarEdicion() {

	// Restaurar los valores originales del select, esto es para que vulva a mostrar la lista original si se presiona cancelar
	restaurarValoresOriginales();

	// Ocultar y deshabilitar el select de días laborales
	document.getElementById("divSeleccionDias").style.display = "none";
	document.getElementById("selectDiasLaborales").disabled = true;

	// Deshabilitar el select de días seleccionados
	document.getElementById("listaSeleccionadas3").disabled = true;

	// Ocultar los botones de agregar/eliminar días
	document.getElementById("divBotones").style.display = "none";

	// Ocultar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar").style.display = "none";
	document.getElementById("btnCancelar").style.display = "none";

	// Mostrar el botón "Modificar" nuevamente
	document.getElementById("divModificar").style.display = "block";
}


// Variables para almacenar los valores originales del select
let valoresOriginalesHoras = [];

// Función para guardar los valores originales del select de horas seleccionados
function guardarValoresOriginalesHoras() {
	const lista = document.getElementById("listaSeleccionadas2");
	// Guardar cada opción actual como un objeto con `value` y `text`
	valoresOriginalesHoras = Array.from(lista.options).map(option => ({
		value: option.value,
		text: option.text
	}));
}

// Función para restaurar los valores originales del select de horas seleccionados
function restaurarValoresOriginalesHoras() {
	const lista = document.getElementById("listaSeleccionadas2");
	lista.innerHTML = '';  // Limpiamos el select

	// Restauramos las opciones originales
	valoresOriginalesHoras.forEach(item => {
		const option = document.createElement('option');
		option.value = item.value;
		option.text = item.text;
		lista.add(option);
	});

	actualizarCampoHoras('listaSeleccionadas2', 'horasSeleccionadosCampo'); // Actualizamos el campo oculto
}

// Función para agregar una hora desde el select de horas al select de horas  seleccionadas
function agregarHora(selectHorasId, selectMinutosId, listaId, campoHorasId) {

	//Obetenmos las propiedades de los select de horas y minutos mediante sus Id y los guardamos en variables
	const selectHoras = document.getElementById(selectHorasId);
	const selectMinutos = document.getElementById(selectMinutosId);

	//Usamos esas variables para acceder a las propiedades value de los select de horas y minutos
	const horaSeleccionada = selectHoras.options[selectHoras.selectedIndex].value;
	const minutosSeleccionados = selectMinutos.options[selectMinutos.selectedIndex].value;

	//creamos un unica variable donde concatenamos la hora y los minutos, para que quede en formato HH:mm
	const horaCompleta = horaSeleccionada + ":" + minutosSeleccionados;

	const lista = document.getElementById(listaId);

	// Verificamos que la hora completa no esté ya en la lista antes de agregarla
	if (!Array.from(lista.options).some(option => option.value === horaCompleta)) {
		const optionElement = document.createElement('option');
		optionElement.value = horaCompleta;
		optionElement.text = horaCompleta;
		lista.add(optionElement);
	}

	// Actualizamos el campo oculto para enviar los días seleccionados al backend
	actualizarCampoHoras(listaId, campoHorasId);
}

// Función para eliminar un día del select de días seleccionados
function eliminarHora(listaId, campoHorasId) {
	const lista = document.getElementById(listaId);
	lista.remove(lista.selectedIndex);  // Elimina el día seleccionado
	actualizarCampoHoras(listaId, campoHorasId);  // Actualiza el campo oculto
}

// Función para actualizar el campo oculto que contiene los días seleccionados
function actualizarCampoHoras(listaId, campoHorasId) {
	const lista = document.getElementById(listaId);
	const horasSeleccionados = Array.from(lista.options).map(option => option.value);
	document.getElementById(campoHorasId).value = horasSeleccionados.join(',');  // Actualiza el valor del campo oculto
}

// Función para habilitar la edición (mostrar elementos y habilitar selects) de las horas Laborales
function habilitarEdicion2() {
	// Mostrar y habilitar el select de días laborales
	document.getElementById("divHorasMinutos").style.display = "flex";
	document.getElementById("selectHorasLaborales").disabled = false;
	document.getElementById("selectMinutos").disabled = false;

	// Habilitar el select de días seleccionados
	document.getElementById("listaSeleccionadas2").disabled = false;

	// Mostrar los botones de agregar/eliminar días
	document.getElementById("divBotones2").style.display = "block";

	// Mostrar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar2").style.display = "inline-block";
	document.getElementById("btnCancelar2").style.display = "inline-block";

	// Ocultar el botón "Modificar"
	document.getElementById("divModificar2").style.display = "none";
}

// Función para cancelar la edición (volver a ocultar y deshabilitar)
function cancelarEdicion2() {

	// Restaurar los valores originales del select
	restaurarValoresOriginalesHoras(); //restaura la lista a los valores originales cuando se presiona cancelar

	// Ocultar y deshabilitar el select de días laborales
	document.getElementById("divHorasMinutos").style.display = "none";
	document.getElementById("selectHorasLaborales").disabled = true;
	document.getElementById("selectMinutos").disabled = true;

	// Deshabilitar el select de días seleccionados
	document.getElementById("listaSeleccionadas2").disabled = true;

	// Ocultar los botones de agregar/eliminar días
	document.getElementById("divBotones2").style.display = "none";

	// Ocultar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar2").style.display = "none";
	document.getElementById("btnCancelar2").style.display = "none";

	// Mostrar el botón "Modificar" nuevamente
	document.getElementById("divModificar2").style.display = "block";
}


// Variables para almacenar los valores originales del select
let valoresOriginalesTratamientos = [];

// Función para guardar los valores originales del select de tratamientos seleccionados
function guardarValoresOriginalesTratamientos() {
	const lista = document.getElementById("listaSeleccionadas1");
	// Guardar cada opción actual como un objeto con `value` y `text`
	valoresOriginalesTratamientos = Array.from(lista.options).map(option => ({
		value: option.value,
		text: option.text
	}));
}

// Función para restaurar los valores originales del select de tratamientos seleccionados
function restaurarValoresOriginalesTratamientos() {
	const lista = document.getElementById("listaSeleccionadas1");
	lista.innerHTML = '';  // Limpiamos el select

	// Restauramos las opciones originales
	valoresOriginalesTratamientos.forEach(item => {
		const option = document.createElement('option');
		option.value = item.value;
		option.text = item.text;
		lista.add(option);
	});

	actualizarCampoTratamientos('listaSeleccionadas1', 'tratamientosSeleccionadosCampo'); // Actualizamos el campo oculto
}

function agregarTratamiento(selectId, listaId, campoTratamientoId, inputCostoId) {
    const select = document.getElementById(selectId);
    const selectedOption = select.options[select.selectedIndex].value;
    const lista = document.getElementById(listaId);
    const inputCosto = document.getElementById(inputCostoId); // Obtener el valor del input de costo
    const costo = inputCosto.value; // Valor del costo ingresado en el input

	// Verificamos que el tratamiento no esté ya en la lista comparando solo el nombre del tratamiento (sin costo)
	 const tratamientoYaEnLista = Array.from(lista.options).some(option => {
	     // Comparamos solo el nombre del tratamiento
	     return option.value.split(' - $')[0] === selectedOption;
	 });

    // Si el tratamiento no está en la lista, lo agregamos con el valor concatenado
    if (!tratamientoYaEnLista) {
        const optionElement = document.createElement('option');
        optionElement.value = selectedOption + " - $" + costo; // Concatenamos el costo al valor del tratamiento
        optionElement.text = selectedOption + " - $" + costo; // Concatenamos el costo al texto que se muestra
        lista.add(optionElement);
    }

    // Actualizamos el campo oculto para enviar los días seleccionados al backend
    actualizarCampoTratamientos(listaId, campoTratamientoId);
}


// Función para eliminar un tratamiento del select de tratamientos seleccionados
function eliminarTratamiento(listaId, campoTratamientoId) {
	const lista = document.getElementById(listaId);
	lista.remove(lista.selectedIndex);  // Elimina el tratamiento seleccionado
	actualizarCampoTratamientos(listaId, campoTratamientoId);  // Actualiza el campo oculto
}

// Función para actualizar el campo oculto que contiene los tratamientos seleccionados
function actualizarCampoTratamientos(listaId, campoTratamientoId) {
	const lista = document.getElementById(listaId);
	const tratamientosSeleccionados = Array.from(lista.options).map(option => option.value);
	document.getElementById(campoTratamientoId).value = tratamientosSeleccionados.join(',');  // Actualiza el valor del campo oculto
}

// Función para habilitar la edición (mostrar elementos y habilitar selects) de los tratamientos 
function habilitarEdicion1() {
	// Mostrar y habilitar el select de tratamientos 
	document.getElementById("divSeleccionTratamientos").style.display = "flex";
	document.getElementById("selectTratamientos").disabled = false;

	// Habilitar el select de tratamientos seleccionados
	document.getElementById("listaSeleccionadas1").disabled = false;

	// Mostrar los botones de agregar/eliminar tratamientos
	document.getElementById("divBotones1").style.display = "block";

	// Mostrar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar1").style.display = "inline-block";
	document.getElementById("btnCancelar1").style.display = "inline-block";

	// Ocultar el botón "Modificar"
	document.getElementById("divModificar1").style.display = "none";
}

// Función para cancelar la edición (volver a ocultar y deshabilitar)
function cancelarEdicion1() {

	// Restaurar los valores originales del select
	restaurarValoresOriginalesTratamientos();

	// Ocultar y deshabilitar el select de tratamientos
	document.getElementById("divSeleccionTratamientos").style.display = "none";
	document.getElementById("selectTratamientos").disabled = true;

	// Deshabilitar el select de tratamientos seleccionados
	document.getElementById("listaSeleccionadas1").disabled = true;

	// Ocultar los botones de agregar/eliminar tratamientos
	document.getElementById("divBotones1").style.display = "none";

	// Ocultar el botón de Guardar y Cancelar
	document.getElementById("btnGuardar1").style.display = "none";
	document.getElementById("btnCancelar1").style.display = "none";

	// Mostrar el botón "Modificar" nuevamente
	document.getElementById("divModificar1").style.display = "block";
}

document.addEventListener("DOMContentLoaded", function () {
	var formularioDiasLaborales = document.getElementById('formularioDiasLaborales');
	var botonGuardar3 = document.getElementById('btnGuardar');

	formularioDiasLaborales.addEventListener('submit', function (event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function () {
			botonGuardar3.disabled = true;
			botonGuardar3.classList.add("enviar");
			// Rehabilitar el botón después de 5 segundos
			setTimeout(function () {
				botonGuardar3.disabled = false;
				botonGuardar3.classList.remove("enviar");
			}, 5000);
		}, 0);
	});
});

document.addEventListener("DOMContentLoaded", function () {
	var formularioHorarioLaboral = document.getElementById('formularioHorarioLaboral');
	var btnGuardar2 = document.getElementById('btnGuardar2');

	formularioHorarioLaboral.addEventListener('submit', function (event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function () {
			btnGuardar2.disabled = true;
			btnGuardar2.classList.add("enviar");
			// Rehabilitar el botón después de 5 segundos
			setTimeout(function () {
				btnGuardar2.disabled = false;
				btnGuardar2.classList.remove("enviar");
			}, 5000);
		}, 0);
	});
});


document.addEventListener("DOMContentLoaded", function () {
	var formularioTratamientos = document.getElementById('formularioTratamientos');
	var btnGuardar1 = document.getElementById('btnGuardar1');

	formularioTratamientos.addEventListener('submit', function (event) {
		// Usar setTimeout con tiempo 0 para deshabilitar el botón justo después de que el formulario haya iniciado el envío
		setTimeout(function () {
			btnGuardar1.disabled = true;
			btnGuardar1.classList.add("enviar");
			// Rehabilitar el botón después de 5 segundos
			setTimeout(function () {
				btnGuardar1.disabled = false;
				btnGuardar1.classList.remove("enviar");
			}, 5000);
		}, 0);
	});
});
