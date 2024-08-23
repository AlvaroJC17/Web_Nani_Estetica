package com.proyecto_integrador_3.Estetica.Enums;

public enum TratamientoEnum {

	FACIAL_COSMETOLOGA_ANTIAGE("Tratamiento antiage"),
	FACIAL_COSMETOLOGA_DESPIGMENTANTES("Tratamiento despigmentante"),
	FACIAL_COSMETOLOGA_HIDRATANTES("Tratamiento hidratante"),
	FACIAL_COSMETOLOGA_ROSACEAS("Tratamiento rosaceas"),
	FACIAL_COSMETOLOGA_ANTIACNE("Tratamiento antiacne"),
	FACIAL_COSMETOLOGA_PULIDO("Pulido corporal"),
	FACIAL_COSMETOLOGA_GLUTEOS("Pulido de gluteos"),
	FACIAL_COSMETOLOGA_LIMPIEZA_ESPALDA("Limpieza de espalda"),
	CORPORAL_MASAJE_SEDATIVO("Masaje integral sedativo"),
	CORPORAL_MASAJE_DECONTRACTURANTE("Masaje integral descontracturante"),
	CORPORAL_MASAJE_DEPORTIVO("Masaje integral deportivo"),
	CORPORAL_MASAJE_DEPORTIVO_ZONA("Masaje deportivo por zona"),
	CORPORAL_MASAJE_CIRCULATORIO("Masaje circulatorio"),
	CORPORAL_MASAJE_REDUCTORES("Masaje reductor por zona"),
	CORPORAL_MASAJE_REFLELXOLOGIA("Reflexología"),
	CORPORAL_MASAJE_EXPRESS("Masaje exprés sedativo o descontracturante"),
	ESTETICO_PEDICURA_PIES("Belleza de pies"),
	ESTETICO_PEDICURA_COMPLETA("Pedicura completa"),
	ESTETICO_PEDICURA_RECONSTRUCCION_UNAS("Reconstrucción de uñas (Con acrílico autocurable + el esmaltado)"),
	ESTETICO_PEDICURA_ONICOMICOSIS("Onicomicosis"),
	ESTETICO_PEDICURA_VERRUGAS("Tratamiento de verrugas"),
	ESTETICO_PEDICURA_HELOMAS_DUREZA_CALLOS("Tratamiento de helomas, dureza, callos"),
	ESTETICO_COSMETOLOGA_LASHISTA_LIFTING_PESTAÑAS("Lifting de pestañas"),
	ESTETICO_COSMETOLOGA_LASHISTA_PERFILADO_CEJAS("Perfilado de cejas"),
	ESTETICO_COSMETOLOGA_LASHISTA_LAMINADO_CEJAS("Laminado de cejas"),
	ESTETICO_COSMETOLOGA_HYDRALIPS("Hydralips"),
	ESTETICO_MANICURA_ESCULPIDAS("Uñas esculpidas"),
	ESTETICO_MANICURA_SEMIPERMANENTE("Uñas semipermanentes"),
	ESTETICO_MANICURA_KAPPING("Kapping");
	
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
