package App.MediFour.MediFour.entidades;

import App.MediFour.MediFour.enumeraciones.ObraSocial;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //Establezco que es una entidad/objeto y que voy a persistir sus datos
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Paciente")
public class Paciente extends Usuario {

    protected Boolean tieneObraSocial;
    @Enumerated(EnumType.STRING)
    protected ObraSocial obraSocial;
    protected Integer numeroAfiliado;

//----Relacion con otras entidades----//
    //    private TurnoAgendado turnoAgendado;
    //    private Profesional profesionalAsignado;
    //    private HistoriaClinica historiaClinica;
//-----------------------------------//
}
