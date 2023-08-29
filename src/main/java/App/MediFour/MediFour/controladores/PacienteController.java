package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.entidades.Usuario;
import App.MediFour.MediFour.enumeraciones.ObraSocial;
import App.MediFour.MediFour.enumeraciones.Rol;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.PacienteRepositorio;
import App.MediFour.MediFour.servicios.PacienteServicio;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    private PacienteRepositorio pacienteRepo;

    @GetMapping("/registrar-form")
    public String mostrarFormularioRegistro(ModelMap modelo) {
        try {
            modelo.addAttribute("tieneObraSocial", false);
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "paciente_form.html";
        }
        return "paciente_form.html";
    }

    @PostMapping("/registrar")
    public String registrarPaciente(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam Integer dni,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam(required = false) Boolean tieneObraSocial,
            @RequestParam(required = false) ObraSocial obraSocial,
            @RequestParam(required = false) Integer numeroAfiliado,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam(required = false) MultipartFile archivo,
            ModelMap modelo) {

        try {
            //Boolean tieneObraSocialBool = tieneObraSocial != null && tieneObraSocial.equals("true");
            pacienteServicio.registrarPaciente(archivo, nombre, apellido, fechaNacimiento, dni, telefono, email, tieneObraSocial, obraSocial, numeroAfiliado, password, password2);

            modelo.put("exito", "El paciente fue registrado correctamente!");
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "paciente_form.html";
        }
        return "redirect:/login";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/listar") //localhost:8080/paciente/listar
    public String listarPacientesActivos(Model model) {
        List<Paciente> pacientes = pacienteServicio.listarPacientesActivos();
        model.addAttribute("pacientes", pacientes);

        return "paciente_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/consultaDni") //localhost:8080/paciente/consulta
    public String listarPacienteXdni(@RequestParam Integer dni, ModelMap model) {
        Paciente paciente = pacienteServicio.listarPacienteXdni(dni);
        model.addAttribute("paciente", paciente);

        return "paciente_consulta";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/consultaObra") //localhost:8080/paciente/consulta
    public String listarPacientesXobraSocial(@RequestParam("obra") String obraValue, ModelMap model) {
        ObraSocial obra = ObraSocial.valueOf(obraValue);//se transforma obraValue a enum ObraSocial
        List<Paciente> paciente = pacienteServicio.listarPacientesXobraSocial(obra);
        model.addAttribute("paciente", paciente);

        return "paciente_consulta";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/bajaPaciente/{id}")
    public String bajaPaciente(@PathVariable String id, ModelMap model) throws MiExcepcion {
        pacienteServicio.bajaPaciente(id);

        return "redirect:/paciente/listar";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/altaPaciente/{id}")
    public String altaPaciente(@PathVariable String id, ModelMap model) throws MiExcepcion {
        pacienteServicio.altaPaciente(id);

        return "redirect:/paciente/listar";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String mostrarPerfilPaciente(@PathVariable String id, ModelMap modelo, HttpSession session) {
        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", pacienteServicio.getOne(id));
            modelo.addAttribute("id", pacienteServicio.getOne(id).getId());
            return "paciente_perfil.html";

        } catch (Exception ex) {
            Rol[] roles = Rol.values();
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return "paciente_perfil.html";
        }
    }

    @PostMapping("/modificar/{id}")
    public String modificarPerfilPaciente(
            @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam Integer dni,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam(required = false) Boolean tieneObraSocial,
            @RequestParam(required = false) ObraSocial obraSocial,
            @RequestParam(required = false) Integer numeroAfiliado,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam(required = false) MultipartFile archivo,
            ModelMap modelo,
            HttpSession session) {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            // Aquí obtienes el objeto de usuario desde tu servicio o base de datos
            Usuario usuario = pacienteServicio.getOne(id);

            if (usuario != null) {
                modelo.addAttribute("paciente", usuario);

                // Llamar al servicio para actualizar los datos del paciente
                pacienteServicio.actualizarPaciente(archivo, id, nombre, apellido, fechaNacimiento, dni, telefono, email, tieneObraSocial, obraSocial, numeroAfiliado, password, password2);

                modelo.put("exito", "Tus datos fueron modificados correctamente!");
            } else {
                // Manejo de usuario nulo
                modelo.put("error", "No se encontró el usuario.");
            }
        } catch (MiExcepcion ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            // Aquí obtienes el objeto de usuario desde tu servicio o base de datos
            Usuario usuario = pacienteServicio.getOne(id);

            if (usuario != null) {
                modelo.addAttribute("paciente", usuario);
                modelo.addAttribute("id", usuario.getId());
                modelo.put("error", ex.getMessage());
                return "redirect:/paciente/perfil/" + id;
            } else {
                // Manejo de usuario nulo
                modelo.put("error", "No se encontró el usuario.");
            }
        }

        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/perfil/{id}") //localhost:8080/profesional/perfil
    public String mostrarPacientePerfil(@PathVariable String id, ModelMap model) {
        //pasa el ID de profesional solo por el path
        //TODO: linkear con lista de profesionales
        Paciente paciente = pacienteServicio.pacientePorID(id);
        model.addAttribute("paciente", paciente);
        return "paciente_perfil_ver.html";
    }
}//Class

