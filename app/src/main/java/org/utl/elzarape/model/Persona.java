package org.utl.elzarape.model;

public class Persona {
    private int idPersona;
    private String nombre;
    private String apellidos;
    private String telefono;

    public Persona() {
    }

    public Persona(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellido;
        this.telefono = telefono;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
