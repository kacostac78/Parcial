package com.saberpro.app.config;

import com.saberpro.app.entity.*;
import com.saberpro.app.entity.PagoSaberPro.EstadoPago;
import com.saberpro.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UsuarioRepository usuarioRepo,
            CoordinadorRepository coordinadorRepo,
            EstudianteRepository estudianteRepo,
            ResultadoRepository resultadoRepo,
            DirectorRepository directorRepo,
            DocenteRepository docenteRepo,
            PagoSaberProRepository pagoRepo,
            PasswordEncoder encoder) {

        return args -> {

            // ── ADMIN ─────────────────────────────────────────────
            usuarioRepo.save(new Usuario("admin@saberpro.edu.co", encoder.encode("admin123"), Rol.ADMIN));

            // ── DIRECTORES ────────────────────────────────────────
            Director dirSistemas = new Director();
            dirSistemas.setTipoDocumento("CC");
            dirSistemas.setNumeroDocumento("1000000010");
            dirSistemas.setPrimerNombre("Carlos");
            dirSistemas.setPrimerApellido("Mendoza");
            dirSistemas.setSegundoApellido("Ruiz");
            dirSistemas.setCorreo("director.sistemas@saberpro.edu.co");
            dirSistemas.setTelefono("3001111111");
            dirSistemas.setNombreCoordinacion("Ingeniería de Sistemas");
            dirSistemas.setActivo(true);
            directorRepo.save(dirSistemas);
            usuarioRepo.save(new Usuario("director.sistemas@saberpro.edu.co", encoder.encode("dir123"), Rol.DIRECTOR));

            Director dirIndustrial = new Director();
            dirIndustrial.setTipoDocumento("CC");
            dirIndustrial.setNumeroDocumento("1000000011");
            dirIndustrial.setPrimerNombre("Laura");
            dirIndustrial.setPrimerApellido("Gómez");
            dirIndustrial.setSegundoApellido("Torres");
            dirIndustrial.setCorreo("director.industrial@saberpro.edu.co");
            dirIndustrial.setTelefono("3002222222");
            dirIndustrial.setNombreCoordinacion("Ingeniería Industrial");
            dirIndustrial.setActivo(true);
            directorRepo.save(dirIndustrial);
            usuarioRepo.save(new Usuario("director.industrial@saberpro.edu.co", encoder.encode("dir123"), Rol.DIRECTOR));

            Director dirSoftware = new Director();
            dirSoftware.setTipoDocumento("CC");
            dirSoftware.setNumeroDocumento("1000000012");
            dirSoftware.setPrimerNombre("Pedro");
            dirSoftware.setPrimerApellido("Castro");
            dirSoftware.setSegundoApellido("Vega");
            dirSoftware.setCorreo("director.software@saberpro.edu.co");
            dirSoftware.setTelefono("3003333333");
            dirSoftware.setNombreCoordinacion("Tecnología en Desarrollo de Software");
            dirSoftware.setActivo(true);
            directorRepo.save(dirSoftware);
            usuarioRepo.save(new Usuario("director.software@saberpro.edu.co", encoder.encode("dir123"), Rol.DIRECTOR));

            // ── COORDINADOR ───────────────────────────────────────
            Coordinador coord = new Coordinador();
            coord.setTipoDocumento("CC");
            coord.setNumeroDocumento("1000000002");
            coord.setPrimerNombre("María");
            coord.setSegundoNombre("Fernanda");
            coord.setPrimerApellido("López");
            coord.setSegundoApellido("Rodríguez");
            coord.setCorreo("coordinador@saberpro.edu.co");
            coord.setTelefono("3001234567");
            coord.setArea("Ingeniería de Sistemas");
            coord.setActivo(true);
            coordinadorRepo.save(coord);
            usuarioRepo.save(new Usuario("coordinador@saberpro.edu.co", encoder.encode("admin123"), Rol.COORDINADOR));

            // ── DOCENTE ───────────────────────────────────────────
            Docente docente = new Docente();
            docente.setTipoDocumento("CC");
            docente.setNumeroDocumento("1000000020");
            docente.setPrimerNombre("Jorge");
            docente.setPrimerApellido("Ramírez");
            docente.setSegundoApellido("Silva");
            docente.setCorreo("docente@saberpro.edu.co");
            docente.setTelefono("3004444444");
            docente.setPrograma("Ingeniería de Sistemas");
            docente.setActivo(true);
            docenteRepo.save(docente);
            usuarioRepo.save(new Usuario("docente@saberpro.edu.co", encoder.encode("doc123"), Rol.DOCENTE));

            // ── ESTUDIANTES DEL EXCEL ─────────────────────────────
            Estudiante e1 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030678901","Juan Sebastian","","BARBOSA","Gomez","juan.barbosa@estudiante.universidad.edu.co","","EK20183007722",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e1, LocalDate.of(2025,6,15), 200.0, 128.0,"Nivel 2", 182.0,"Nivel 3", 202.0,"Nivel 4", 206.0,"Nivel 4", 183.0,"B1", 185.0,"Nivel 3", 160.0,"Nivel 3", 197.0,"Nivel 4");
            crearPagoAceptado(pagoRepo, e1, coord, LocalDate.of(2025,5,1));

            Estudiante e2 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030689123","Maria Alejandra","","QUINTERO","Rodriguez","maria.quintero@estudiante.universidad.edu.co","","EK20183009123",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e2, LocalDate.of(2025,6,15), 165.0, 125.0,"Nivel 1", 151.0,"Nivel 2", 179.0,"Nivel 3", 205.0,"Nivel 4", 208.0,"B2", 160.0,"Nivel 2", 155.0,"Nivel 2", 170.0,"Nivel 3");
            crearPagoAceptado(pagoRepo, e2, coord, LocalDate.of(2025,5,1));

            Estudiante e3 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030698765","Carlos Andres","","PARRA","Martinez","carlos.parra@estudiante.universidad.edu.co","","EK20183040545",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e3, LocalDate.of(2025,6,15), 164.0, 159.0,"Nivel 3", 172.0,"Nivel 3", 182.0,"Nivel 4", 165.0,"Nivel 3", 165.0,"A2", 170.0,"Nivel 3", 158.0,"Nivel 2", 163.0,"Nivel 3");
            crearPagoAceptado(pagoRepo, e3, coord, LocalDate.of(2025,5,2));

            Estudiante e4 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030701234","Laura Camila","","ANAYA","Lopez","laura.anaya@estudiante.universidad.edu.co","","EK20183056234",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e4, LocalDate.of(2025,6,15), 160.0, 146.0,"Nivel 2", 199.0,"Nivel 4", 157.0,"Nivel 3", 147.0,"Nivel 2", 147.0,"A2", 155.0,"Nivel 2", 162.0,"Nivel 3", 158.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e4, coord, LocalDate.of(2025,5,2));

            Estudiante e5 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030712345","Daniel Felipe","","FLOR","Perez","daniel.flor@estudiante.universidad.edu.co","","EK20183025335",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e5, LocalDate.of(2025,6,15), 160.0, 198.0,"Nivel 4", 153.0,"Nivel 2", 147.0,"Nivel 2", 146.0,"Nivel 2", 146.0,"A2", 152.0,"Nivel 2", 158.0,"Nivel 2", 163.0,"Nivel 3");
            crearPagoAceptado(pagoRepo, e5, coord, LocalDate.of(2025,5,3));

            Estudiante e6 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030723456","Sofia Valentina","","GARCIA","Ruiz","sofia.garcia@estudiante.universidad.edu.co","","EK20183122648",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e6, LocalDate.of(2025,6,15), 157.0, 179.0,"Nivel 3", 172.0,"Nivel 3", 158.0,"Nivel 3", 136.0,"Nivel 1", 136.0,"A1", 148.0,"Nivel 2", 145.0,"Nivel 2", 153.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e6, coord, LocalDate.of(2025,5,3));

            Estudiante e7 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030734567","Jose David","","MANOSALVA","Castro","jose.manosalva@estudiante.universidad.edu.co","","EK20183064605",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e7, LocalDate.of(2025,6,15), 153.0, 115.0,"Nivel 1", 152.0,"Nivel 2", 159.0,"Nivel 3", 165.0,"Nivel 3", 165.0,"A2", 148.0,"Nivel 2", 142.0,"Nivel 2", 157.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e7, coord, LocalDate.of(2025,5,4));

            Estudiante e8 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030745678","Ana Maria","","MENDOZA","Ortiz","ana.mendoza@estudiante.universidad.edu.co","","EK20183187351",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e8, LocalDate.of(2025,6,15), 151.0, 132.0,"Nivel 2", 123.0,"Nivel 1", 125.0,"Nivel 1", 204.0,"Nivel 4", 204.0,"B2", 148.0,"Nivel 2", 138.0,"Nivel 1", 143.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e8, coord, LocalDate.of(2025,5,4));

            Estudiante e9 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030756789","Santiago","","BELTRAN","Ramirez","santiago.beltran@estudiante.universidad.edu.co","","EK20183233820",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e9, LocalDate.of(2025,6,15), 150.0, 86.0,"Nivel 1", 187.0,"Nivel 3", 160.0,"Nivel 3", 148.0,"Nivel 2", 148.0,"A2", 145.0,"Nivel 2", 140.0,"Nivel 1", 152.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e9, coord, LocalDate.of(2025,5,5));

            Estudiante e10 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030767890","Valentina","","SANTAMARIA","Gonzalez","valentina.santamaria@estudiante.universidad.edu.co","","EK20183030016",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e10, LocalDate.of(2025,6,15), 150.0, 175.0,"Nivel 3", 149.0,"Nivel 2", 145.0,"Nivel 2", 125.0,"Nivel 1", 125.0,"A1", 143.0,"Nivel 2", 148.0,"Nivel 2", 152.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e10, coord, LocalDate.of(2025,5,5));

            Estudiante e11 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030778901","Andres Felipe","","SANABRIA","Pedraza","andres.sanabria@estudiante.universidad.edu.co","","EK20183045678",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e11, LocalDate.of(2025,6,15), 148.0, 209.0,"Nivel 4", 117.0,"Nivel 1", 147.0,"Nivel 2", 147.0,"Nivel 2", 120.0,"A1", 140.0,"Nivel 1", 138.0,"Nivel 1", 145.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e11, coord, LocalDate.of(2025,5,6));

            Estudiante e12 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030789012","Luisa Fernanda","","MORA","Jimenez","luisa.mora@estudiante.universidad.edu.co","","EK20183056789",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e12, LocalDate.of(2025,6,15), 147.0, 160.0,"Nivel 3", 148.0,"Nivel 2", 140.0,"Nivel 2", 136.0,"Nivel 1", 136.0,"A1", 138.0,"Nivel 1", 145.0,"Nivel 2", 148.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e12, coord, LocalDate.of(2025,5,6));

            Estudiante e13 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030800123","Juan Pablo","","VARGAS","Herrera","juan.vargas@estudiante.universidad.edu.co","","EK20183067890",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e13, LocalDate.of(2025,6,15), 145.0, 138.0,"Nivel 1", 160.0,"Nivel 3", 142.0,"Nivel 2", 130.0,"Nivel 1", 130.0,"A1", 138.0,"Nivel 1", 142.0,"Nivel 2", 146.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e13, coord, LocalDate.of(2025,5,7));

            Estudiante e14 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030811234","Camila Andrea","","ROJAS","Navarro","camila.rojas@estudiante.universidad.edu.co","","EK20183078901",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, e14, LocalDate.of(2025,6,15), 143.0, 155.0,"Nivel 2", 138.0,"Nivel 1", 145.0,"Nivel 2", 128.0,"Nivel 1", 128.0,"A1", 135.0,"Nivel 1", 140.0,"Nivel 1", 143.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e14, coord, LocalDate.of(2025,5,7));

            Estudiante e15 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030822345","Miguel Angel","","TORRES","Castillo","miguel.torres@estudiante.universidad.edu.co","","EK20183089012",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultadoAnulado(resultadoRepo, e15, LocalDate.of(2025,6,15));
            crearPagoAceptado(pagoRepo, e15, coord, LocalDate.of(2025,5,8));

            Estudiante e16 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030833456","Paula Andrea","","SILVA","Moreno","paula.silva@estudiante.universidad.edu.co","","EK20183090123",8,"Tecnología en Desarrollo de Software","Tecnológico",dirSoftware);
            crearResultado(resultadoRepo, e16, LocalDate.of(2025,6,15), 172.0, 165.0,"Nivel 3", 178.0,"Nivel 3", 168.0,"Nivel 3", 175.0,"Nivel 3", 175.0,"B1", 168.0,"Nivel 3", 162.0,"Nivel 3", 170.0,"Nivel 3");
            crearPagoAceptado(pagoRepo, e16, coord, LocalDate.of(2025,5,8));

            Estudiante e17 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030844567","David Alejandro","","PEREZ","Gutierrez","david.perez@estudiante.universidad.edu.co","","EK20183101234",8,"Tecnología en Desarrollo de Software","Tecnológico",dirSoftware);
            crearResultado(resultadoRepo, e17, LocalDate.of(2025,6,15), 155.0, 148.0,"Nivel 2", 162.0,"Nivel 3", 152.0,"Nivel 2", 158.0,"Nivel 2", 158.0,"A2", 150.0,"Nivel 2", 148.0,"Nivel 2", 155.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e17, coord, LocalDate.of(2025,5,9));

            Estudiante e18 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030855678","Natalia","","ACOSTA","Vargas","natalia.acosta@estudiante.universidad.edu.co","","EK20183112345",10,"Ingeniería Industrial","Profesional",dirIndustrial);
            crearResultado(resultadoRepo, e18, LocalDate.of(2025,6,15), 168.0, 170.0,"Nivel 3", 165.0,"Nivel 3", 172.0,"Nivel 3", 160.0,"Nivel 3", 160.0,"B1", 165.0,"Nivel 3", 158.0,"Nivel 2", 168.0,"Nivel 3");
            crearPagoAceptado(pagoRepo, e18, coord, LocalDate.of(2025,5,9));

            Estudiante e19 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1030866789","Sebastian","","MORA","Delgado","sebastian.mora@estudiante.universidad.edu.co","","EK20183123456",10,"Ingeniería Industrial","Profesional",dirIndustrial);
            crearResultado(resultadoRepo, e19, LocalDate.of(2025,6,15), 142.0, 135.0,"Nivel 1", 148.0,"Nivel 2", 138.0,"Nivel 1", 145.0,"Nivel 2", 125.0,"A1", 138.0,"Nivel 1", 140.0,"Nivel 1", 142.0,"Nivel 2");
            crearPagoAceptado(pagoRepo, e19, coord, LocalDate.of(2025,5,10));

            // ── Estudiantes de prueba originales ──────────────────
            Estudiante est1 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1098765432","Juann","Sebastián","Ramírez","Torres","estudiante1@saberpro.edu.co","3157894561","REG-2024-001",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, est1, LocalDate.of(2024,11,15), 250.0, 245.0,"Superior", 255.0,"Superior", 248.0,"Superior", 240.0,"Superior", 85.0,"B2", 252.0,"Superior", 258.0,"Superior", 260.0,"Superior");
            crearPagoAceptado(pagoRepo, est1, coord, LocalDate.of(2024,10,15));

            Estudiante est2 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1098765433","Ana","María","Gómez","Díaz","estudiante2@saberpro.edu.co","3167894562","REG-2024-002",6,"Tecnología en Desarrollo de Software","Tecnológico",dirSoftware);
            crearResultado(resultadoRepo, est2, LocalDate.of(2025,6,15), 200.0, 128.0,"Nivel 2", 182.0,"Nivel 3", 202.0,"Nivel 4", 206.0,"Nivel 4", 183.0,"B1", 185.0,"Nivel 3", 160.0,"Nivel 3", 197.0,"Nivel 4");
            crearPagoAceptado(pagoRepo, est2, coord, LocalDate.of(2025,5,10));

            Estudiante est3 = crearEstudiante(estudianteRepo, usuarioRepo, encoder, "CC","1098765434","Luis","Fernando","Pérez","Santos","estudiante3@saberpro.edu.co","3177894563","REG-2024-003",10,"Ingeniería de Sistemas","Profesional",dirSistemas);
            crearResultado(resultadoRepo, est3, LocalDate.of(2024,11,15), 75.0, 72.0,"Nivel 1", 78.0,"Nivel 1", 70.0,"Nivel 1", 80.0,"Nivel 1", 65.0,"A2", 68.0,"Nivel 1", 71.0,"Nivel 1", 74.0,"Nivel 1");
            crearPagoAceptado(pagoRepo, est3, coord, LocalDate.of(2024,10,16));

            System.out.println("✅ Datos de prueba cargados correctamente.");
        };
    }

    // ── Helpers ───────────────────────────────────────────────────
    private Estudiante crearEstudiante(
            EstudianteRepository estudianteRepo,
            UsuarioRepository usuarioRepo,
            PasswordEncoder encoder,
            String tipoDoc, String numDoc,
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            String correo, String telefono,
            String numReg, int semestre,
            String programa, String tipoPrograma,
            Director director) {

        Estudiante e = new Estudiante();
        e.setTipoDocumento(tipoDoc);
        e.setNumeroDocumento(numDoc);
        e.setPrimerNombre(primerNombre);
        e.setSegundoNombre(segundoNombre.isEmpty() ? null : segundoNombre);
        e.setPrimerApellido(primerApellido);
        e.setSegundoApellido(segundoApellido.isEmpty() ? null : segundoApellido);
        e.setCorreo(correo);
        e.setTelefono(telefono.isEmpty() ? null : telefono);
        e.setNumeroRegistro(numReg);
        e.setSemestre(semestre);
        e.setPrograma(programa);
        e.setTipoPrograma(tipoPrograma);
        e.setActivo(true);
        e.setDirector(director);
        estudianteRepo.save(e);
        usuarioRepo.save(new Usuario(correo, encoder.encode(numDoc), Rol.ESTUDIANTE));
        return e;
    }

    private void crearPagoAceptado(
            PagoSaberProRepository pagoRepo,
            Estudiante estudiante,
            Coordinador coordinador,
            LocalDate fecha) {

        PagoSaberPro pago = new PagoSaberPro();
        pago.setEstudiante(estudiante);
        pago.setNombreArchivo("comprobante_" + estudiante.getNumeroDocumento() + ".pdf");
        pago.setRutaArchivo("/pagos/comprobante_" + estudiante.getNumeroDocumento() + ".pdf");
        pago.setEstado(EstadoPago.ACEPTADO);
        pago.setFechaSubida(fecha.atStartOfDay());
        pago.setFechaRevision(fecha.plusDays(1).atStartOfDay());
        pago.setCoordinador(coordinador);
        pagoRepo.save(pago);
    }

    private void crearResultado(
            ResultadoRepository resultadoRepo,
            Estudiante est, LocalDate fecha,
            double global,
            double comEsc, String comEscNivel,
            double razCuant, String razCuantNivel,
            double lecCrit, String lecCritNivel,
            double compCiud, String compCiudNivel,
            double ingles, String inglesNivel,
            double formProy, String formProyNivel,
            double pensCient, String pensCientNivel,
            double disenoSoft, String disenoSoftNivel) {

        ResultadoSaberPro r = new ResultadoSaberPro();
        r.setEstudiante(est);
        r.setFechaExamen(fecha);
        r.setPuntajeGlobal(global);
        r.setNivelGlobal(calcularNivel(global));
        r.setComunicacionEscrita(comEsc);
        r.setComunicacionEscritaNivel(comEscNivel);
        r.setRazonamientoCuantitativo(razCuant);
        r.setRazonamientoCuantitativoNivel(razCuantNivel);
        r.setLecturaCritica(lecCrit);
        r.setLecturaCriticaNivel(lecCritNivel);
        r.setCompetenciasCiudadanas(compCiud);
        r.setCompetenciasCiudadanasNivel(compCiudNivel);
        r.setIngles(ingles);
        r.setInglesNivel(inglesNivel);
        r.setFormulacionProyectos(formProy);
        r.setFormulacionProyectosNivel(formProyNivel);
        r.setPensamientoCientifico(pensCient);
        r.setPensamientoCientificoNivel(pensCientNivel);
        r.setDisenoSoftware(disenoSoft);
        r.setDisenoSoftwareNivel(disenoSoftNivel);
        resultadoRepo.save(r);
    }

    private void crearResultadoAnulado(
            ResultadoRepository resultadoRepo,
            Estudiante est, LocalDate fecha) {
        ResultadoSaberPro r = new ResultadoSaberPro();
        r.setEstudiante(est);
        r.setFechaExamen(fecha);
        r.setPuntajeGlobal(null);
        r.setNivelGlobal("ANULADO");
        resultadoRepo.save(r);
    }

    private String calcularNivel(double puntaje) {
        if (puntaje > 241) return "Superior";
        if (puntaje >= 165) return "Nivel 3";
        if (puntaje >= 140) return "Nivel 2";
        return "Nivel 1";
    }
}