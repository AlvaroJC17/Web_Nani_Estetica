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
	
		  
