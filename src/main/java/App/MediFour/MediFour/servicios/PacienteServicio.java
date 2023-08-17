package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.enumeraciones.Rol;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.PacienteRepositorio;
import App.MediFour.MediFour.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PacienteServicio extends UsuarioServicio {

    @Autowired
    private PacienteRepositorio pacienteRepo;

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Transactional
    public void registrarPaciente(String nombre, String apellido, LocalDate fechaNacimiento,
            Integer dni, String telefono, String email, Boolean tieneObraSocial,
            String nombreObraSocial, Integer numeroAfiliado, String password, String password2) throws MiExcepcion {
        //HACER METODO para validar las excepciones
        //validar(nombre, apellido, fechaNacimiento, dni, telefono, email, tieneObraSocial,
        //nombreObraSocial, numeroAfiliado,  password,  password2); 
        Paciente paciente = new Paciente();

        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setDni(dni);
        paciente.setTelefono(telefono);
        paciente.setEmail(email);

        paciente.setTieneObraSocial(tieneObraSocial);
        if (tieneObraSocial) {
            validarObraSocial(nombreObraSocial, numeroAfiliado);
            paciente.setNombreObraSocial(nombreObraSocial);
            paciente.setNumeroAfiliado(numeroAfiliado);
        } else {
            paciente.setNombreObraSocial(null);
            paciente.setNumeroAfiliado(null);
        }

        paciente.setActivo(true);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setRol(Rol.USER); //por defecto le damos el rol de user en vez de admin
        pacienteRepo.save(paciente);
    }

    private void validarObraSocial(String nombreObraSocial, Integer numeroAfiliado) throws MiExcepcion {
        if (nombreObraSocial == null || nombreObraSocial.isEmpty()) {
            throw new MiExcepcion("El nombre de su obra social no puede ser nulo o estar vacío!");
        }
        if (numeroAfiliado == null) {
            throw new MiExcepcion("El número de afiliado no puede ser nulo!");
        }
    }

    public List<Paciente> listarPacientesActivos() {
        return pacienteRepo.findByActivoTrue();
    }

    public List<Paciente> listarTodosPacientes() {
        return pacienteRepo.findAll();
    }

    @Transactional
    public void bajaPaciente(String id) throws MiExcepcion {
        if (id == null || id.isEmpty()) {
            throw new MiExcepcion("El id ingresado no puede ser nulo o estar vacio");
        }
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Paciente paciente = (Paciente) respuesta.get();
            paciente.setActivo(false); // Establece el estado del paciente como inactivo
            usuarioRepo.save(paciente);
        }
    }

//    @Transactional
//    public void actualizarPaciente(String id, String nombre, String apellido, LocalDate fechaNacimiento,
//            Integer dni, String telefono, String email, Boolean tieneObraSocial,
//            String nombreObraSocial, Integer numeroAfiliado, String password, String password2) throws MiExcepcion {
////        validar(nombre, apellido, fechaNacimiento, dni, telefono, email, tieneObraSocial,
////                nombreObraSocial, numeroAfiliado);
//        Optional<Paciente> respuesta = usuarioRepo.findById(id);
//        if (respuesta.isPresent()) {
//            Paciente paciente = respuesta.get();
//
//            paciente.setNombre(nombre);
//            paciente.setApellido(apellido);
//            paciente.setFechaNacimiento(fechaNacimiento);
//            paciente.setDni(dni);
//            paciente.setTelefono(telefono);
//            paciente.setEmail(email);
//
//            paciente.setTieneObraSocial(tieneObraSocial);
//            if (tieneObraSocial) {
//                validar(nombreObraSocial, numeroAfiliado);
//                paciente.setNombreObraSocial(nombreObraSocial);
//                paciente.setNumeroAfiliado(numeroAfiliado);
//            } else {
//                paciente.setNombreObraSocial(null);
//                paciente.setNumeroAfiliado(null);
//            }
//
//            paciente.setActivo(true);
//            paciente.setPassword(new BCryptPasswordEncoder().encode(password));
//
////            paciente.setRol(Rol.USER); //por defecto le damos el rol de user en vez de admin
////            String idImagen = null;
////            if (paciente.getImagen() != null) {
////                idImagen = paciente.getImagen().getId();
////            }
////            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
////            paciente.setImagen(imagen);
//            usuarioRepo.save(paciente);
//        }
//    }

}
