


window.addEventListener('load', function() {
    const fecha = sessionStorage.getItem('fecha');
    const idCliente = sessionStorage.getItem('idCliente');
    const email = sessionStorage.getItem('email');

    if (fecha && idCliente && email) {
        document.getElementById('fecha').value = fecha;
        document.getElementById('idCliente').value = idCliente;
        document.getElementById('email').value = email;
    } else {
        // Manejar el caso en que no se encuentren los valores esperados
        alert('No se encontraron los datos necesarios.');
    }
});