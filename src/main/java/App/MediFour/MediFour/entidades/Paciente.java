package App.MediFour.MediFour.entidades;

import javax.persistence.Entity;
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
    protected String nombreObraSocial;
    protected Integer numeroAfiliado;

//----Relacion con otras entidades----//
    //    private TurnoAgendado turnoAgendado;
    //    private Profesional profesionalAsignado;
    //    private HistoriaClinica historiaClinica;
//-----------------------------------//
}
