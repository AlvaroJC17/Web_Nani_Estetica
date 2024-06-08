
window.addEventListener('popstate', function(event) {
    const fecha = document.getElementById('fecha').value;
    const idCliente = document.getElementById('idCliente').value;
    const email = document.getElementById('email').value;

    // Guardar los valores en sessionStorage
    sessionStorage.setItem('fecha', fecha);
    sessionStorage.setItem('idCliente', idCliente);
    sessionStorage.setItem('email', email);

    // Redirigir a la página deseada
    window.location.href = '../pagina_cliente/tratamiento';
});

function pushHistoryState() {
    history.pushState({}, '');
}

window.addEventListener('load', pushHistoryState);

