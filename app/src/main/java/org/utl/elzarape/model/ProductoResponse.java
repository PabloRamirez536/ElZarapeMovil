package org.utl.elzarape.model;

public class ProductoResponse {
    private Alimento alimento;
    private Bebida bebida;

    public ProductoResponse() {
    }
    // Getters y Setters
    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public Bebida getBebida() {
        return bebida;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }
}
