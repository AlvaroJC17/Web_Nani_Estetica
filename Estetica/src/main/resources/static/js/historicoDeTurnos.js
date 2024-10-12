
/*Script para el modal de existos*/
$(document).ready(function() {
       if (showModalExito) {
           $('#exitoModal').modal('show');
       }

       $('#exitoModal').on('hidden.bs.modal', function () {
           $(this).remove();
       });
   });


   /*Script para el modal de errores*/
$(document).ready(function() {
       if (showModalError) {
           $('#errorModal').modal('show');
       }

       $('#errorModal').on('hidden.bs.modal', function () {
           $(this).remove();
       });
   });

/*Script para los autosubmit de los select*/ 
function autoSubmitAno() {
           document.getElementById("formularioAnual").submit();
       }
	
	function autoSubmitMes() {
	           document.getElementById("formularioMensual").submit();
	       }
       
       function autoSubmitDia() {
           document.getElementById("formularioDiario").submit();
       }
       
        function autoSubmitIntervalo() {
           document.getElementById("formularioIntervalo").submit();
       }

	   
/*Scrip para darle funcionalidad a la tabla y colocarla en español*/
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

   
/*Script para habilitar el select de mes*/
	if(varActivarSelectMes){
		
		  $(document).ready(function() {
			var selectMes = document.getElementById('mes');
		      if (varActivarSelectMes) {
				selectMes.disabled = false;
		      }
		  });
		
	}else{
		console.log("No hay mes presente");
	}
  
/*Script para habilitar el select de dia*/
    $(document).ready(function() {
		var inputDia = document.getElementById('dia');
        if (varActivarInputDia) {
			inputDia.disabled = false;
        }
    });
    

/*Script para habilitar el select de fecha fin*/

	if(varActivarSelectFechaFin){
		  $(document).ready(function() {
			var selectFechaFin = document.getElementById('dia2');
		      if (varActivarSelectFechaFin) {
				selectFechaFin.disabled = false;
		      }
		  });
		 
	}else{
		console.log("No hay fecha fin presente");
	}
  

/*Script para habilitar la fecha 2 cuando se modifica la fecha 1*/
       function habilitarFecha2() {
           // Obtener el valor del primer input
           const fecha1 = document.getElementById('dia1').value;
           
           // Si el usuario selecciona una fecha, se habilita el segundo input
           if (fecha1) {
               document.getElementById('dia2').disabled = false;
           }
       }
		

/*Script para todas las funcinalidades de la grafica*/
			  const ctx = document.getElementById('myChart');
			  new Chart(ctx, {
			    type: 'doughnut',
			    data: {
					labels: [
					   'Turnos asistidos: ' + '$' + varMontoAsistidos,
					   'Turnos pendientes: ' + '$' + varMontoPendiente,
					   'Turnos cancelados: ' + '$' + varMontoCancelado,
					   'Turnos multas: ' + '$' + varMontoMulta
					 ],
					 datasets: [{
					   label: 'Monto total $',
					   data: [varMontoAsistidos, varMontoPendiente, varMontoCancelado, varMontoMulta],
					   backgroundColor: [
					     'rgb(54, 162, 235)',
					     'rgb(255, 205, 86)',
					     'rgb(255, 99, 132)',
						 'rgb(0, 0, 0)',
					   ],
					   hoverOffset: 4
					 }]
			    },
			    
			  });
		