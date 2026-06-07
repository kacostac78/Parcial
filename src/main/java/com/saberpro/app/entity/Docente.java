package com.saberpro.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoDocumento;
    private String numeroDocumento;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;

    @Column(unique = true)
    private String correo;

    private String telefono;
    private String programa;
    private boolean activo = true;

    public Docente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String v) { this.tipoDocumento = v; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String v) { this.numeroDocumento = v; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String v) { this.primerNombre = v; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String v) { this.segundoNombre = v; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String v) { this.primerApellido = v; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String v) { this.segundoApellido = v; }

    public String getCorreo() { return correo; }
    public void setCorreo(String v) { this.correo = v; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { this.telefono = v; }

    public String getPrograma() { return programa; }
    public void setPrograma(String v) { this.programa = v; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean v) { this.activo = v; }

    public String getNombreCompleto() {
        return primerNombre + " " +
               (segundoNombre != null ? segundoNombre + " " : "") +
               primerApellido + " " +
               (segundoApellido != null ? segundoApellido : "");
    }
}