package com.saberpro.app.repository;

import com.saberpro.app.entity.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    Optional<Coordinador> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}