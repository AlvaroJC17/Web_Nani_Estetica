/*Scrip para el modal de mensaje de exito*/
			

    $(document).ready(function() {
        if (showModalExito) {
            $('#exitoModal').modal('show');
        }

        $('#exitoModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
    });
