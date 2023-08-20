package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> aux = new ArrayList();
        List<Usuario> usuarios = new ArrayList();
        aux = usuarioRepo.findAll();
        for (Usuario usuario : aux) {
            if (usuario.getActivo().equals(Boolean.TRUE)) {
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    @Transactional
    public void eliminarUsuario(String id) {
        Optional<Usuario> resp = usuarioRepo.findById(id);
        if (resp.isPresent()) {
            Usuario user = (Usuario) (resp.get());
            user.setActivo(Boolean.FALSE);
            usuarioRepo.save(user);
        }
    }

    public void validar(String nombre, String apellido, LocalDate fechaNacimiento,
            Integer dni, String telefono, String email, String password, String password2) throws MiExcepcion {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no puede ser nulo o estar vacío.");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiExcepcion("El apellido no puede ser nulo o estar vacío.");
        }
        if (fechaNacimiento == null) {
            throw new MiExcepcion("La fecha de nacimiento no puede ser nula.");
        }

        if (dni == null || dni < 999) {
            throw new MiExcepcion("El DNI no puede ser nulo y debe contener al menos 4 dígitos.");
        }
        if (usuarioRepo.buscarPorDni(dni) != null) {
            throw new MiExcepcion("El DNI está en uso.");
        }
        if (telefono == null || telefono.isEmpty() || telefono.length() <= 4) {
            throw new MiExcepcion("El teléfono no puede estar vacío y debe tener más de 4 caracteres.");
        }
        if (usuarioRepo.buscarPorTelefono(telefono) != null) {
            throw new MiExcepcion("El teléfono ya está en uso.");
        }
        if (email == null || email.isEmpty()) {
            throw new MiExcepcion("El email no puede ser nulo o estar vacío.");
        }
        if (usuarioRepo.buscarPorEmail(email) != null) {
            throw new MiExcepcion("El email ya está en uso.");
        }
        if (password == null || password.isEmpty() || password.length() <= 4) {
            throw new MiExcepcion("La contraseña no puede estar vacía y debe tener más de 4 caracteres.");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //prueba
}
