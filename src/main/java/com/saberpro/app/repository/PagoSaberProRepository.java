package com.saberpro.app.repository;

import com.saberpro.app.entity.Estudiante;
import com.saberpro.app.entity.PagoSaberPro;
import com.saberpro.app.entity.PagoSaberPro.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PagoSaberProRepository extends JpaRepository<PagoSaberPro, Long> {
    List<PagoSaberPro> findByEstudiante(Estudiante estudiante);
    List<PagoSaberPro> findByEstado(EstadoPago estado);
    Optional<PagoSaberPro> findTopByEstudianteOrderByFechaSubidaDesc(Estudiante estudiante);
    List<PagoSaberPro> findByEstudianteOrderByFechaSubidaDesc(Estudiante estudiante);
    List<PagoSaberPro> findByEstado_AndEstudiante_Programa(EstadoPago estado, String programa);
}