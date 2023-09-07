package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Paciente;
import App.MediFour.MediFour.entidades.Profesional;
import App.MediFour.MediFour.entidades.Turno;
import App.MediFour.MediFour.servicios.ProfesionalServicio;
import App.MediFour.MediFour.servicios.TurnoServicio;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/turno")
public class TurnoController {

    private final TurnoServicio turnoServicio;
    private final ProfesionalServicio profesionalServicio; // Agrega el servicio de Profesional

    @Autowired
    public TurnoController(TurnoServicio servicioTurno, ProfesionalServicio servicioProfesional) {
        this.turnoServicio = servicioTurno;
        this.profesionalServicio = servicioProfesional;
    }

    // Endpoint para obtener todos los turnos
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROFESIONAL','ROLE_ADMIN')")
    @GetMapping("/listar")
    public String obtenerTodosLosTurnos(Model model, HttpSession session) {
        // Verifica si el usuario está autenticado utilizando la sesión
        if (session.getAttribute("usuariosession") == null) {
            System.out.println("Estoy en el if listar obtenerTodosLosTurnos");
            // Si no está autenticado, redirige al usuario a la página de inicio de sesión
            return "login.html";
        } else {
            List<Turno> turno = turnoServicio.obtenerTodosLosTurnos();
            model.addAttribute("turnos", turno);
            System.out.println("Estoy en el else listar obtenerTodosLosTurnos");
            return "turno_List.html";
        }
    }

    @PostMapping("/agendar-turno")
    public String agendarTurno(@RequestParam("turnoId") String turnoId, HttpSession session) {
        // Verifica si el usuario está autenticado utilizando la sesión
        Paciente paciente = (Paciente) session.getAttribute("usuariosession");
        if (paciente == null) {
            System.out.println("***** Entre if de agendarTurno");
            // Si no está autenticado, redirige al usuario a la página de inicio de sesión
            return "login.html";
        }

        // Llama al servicio para asignar el turno al paciente
        turnoServicio.asignarTurnoAPaciente(turnoId, paciente);
        System.out.println("***** Sali del if de agendarTurno dsp de asignarturno");
        // Obtiene la URL original almacenada en la variable de sesión
        String originalUrl = (String) session.getAttribute("originalUrl");
        if (originalUrl != null) {
            System.out.println("***** Entre if de originalUrl");

            // Redirige al usuario a la URL original
            session.removeAttribute("originalUrl");
            return "redirect:" + originalUrl;
        } else {
            System.out.println("***** Entre else de originalUrl");

            // Si no hay URL original, redirige al usuario a la página de lista de turnos
            return "redirect:/listar";
        }
    }

//    // Endpoint para obtener los turnos de un profesional por su ID
//    @GetMapping("/listar/{profesionalId}")
//    public String obtenerTurnosDeProfesional(@PathVariable String profesionalId, Model model) {
//        // Obtener el profesional por su ID
//        Profesional profesional = profesionalServicio.profesionalPorID(profesionalId);
//
//        if (profesional != null) {
//            // Obtener los turnos del profesional ordenados por fecha y hora
//            List<Turno> turnos = turnoServicio.obtenerTurnosDeProfesionalOrdenados(profesional);
//
//            // Agregar los turnos al modelo para mostrarlos en la vista
//            model.addAttribute("turnos", turnos);
//
//            // Resto de la lógica para mostrar los turnos en la vista
//            // ...
//            return "turno_List.html"; // Reemplaza con la vista adecuada
//        } else {
//            // Manejo de error si el profesional no existe
//            model.addAttribute("error", "Profesional no encontrado.");
//            return "pagina_de_error.html"; // Reemplaza con la vista de error adecuada
//        }
//    }
}//Class
