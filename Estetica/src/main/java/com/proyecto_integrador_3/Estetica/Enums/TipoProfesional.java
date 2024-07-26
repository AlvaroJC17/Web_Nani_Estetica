package com.proyecto_integrador_3.Estetica.Enums;

public enum TipoProfesional {

	FACIAL_COSMETOLOGA("Cosmetologa"),
	ESTETICO_LASHISTA("Lashista"),
	ESTETICO_PEDICURA("Pedicura"),
	ESTETICO_MANICURA("Manicura"),
	CORPORAL_MASAJISTA("Masajista");
	
private String displayName;
	
	TipoProfesional(String displayName) {
        this.displayName = displayName;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }
	
}
