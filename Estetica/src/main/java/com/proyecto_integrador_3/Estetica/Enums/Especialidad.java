package com.proyecto_integrador_3.Estetica.Enums;

public enum Especialidad {

	FACIAL("Facial"),
	CORPORAL("Corporal"),
	ESTETICO("Estético");
	
private String displayName;
	
	Especialidad(String displayName) {
        this.displayName = displayName;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }
	
}
