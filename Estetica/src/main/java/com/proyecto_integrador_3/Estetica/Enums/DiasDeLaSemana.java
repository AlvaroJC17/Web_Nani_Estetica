package com.proyecto_integrador_3.Estetica.Enums;

public enum DiasDeLaSemana {

	LUNES("1", "MONDAY"),
	MARTES("2", "TUESDAY"),
	MIERCOLES("3", "WEDNESDAY"),
	JUEVES("4", "THURSDAY"),
	VIERNES("5", "FRIDAY"),
	SABADO("6", "SATURDAY"),
	DOMINGO("7", "SUNDAY");
	
	//Atributos
	private String displayName; //propiedad para mostrar el numero del dia
	private String abbreviation; // Nueva propiedad para la abreviatura del día

// Constructor con más de un campo
	DiasDeLaSemana(String displayName, String abbreviation) {
    this.displayName = displayName;
    this.abbreviation = abbreviation;
}

	 // Getter para displayName
	public String getDisplayName() {
        return displayName;
    }
	
	 // Getter para abbreviation
    public String getAbbreviation() {
        return abbreviation;
    }

    // Método para obtener el nombre original del enum
    public String getName() {
        return name();
    }
}
