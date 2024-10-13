//Script para el modal de error
    $(document).ready(function() {
        if (showModalError) {
            $('#errorModal').modal('show');
        }
        $('#errorModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });

//Script para el autosubmot del formulario
	function autoSubmitTratamiento() {
	           document.getElementById("formularioTratamiento").submit();
	       }

 //Script para la seleccion de especialidad, tratamientos y dias de la semana
 function agregarOpcion(selectId, listaId, campoOpcionesId) {
     const select = document.getElementById(selectId);
     const selectedOption = select.options[select.selectedIndex].text;
     const lista = document.getElementById(listaId);

     if (!Array.from(lista.options).some(option => option.value === selectedOption)) {
         const optionElement = document.createElement('option');
         optionElement.value = selectedOption;
         optionElement.text = selectedOption;
         lista.add(optionElement);
     }

     actualizarCampoOpciones(listaId, campoOpcionesId);
 }

 function actualizarCampoOpciones(listaId, campoOpcionesId) {
     const lista = document.getElementById(listaId);
     const opcionesSeleccionadas = Array.from(lista.options).map(option => option.value);
     document.getElementById(campoOpcionesId).value = opcionesSeleccionadas.join(',');
 }

 function eliminarOpcion(listaId, campoOpcionesId) {
     const lista = document.getElementById(listaId);
     lista.remove(lista.selectedIndex);
     actualizarCampoOpciones(listaId, campoOpcionesId);
 }

//Scrip para la seleccion de horarios
function agregarHorario() {
        const horas = document.getElementById('horas').value;
        const minutos = document.getElementById('minutos').value;
        const horario = horas + ':' + minutos;
        const lista = document.getElementById('listaHorarios');

        if (!Array.from(lista.options).some(option => option.value === horario)) {
            const optionElement = document.createElement('option');
            optionElement.value = horario;
            optionElement.text = horario;
            lista.add(optionElement);
        }

        actualizarCampoHorarios();
    }

    function actualizarCampoHorarios() {
        const lista = document.getElementById('listaHorarios');
        const horariosSeleccionados = Array.from(lista.options).map(option => option.value);
        document.getElementById('horariosSeleccionados').value = horariosSeleccionados.join(',');
    }

    function eliminarHorario() {
        const lista = document.getElementById('listaHorarios');
        lista.remove(lista.selectedIndex);
        actualizarCampoHorarios();
    }

//Scrip para la seleccion de tratamientos-->
function agregarTratamiento() {
    const tratamiento = document.getElementById('tratamiento').value;
    const costo = document.getElementById('costo').value;
    const tratamientoCosto = tratamiento + ' - $' + costo;
    const lista = document.getElementById('listaTratamientos');

    if (!Array.from(lista.options).some(option => option.value === tratamientoCosto)) {
        const optionElement = document.createElement('option');
        optionElement.value = tratamientoCosto;
        optionElement.text = tratamientoCosto;
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

