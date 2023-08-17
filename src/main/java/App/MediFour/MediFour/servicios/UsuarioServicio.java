package App.MediFour.MediFour.servicios;

import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.repositorios.UsuarioRepositorio;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
