package org.utl.elzarape.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String contrasenia;
    private int rol;
    private String lastToken;
    private String dateLastTaken;

    public Usuario() {
    }

    public Usuario(String usuario, String contrasena) {
        this.nombre = usuario;
        this.contrasenia = contrasena;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public String getDateLastTaken() {
        return dateLastTaken;
    }

    public void setDateLastTaken(String dateLastTaken) {
        this.dateLastTaken = dateLastTaken;
    }
}
