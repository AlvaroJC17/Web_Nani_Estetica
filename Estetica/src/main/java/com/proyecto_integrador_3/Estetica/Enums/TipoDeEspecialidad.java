package com.proyecto_integrador_3.Estetica.Enums;

public enum TipoDeEspecialidad {

	FACIAL_COSMETOLOGA("Cosmetologa"),
	ESTETICO_LASHISTA("Lashista"),
	ESTETICO_PEDICURA("Pedicura"),
	ESTETICO_MANICURA("Manicura"),
	CORPORAL_MASAJISTA("Masajista");
	
private String displayName;
	
	TipoDeEspecialidad(String displayName) {
        this.displayName = displayName;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }
	
}
