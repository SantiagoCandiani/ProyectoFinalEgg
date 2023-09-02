package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Imagen;
import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.enumeraciones.ObraSocial;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteServicio extends UsuarioServicio {

    @Autowired
    private PacienteRepositorio pacienteRepo;

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarPaciente(MultipartFile archivo, String nombre, String apellido, LocalDate fechaNacimiento,
            Integer dni, String telefono, String email, Boolean tieneObraSocial,
            ObraSocial obraSocial, Integer numeroAfiliado, String password, String password2) throws MiExcepcion {

        Paciente paciente = new Paciente();
        usuarioServicio.validar(nombre, apellido, fechaNacimiento, dni, telefono, email, password, password2);

        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setDni(dni);
        paciente.setTelefono(telefono);
        paciente.setEmail(email);

        paciente.setTieneObraSocial(tieneObraSocial);
        // Si tiene obra social, se establece la obra social y el número de afiliado
        if (tieneObraSocial) {
            if (obraSocial != null) {
                paciente.setObraSocial(obraSocial);
            } else {
                throw new MiExcepcion("Debe seleccionar una obra social si tiene obra social.");
            }
            if (numeroAfiliado != null) {
                paciente.setNumeroAfiliado(numeroAfiliado);
            } else {
                throw new MiExcepcion("El número de afiliado no puede ser nulo si tiene obra social.");
            }
        } else {
            // Si no tiene obra social, se establecen los campos a null
            paciente.setObraSocial(null);
            paciente.setNumeroAfiliado(null);
        }

        paciente.setActivo(true);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setRol(Rol.USER); //por defecto le damos el rol de user en vez de admin

// Comprobar si se proporcionó un nuevo archivo de imagen y guardarla
        if (archivo != null && !archivo.isEmpty()) {
            Imagen imagen = imagenServicio.guardar(archivo);
            paciente.setImagen(imagen);
        } else {
            // Si no se proporcionó una imagen, asigna la imagen por defecto desde el servicio de Imagen
            Imagen imagenPorDefecto = imagenServicio.ImagenPacientePorDefecto();
            if (imagenPorDefecto != null) {
                paciente.setImagen(imagenPorDefecto);
            } else {
                // Si no se pudo obtener la imagen por defecto, maneja el caso de error de alguna manera
                throw new MiExcepcion("No se pudo obtener la imagen por defecto.");
            }
        }

        pacienteRepo.save(paciente);
    }

    public List<Paciente> listarPacientesActivos() {
        return pacienteRepo.findByActivoTrue();
    }

    public Paciente listarPacienteXdni(Integer dni) {
        return pacienteRepo.buscarPorDNI(dni);
    }

    public List<Paciente> listarPacientesXobraSocial(ObraSocial obraSocial) {
        return pacienteRepo.buscarPorObraSocial(obraSocial);
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

    @Transactional
    public void altaPaciente(String id) throws MiExcepcion {
        if (id == null || id.isEmpty()) {
            throw new MiExcepcion("El id ingresado no puede ser nulo o estar vacio");
        }
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Paciente paciente = (Paciente) respuesta.get();
            paciente.setActivo(true); // Establece el estado del paciente como activo
            usuarioRepo.save(paciente);
        }
    }

    @Transactional
    public void actualizarPaciente(MultipartFile archivo, String id, String nombre, String apellido, LocalDate fechaNacimiento,
            Integer dni, String telefono, String email, Boolean tieneObraSocial,
            ObraSocial obraSocial, Integer numeroAfiliado, String password, String password2) throws MiExcepcion {

        usuarioServicio.validar(nombre, apellido, fechaNacimiento, dni, telefono, email, password, password2);

        System.out.println("Entre a MODIFICAR"); // Imprime la información del paciente en la consola

        Optional<Paciente> respuesta = pacienteRepo.findById(id);
        if (respuesta.isPresent()) {
            Paciente paciente = respuesta.get();

            System.out.println("Paciente en actualizarPaciente del SERVICIO: " + paciente.toString());

            paciente.setNombre(nombre);
            paciente.setApellido(apellido);
            paciente.setFechaNacimiento(fechaNacimiento);
            paciente.setDni(dni);
            paciente.setTelefono(telefono);
            paciente.setEmail(email);

            paciente.setTieneObraSocial(tieneObraSocial);
            // Si tiene obra social, se establece la obra social y el número de afiliado
            if (tieneObraSocial) {
                if (obraSocial != null) {
                    paciente.setObraSocial(obraSocial);
                } else {
                    throw new MiExcepcion("Debe seleccionar una obra social si tiene obra social.");
                }
                if (numeroAfiliado != null) {
                    paciente.setNumeroAfiliado(numeroAfiliado);
                } else {
                    throw new MiExcepcion("El número de afiliado no puede ser nulo si tiene obra social.");
                }
            } else {
                // Si no tiene obra social, se establecen los campos a null
                paciente.setObraSocial(null);
                paciente.setNumeroAfiliado(null);
            }

            paciente.setPassword(new BCryptPasswordEncoder().encode(password));
            // Comprobar si se proporcionó un nuevo archivo de imagen
            if (archivo != null && !archivo.isEmpty()) {
                String idImagen = null;
                if (paciente.getImagen() != null) {
                    idImagen = paciente.getImagen().getId();
                }
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                paciente.setImagen(imagen);
            }
            System.out.println("Paciente en actualizarPaciente del SERVICIO: " + paciente.toString());

            pacienteRepo.save(paciente);
        }
    }

    public Usuario getOne(String id) {
        return pacienteRepo.getOne(id);
    }

    public Paciente pacientePorID(String id) {
        return pacienteRepo.buscarPorId(id);
    }

}//Class
