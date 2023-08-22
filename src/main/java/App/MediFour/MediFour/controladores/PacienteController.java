package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.enumeraciones.ObraSocial;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.repositorios.PacienteRepositorio;
import App.MediFour.MediFour.servicios.PacienteServicio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("tieneObraSocial", false);
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
            @RequestParam(required = false) MultipartFile archivo, ModelMap modelo) {

        try {
            Boolean tieneObraSocialBool = tieneObraSocial != null && tieneObraSocial.equals("true");
            pacienteServicio.registrarPaciente(archivo, nombre, apellido, fechaNacimiento, dni, telefono, email, tieneObraSocial, obraSocial, numeroAfiliado, password, password2);

            modelo.put("exito", "El paciente fue registrado correctamente!");

        } catch (MiExcepcion ex) {

            modelo.put("error", ex.getMessage());
            return "paciente_form.html";
        }
        return "redirect:/paciente/listar";
    }

    @GetMapping("/listar") //localhost:8080/paciente/listar
    public String listarPacientesActivos(Model model) {
        List<Paciente> pacientes = pacienteServicio.listarPacientesActivos();
        model.addAttribute("pacientes", pacientes);

        return "paciente_list";
    }

    @GetMapping("/bajaPaciente/{id}")
    public String bajaPaciente(@PathVariable String id, ModelMap model) throws MiExcepcion {
        pacienteServicio.bajaPaciente(id);
        return "redirect:/paciente/listar";
    }
    
  /*  @PostMapping("/modificar/{id}")
    public String modificarPaciente(@PathVariable String id,
            @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam Integer dni, @RequestParam String telefono,
            @RequestParam String email, @RequestParam Boolean tieneObraSocial,
            @RequestParam(required = false) String nombreObraSocial,
            @RequestParam(required = false) Integer numeroAfiliado,
            @RequestParam String password, @RequestParam String password2) {

        try {
            pacienteServicio.actualizarPaciente(id, nombre, apellido, fechaNacimiento, dni,
                    telefono, email, tieneObraSocial, nombreObraSocial, numeroAfiliado,
                    password, password2);
            return "redirect:/paciente/listar";
        } catch (MiExcepcion e) {
            // Manejo de excepciones si es necesario
            return "redirect:/paciente/modificar-form/" + id;
        }
    } */

}
