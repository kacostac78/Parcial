package com.saberpro.app.repository;

import com.saberpro.app.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByNumeroDocumento(String numeroDocumento);
}