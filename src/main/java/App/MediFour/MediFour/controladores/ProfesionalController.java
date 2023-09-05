package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Profesional;
import App.MediFour.MediFour.enumeraciones.DiaSemana;
import App.MediFour.MediFour.enumeraciones.Especialidad;
import App.MediFour.MediFour.excepciones.MiExcepcion;
import App.MediFour.MediFour.servicios.ProfesionalServicio;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/profesional")
public class ProfesionalController {

    @Autowired
    private ProfesionalServicio profesionalServicio;

    @GetMapping("/registrar-form")
    public String mostrarFormularioRegistro(Model model) {
        return "profesional_form.html";
    }

    @PostMapping("/registrar")
    public String registrarProfesional(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam Integer dni,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String matricula,
            @RequestParam Especialidad especialidad,
            @RequestParam List<DiaSemana> diasDisponibles,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioEntrada,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioSalida,
            @RequestParam Double precioConsulta,
            @RequestParam(required = false) String observaciones,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam MultipartFile archivo,
            ModelMap modelo) {

        try {
            profesionalServicio.registrarProfesional(archivo, nombre, apellido, fechaNacimiento, dni, telefono, email, matricula, especialidad, diasDisponibles, horarioEntrada, horarioSalida, precioConsulta, observaciones, password, password2);

            modelo.put("exito", "El profesional fue registrado correctamente!");

        } catch (MiExcepcion ex) {

            modelo.put("error", ex.getMessage());
            return "profesional_form.html";
        }
        return "redirect:/login";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_PROFESIONAL')")

    @GetMapping("/listar") //localhost:8080/profesional/listar
    public String listarProfesionalesActivos(Model model) {
        List<Profesional> profesionales = profesionalServicio.listarProfesionalesActivos();
        model.addAttribute("profesionales", profesionales);

        return "lista_profesionales.HTML";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarAdmin") //localhost:8080/profesional/listar
    public String listarProfesionales(Model model) {
        List<Profesional> profesionales = profesionalServicio.listarTodosProfesionales();
        model.addAttribute("profesionales", profesionales);

        return "PanelAdminProfesionales";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/bajaProfesional/{id}")
    public String bajaProfesional(@PathVariable String id, ModelMap model) throws MiExcepcion {
        profesionalServicio.bajaProfesional(id);
        return "redirect:/profesional/listarAdmin";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/altaProfesional/{id}")
    public String altaProfesional(@PathVariable String id, ModelMap model) throws MiExcepcion {
        profesionalServicio.altaProfesional(id);
        return "redirect:/profesional/listarAdmin";
    }
    

    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/perfil/{id}") //localhost:8080/profesional/perfil
    public String mostrarProfesionalPerfil(@PathVariable String id, ModelMap model, HttpSession session) {

        
        Profesional profesional = profesionalServicio.profesionalPorID(id) ;
        //Profesional logueado = (Profesional) session.getAttribute("usuariosession");
        model.addAttribute("profesional", profesional);
        
        System.out.println("profesional Email: "+ profesional.getEmail());
        
        return "profesional_perfil";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROFESIONAL','ROLE_ADMIN')")
    @PostMapping("/perfil/{id}") //localhost:8080/profesional/perfil
    public String mostrarProfesionalPerfil(
            @RequestParam @PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam Integer dni,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String matricula,
            @RequestParam Especialidad especialidad,
            @RequestParam List<DiaSemana> diasDisponibles,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioEntrada,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioSalida,
            @RequestParam Double precioConsulta,
            @RequestParam String observaciones,
            @RequestParam MultipartFile archivo,
            ModelMap model) {

            try {

                profesionalServicio.actualizarProfesional(archivo, id, nombre, apellido, fechaNacimiento, dni, telefono, 
                                    email, matricula, especialidad, diasDisponibles, horarioEntrada, horarioSalida, 
                                    precioConsulta, observaciones, Boolean.TRUE);
                
                System.out.println("Estoy en Try");
                return "redirect:../listar";
            } catch (MiExcepcion ex) {
                model.put("error",ex.getMessage());
                System.out.println("Estoy en CAtch");
                return "profesional_perfil";
            }
            
            
            
        }
    
    
}//class

