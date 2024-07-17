package com.proyecto_integrador_3.Estetica.Enums;

public enum DiasDeLaSemana {

	LUNES("1"),
	MARTES("2"),
	MIERCOLES("3"),
	JUEVES("4"),
	VIERNES("5"),
	SABADO("6"),
	DOMINGO("7");
	
private String displayName;
	
	DiasDeLaSemana(String displayName) {
        this.displayName = displayName;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }
}
