package org.utl.elzarape.model;

public class Ciudad {
    private int idCiudad;
    private String nombre;

    public Ciudad() {
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre; // O cualquier otro campo que desees mostrar
    }
}
