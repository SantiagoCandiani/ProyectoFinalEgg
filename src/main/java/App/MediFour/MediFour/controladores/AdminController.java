package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);
        return "usuario_list";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list";
    }

    @GetMapping("/bajaUsuario/{id}")
    public String bajaUsuario(@PathVariable String id, ModelMap modelo) throws MiExcepcion {
        usuarioServicio.bajaUsuario(id);
        modelo.addAttribute("exito", "El usuario ha sido dado de baja");
        // return "redirect:/admin/usuarios";
        return "usuario_list";
    }

    @GetMapping("/altaUsuario/{id}")
    public String altaUsuario(@PathVariable String id, ModelMap modelo) throws MiExcepcion {
        usuarioServicio.altaUsuario(id);
        modelo.addAttribute("exito", "El usuario ha sido dado de alta");
        // return "redirect:/admin/usuarios";
        return "usuario_list";
    }

}
