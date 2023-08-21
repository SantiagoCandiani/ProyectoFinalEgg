package App.MediFour.MediFour.entidades;

import App.MediFour.MediFour.enumeraciones.DiaSemana;
import App.MediFour.MediFour.enumeraciones.Especialidad;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Profesional")
public class Profesional extends Usuario {

    protected String matricula;
    @Enumerated(EnumType.STRING)
    protected Especialidad especialidad;
    @Enumerated(EnumType.STRING)
    protected List<DiaSemana> diasDisponibles;
    protected Integer horarioEntrada;
    protected Integer horarioSalida;
    protected Double precioConsulta;
    protected Double reputacion;
}
