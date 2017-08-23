/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parchapp;

/**
 *
 * @author Usuario
 */
public class Cliente {
    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String email;
    private String tipoCliente;
    
    public Cliente() {
        this.cedula = "";
        this.nombres = "";
        this.apellidos = "";
        this.direccion = "";
        this.email = "";
        this.tipoCliente = "";
    }
    
    public Cliente(String cedula, String nombres, String apellidos, String direccion, String email, String tipoCliente) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    
}
