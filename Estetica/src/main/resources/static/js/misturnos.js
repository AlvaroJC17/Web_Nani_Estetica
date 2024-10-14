//Script para modal de error-->
$(document).ready(function() {
    if (showModalError) {
        $('#errorModal').modal('show');
    }
    $('#errorModal').on('hidden.bs.modal', function () {
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

	
	
	
	
			

