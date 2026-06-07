package com.saberpro.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultados")
public class ResultadoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    private LocalDate fechaExamen;

    private Double puntajeGlobal;
    private String nivelGlobal;

    private Double comunicacionEscrita;
    private String comunicacionEscritaNivel;

    private Double razonamientoCuantitativo;
    private String razonamientoCuantitativoNivel;

    private Double lecturaCritica;
    private String lecturaCriticaNivel;

    private Double competenciasCiudadanas;
    private String competenciasCiudadanasNivel;

    private Double ingles;
    private String inglesNivel;

    private Double formulacionProyectos;
    private String formulacionProyectosNivel;

    private Double pensamientoCientifico;
    private String pensamientoCientificoNivel;

    private Double disenoSoftware;
    private String disenoSoftwareNivel;

    public ResultadoSaberPro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public LocalDate getFechaExamen() { return fechaExamen; }
    public void setFechaExamen(LocalDate fechaExamen) { this.fechaExamen = fechaExamen; }

    public Double getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Double puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }

    public String getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(String nivelGlobal) { this.nivelGlobal = nivelGlobal; }

    public Double getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Double v) { this.comunicacionEscrita = v; }

    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String v) { this.comunicacionEscritaNivel = v; }

    public Double getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double v) { this.razonamientoCuantitativo = v; }

    public String getRazonamientoCuantitativoNivel() { return razonamientoCuantitativoNivel; }
    public void setRazonamientoCuantitativoNivel(String v) { this.razonamientoCuantitativoNivel = v; }

    public Double getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Double v) { this.lecturaCritica = v; }

    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String v) { this.lecturaCriticaNivel = v; }

    public Double getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double v) { this.competenciasCiudadanas = v; }

    public String getCompetenciasCiudadanasNivel() { return competenciasCiudadanasNivel; }
    public void setCompetenciasCiudadanasNivel(String v) { this.competenciasCiudadanasNivel = v; }

    public Double getIngles() { return ingles; }
    public void setIngles(Double v) { this.ingles = v; }

    public String getInglesNivel() { return inglesNivel; }
    public void setInglesNivel(String v) { this.inglesNivel = v; }

    public Double getFormulacionProyectos() { return formulacionProyectos; }
    public void setFormulacionProyectos(Double v) { this.formulacionProyectos = v; }

    public String getFormulacionProyectosNivel() { return formulacionProyectosNivel; }
    public void setFormulacionProyectosNivel(String v) { this.formulacionProyectosNivel = v; }

    public Double getPensamientoCientifico() { return pensamientoCientifico; }
    public void setPensamientoCientifico(Double v) { this.pensamientoCientifico = v; }

    public String getPensamientoCientificoNivel() { return pensamientoCientificoNivel; }
    public void setPensamientoCientificoNivel(String v) { this.pensamientoCientificoNivel = v; }

    public Double getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(Double v) { this.disenoSoftware = v; }

    public String getDisenoSoftwareNivel() { return disenoSoftwareNivel; }
    public void setDisenoSoftwareNivel(String v) { this.disenoSoftwareNivel = v; }
}