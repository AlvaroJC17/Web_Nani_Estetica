//Este jquery lo saque de chatgp, cumple la funcion de abrir el modal al recibir la variable de 
//showModal y tambien se encarga de cerrar el modal cuando le damos al boton cerrar

//Scrip para el modal de eliminar usuario exitoso
$(document).ready(function() {
    if (showModalBorradoExitoso) {
        $('#eliminarModal').modal('show');
    }
    $('#eliminarModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});
			
//Scrip para el modal de mensaje de exito-->
$(document).ready(function() {
    if (showModalExito) {
        $('#exitoModal').modal('show');
    }
    $('#exitoModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});

//Scrip para el modal de mensaje de error-->
$(document).ready(function() {
    if (showModalError) {
        $('#errorModal').modal('show');
    }
    $('#errorModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
});

//Script para la tabla y colocarla en español
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

   