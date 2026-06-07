package com.saberpro.app.repository;

import com.saberpro.app.entity.Estudiante;
import com.saberpro.app.entity.ResultadoSaberPro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ResultadoRepository extends JpaRepository<ResultadoSaberPro, Long> {
    List<ResultadoSaberPro> findByEstudiante(Estudiante estudiante);
    List<ResultadoSaberPro> findByEstudianteOrderByFechaExamenDesc(Estudiante estudiante);
    Optional<ResultadoSaberPro> findTopByEstudianteOrderByFechaExamenDesc(Estudiante estudiante);
    List<ResultadoSaberPro> findByEstudiante_Programa(String programa);
    List<ResultadoSaberPro> findAllByOrderByPuntajeGlobalDesc();
    List<ResultadoSaberPro> findByEstudiante_Director_NombreCoordinacion(String nombreCoordinacion);

    @Query("SELECT r FROM ResultadoSaberPro r WHERE " +
           "LOWER(r.estudiante.primerNombre) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(r.estudiante.primerApellido) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "r.estudiante.numeroDocumento LIKE CONCAT('%',:q,'%')")
    List<ResultadoSaberPro> buscarPorNombreOCedula(@Param("q") String query);
}