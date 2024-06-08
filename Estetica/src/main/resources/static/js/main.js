window.addEventListener('beforeunload', function() {
  const fecha = document.getElementById('fecha').value;
  const idCliente = document.getElementById('idCliente').value;
  const email = document.getElementById('email').value;

  sessionStorage.setItem('fecha', fecha);
  sessionStorage.setItem('idCliente', idCliente);
  sessionStorage.setItem('email', email);
});

window.addEventListener('load', function() {
  const fecha = sessionStorage.getItem('fecha');
  const idCliente = sessionStorage.getItem('idCliente');
  const email = sessionStorage.getItem('email');

  if (fecha) {
    document.getElementById('fecha').value = fecha;
  }
  if (idCliente) {
    document.getElementById('idCliente').value = idCliente;
  }
  if (email) {
    document.getElementById('email').value = email;
  }
});
