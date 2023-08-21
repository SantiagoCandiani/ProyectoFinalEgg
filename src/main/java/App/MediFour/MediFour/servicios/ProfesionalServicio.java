package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Profesional;
import App.MediFour.MediFour.enumeraciones.DiaSemana;
import App.MediFour.MediFour.enumeraciones.Especialidad;
import App.MediFour.MediFour.enumeraciones.Rol;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.ProfesionalRepositorio;
import App.MediFour.MediFour.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfesionalServicio extends UsuarioServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepo;

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarProfesional(String nombre, String apellido, LocalDate fechaNacimiento,
            Integer dni, String telefono, String email, String matricula,
            Especialidad especialidad, List<DiaSemana> diasDisponibles,
            Integer horarioEntrada, Integer horarioSalida, Double precioConsulta,
            Double reputacion, String password, String password2) throws MiExcepcion {

        Profesional profesional = new Profesional();
        usuarioServicio.validar(nombre, apellido, fechaNacimiento, dni, telefono, email, password, password2);

        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setFechaNacimiento(fechaNacimiento);
        profesional.setDni(dni);
        profesional.setTelefono(telefono);
        profesional.setEmail(email);

        // Configurar atributos específicos de Profesional
        profesional.setMatricula(matricula);
        profesional.setEspecialidad(especialidad);
        profesional.setDiasDisponibles(diasDisponibles);
        profesional.setHorarioEntrada(horarioEntrada);
        profesional.setHorarioSalida(horarioSalida);
        profesional.setPrecioConsulta(precioConsulta);
        profesional.setReputacion(0.0);

        profesional.setActivo(true);
        profesional.setPassword(new BCryptPasswordEncoder().encode(password));
        profesional.setRol(Rol.PROFESIONAL);

        profesionalRepo.save(profesional);
    }

}
