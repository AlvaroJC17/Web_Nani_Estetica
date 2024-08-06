package com.proyecto_integrador_3.Estetica.Enums;

public enum TratamientoEnum {

	FACIALES_ANTIAGE("Tratamiento antiage"),
	FACIALES_DESPIGMENTANTES("Tratamiento despigmentante"),
	FACIALES_HIDRATANTES("Tratamiento hidratante"),
	FACIALES_ROSACEAS("Tratamiento rosaceas"),
	FACIALES_ANTIACNE("Tratamiento antiacne"),
	CORPORAL_PULIDO("Pulido corporal"),
	CORPORAL_GLUTEOS("Pulido de gluteos"),
	CORPORAL_LIMPIEZA_ESPALDA("Limpieza de espalda"),
	CORPORAL_MASAJE_SEDATIVO("Masaje integral sedativo"),
	CORPORAL_MASAJE_DECONTRACTURANTE("Masaje integral descontracturante"),
	CORPORAL_MASAJE_DEPORTIVO("Masaje integral deportivo"),
	CORPORAL_MASAJE_DEPORTIVO_ZONA("Masaje deportivo por zona"),
	CORPORAL_MASAJE_CIRCULATORIO("Masaje circulatorio"),
	CORPORAL_MASAJE_REDUCTORES("Masaje reductor por zona"),
	CORPORAL_REFLELXOLOGIA("Reflexología"),
	CORPORAL_MASAJE_EXPRESS("Masaje exprés sedativo o descontracturante"),
	ESTETICO_PIES("Belleza de pies"),
	ESTETICO_PEDICURA_COMPLETA("Pedicura completa"),
	ESTETICO_RECONSTRUCCION_UNAS("Reconstrucción de uñas (Con acrílico autocurable + el esmaltado)"),
	ESTETICO_ONICOMICOSIS("Onicomicosis"),
	ESTETICO_VERRUGAS("Tratamiento de verrugas"),
	ESTETICO_HELOMAS_DUREZA_CALLOS("Tratamiento de helomas, dureza, callos"),
	ESTETICO_LIFTING_PESTAÑAS("Lifting de pestañas"),
	ESTETICO_PERFILADO_CEJAS("Perfilado de cejas"),
	ESTETICO_LAMINADO_CEJAS("Laminado de cejas"),
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
