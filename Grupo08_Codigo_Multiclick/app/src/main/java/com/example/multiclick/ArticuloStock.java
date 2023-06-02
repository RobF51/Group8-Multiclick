package com.example.multiclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ArticuloStock {
    private String categoria;
    private String nombreArticulo;
    private String cantidad;

    public ArticuloStock(String categoria, String nombreArticulo, String cantidad) {
        this.categoria = categoria;
        this.nombreArticulo = nombreArticulo;
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}