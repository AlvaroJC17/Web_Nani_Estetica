package com.proyecto_integrador_3.Estetica.Enums;

public enum Horarios {

	NUEVE("9:00"),
	DIEZ("10:00"),
	ONCE("11:00"),
	QUINCE("15:00"),
	DIECISEIS("16:00"),
	DIECISIETE("17:00"),
	DIECIOCHO("18:00"),
	DIECINUEVE("19:00"),
	VEINTE("20:00"),
	VEINTIUNO("21:00"),
	VEINTIDOS("22:00"),
	VEINTITRES("23:00");
	
	 private final String hora;

	    // Constructor del enum
	    Horarios(String hora) {
	        this.hora = hora;
	    }

	    // Getter para obtener el valor en String
	    public String getHora() {
	        return hora;
	    }
	

}
