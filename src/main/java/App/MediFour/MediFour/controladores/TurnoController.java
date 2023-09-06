package App.MediFour.MediFour.controladores;

import App.MediFour.MediFour.entidades.Profesional;
import App.MediFour.MediFour.entidades.Turno;
import App.MediFour.MediFour.servicios.ProfesionalServicio;
import App.MediFour.MediFour.servicios.TurnoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/listar")
    public String obtenerTodosLosTurnos(Model model) {
        List<Turno> turno = turnoServicio.obtenerTodosLosTurnos();
        model.addAttribute("turnos", turno);
        return "turno_List.html";
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
