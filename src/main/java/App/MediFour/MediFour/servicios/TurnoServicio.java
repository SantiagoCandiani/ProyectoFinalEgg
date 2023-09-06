/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.entidades.Profesional;
import App.MediFour.MediFour.entidades.Turno;
import App.MediFour.MediFour.enumeraciones.DiaSemana;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.TurnoRepositorio;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Usuario
 */
@Service
public class TurnoServicio {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private TurnoRepositorio turnoRepositorio;

//    public Turno crearTurno(String fecha, String hora, Boolean disponibilidad, String consulta, String idProfesional) throws MiExcepcion {
//        // Validación de los datos ingresados
//        validar(fecha, hora, consulta, idProfesional, disponibilidad);
//        // Obtener el profesional desde el servicio
//        Profesional profesional = (Profesional) profesionalServicio.getOne(idProfesional);
//        if (profesional == null) {
//            throw new MiExcepcion("no existe un profesional con este turno");
//        }
//        // Crear y guardar el turno
//        Turno turno = new Turno();
//        turno.setFecha(fecha);
//        turno.setHora(hora);
//        turno.setDisponibilidad(false); // Por defecto, el turno se crea como disponible
//        turno.setConsulta(consulta);
//        turno.setProfesional(profesional);
//
//        return turnoRepositorio.save(turno);
//    }
    @Transactional
    public List<Turno> generarTurnos(Profesional profesional) {
        List<Turno> turnos = new ArrayList<>();
        LocalTime horaActual = profesional.getHorarioEntrada();

        for (DiaSemana dia : profesional.getDiasDisponibles()) {
            DayOfWeek dayOfWeek = DiaSemana.toDayOfWeek(dia);

            while (horaActual.isBefore(profesional.getHorarioSalida())) {
                Turno turno = new Turno();
                turno.setProfesional(profesional);
                turno.setDisponibilidad(true);
                turno.setFecha(dayOfWeek.toString()); // Puedes ajustar el formato de fecha
                turno.setHora(horaActual.toString());

                turnos.add(turno);

                horaActual = horaActual.plus(30, ChronoUnit.MINUTES);
            }

            horaActual = profesional.getHorarioEntrada();
        }

        // Guardar los turnos en el repositorio si es necesario
        turnoRepositorio.saveAll(turnos);
        return turnos;
    }

    public List<Turno> obtenerTodosLosTurnos() {
        // Utiliza el repositorio para obtener todos los turnos almacenados
        return turnoRepositorio.findAll();
    }

    public List<Turno> listarTurnos() {
        try {

            List<Turno> turnos = turnoRepositorio.findAll();
            return turnos;
        } catch (Exception e) {
            System.err.println("Error al listar los turnos: " + e.getMessage());
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error
        }
    }

    public List<Paciente> obtenerNombresPacientesConTurnoPorMedico(String profesionalId) {
        List<Paciente> pacientes = turnoRepositorio.obtenerNombresPacientesConTurnoPorMedico(profesionalId);
        return pacientes;
    }

    @Transactional
    public void eliminarTurno(String id) {
        try {
            // Buscar un turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(id);

            // Verificar si hay un valor presente en el Optional "respuesta"
            // Si se encuentra el turno en la base de datos, eliminarlo
            respuesta.ifPresent(turno -> turnoRepositorio.delete(turno));
        } catch (Exception e) {
            System.err.println("No es posible eliminar el turno: " + e.getMessage());
        }
    }

    @Transactional
    public boolean actualizarDisponibilidad(String id, boolean nuevaDisponibilidad) {
        try {
            // Intentar buscar un turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(id);

            // Verificar si se encontró un turno con el ID proporcionado
            if (respuesta.isPresent()) {
                // Obtener el turno desde el Optional
                Turno turno = respuesta.get();

                // Actualizar la disponibilidad con el nuevo valor
                turno.setDisponibilidad(nuevaDisponibilidad);

                // Guardar los cambios en la base de datos
                turnoRepositorio.save(turno);

                return true; // Indicar que la actualización fue exitosa
            }

            // Si no se encontró un turno con el ID, retornar false
            return false;
        } catch (Exception e) {
            // Manejo de excepciones en caso de algún problema
            System.err.println("No es posible actualizar la disponibilidad: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean elegirTurno(String idTurno, Paciente paciente) throws MiExcepcion {
        try {
            // Buscar el turno por su ID en la base de datos
            Optional<Turno> respuesta = turnoRepositorio.findById(idTurno);

            // Verificar si se encontró un turno con el ID proporcionado
            if (respuesta.isPresent()) {
                // Obtener el turno desde el Optional
                Turno turno = respuesta.get();

                // Verificar si el turno está disponible
                if (!turno.isDisponibilidad()) {
                    throw new MiExcepcion("El turno no está disponible.");
                }

                // Asociar el paciente al turno
                turno.setPaciente(paciente);

                // Actualizar la disponibilidad del turno
                turno.setDisponibilidad(false);

                // Guardar los cambios en la base de datos
                turnoRepositorio.save(turno);

                return true; // Indicar que la elección fue exitosa
            }

            // Si no se encontró un turno con el ID, retornar false
            return false;
        } catch (Exception e) {
            // Manejo de excepciones en caso de algún problema
            System.err.println("No es posible elegir el turno: " + e.getMessage());
            return false;
        }
    }

    public Turno obtenerTurnoPorId(String id) throws MiExcepcion {
        try {
            Optional<Turno> respuesta = turnoRepositorio.findById(id);
            if (respuesta.isPresent()) {
                return respuesta.get();
            } else {
                throw new MiExcepcion("No se encontró el turno con el ID proporcionado.");
            }
        } catch (Exception e) {
            throw new MiExcepcion("Error al obtener el turno: " + e.getMessage());
        }
    }

    protected void validar(String fecha, String hora, String consulta, String idProfesional, Boolean disponibilidad)
            throws MiExcepcion {
        if (fecha == null || fecha.isEmpty()) {
            throw new MiExcepcion("Debe indicar uan fecha para el turno");
        }
        if (hora == null || hora.isEmpty()) {
            throw new MiExcepcion("Debe indicar un horario para el turno");
        }
        if (consulta == null || consulta.isEmpty()) {
            throw new MiExcepcion("Debe ingresar una descripcion de la visita");
        }

        if (idProfesional == null || idProfesional.isEmpty()) {
            throw new MiExcepcion("El turno debe tener un Profesional Asociado");
        }
        if (disponibilidad == null) {
            throw new MiExcepcion("El turno debe tener un estado");
        }

    }

}
