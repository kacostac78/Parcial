package com.saberpro.app.repository;

import com.saberpro.app.entity.Director;
import com.saberpro.app.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByCorreo(String correo);
    Optional<Estudiante> findByNumeroDocumento(String numeroDocumento);
    List<Estudiante> findByPrograma(String programa);
    List<Estudiante> findByDirector(Director director);
    List<Estudiante> findByDirector_NombreCoordinacion(String nombreCoordinacion);
    boolean existsByCorreo(String correo);
    boolean existsByNumeroDocumento(String numeroDocumento);

    @Query("SELECT e FROM Estudiante e WHERE " +
           "LOWER(e.primerNombre) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.primerApellido) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.segundoNombre) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.segundoApellido) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "e.numeroDocumento LIKE CONCAT('%',:q,'%')")
    List<Estudiante> buscarPorNombreODocumento(@Param("q") String query);
}