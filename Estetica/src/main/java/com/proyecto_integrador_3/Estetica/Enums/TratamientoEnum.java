package com.proyecto_integrador_3.Estetica.Enums;

public enum TratamientoEnum {

	FACIALES_ANTIAGE("Tratamiento Antiage"),
	FACIALES_DESPIGMENTANTES("Tratamiento Despigmentante"),
	FACIALES_HIDRATANTES("Tratamiento Hidratante"),
	FACIALES_ROSACEAS("Tratamiento Rosaceas"),
	FACIALES_ANTIACNE("Tratamiento Antiacne"),
	CORPORAL_PULIDO("Pulido Corporal"),
	CORPORAL_MASAJE("Masaje Corporal"),
	CORPORAL_GLUTEOS("Pulido de Gluteos"),
	CORPORAL_LIMPIEZA_ESPALDA("Limpieza de Espalda"),
	ESTETICO_LIFTING_PESTAÑAS("Lifting de Pestañas"),
	ESTETICO_PERFILADO_CEJAS("Perfilado de Cejas"),
	ESTETICO_LAMINADO_CEJAS("Laminado de Cejas"),
	ESTETICO_HYDRALIPS("Hydralips"); 
	
private String displayName;
	
	TratamientoEnum(String displayName) {
        this.displayName = displayName;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name();
    }

}
