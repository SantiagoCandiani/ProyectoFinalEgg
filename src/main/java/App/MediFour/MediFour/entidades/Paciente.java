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
    //    @OneToOne //Un paciente tiene una historia clínica
//    @JoinColumn(name = "historia_clinica_id") //Foreign Key: historia_clinica_id
//    private HistoriaClinica historiaClinica;
    //@OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY) //Usando mappedBy (mappedBy: indica cuál atributo de la entidad Turno es dueña del uno a muchos de forma única) indicas que la relación es unidireccional. Un ‘Paciente’ tiene muchos ‘Turnos’ pero un ‘Turno’ no tiene muchos pacientes.
    //private List<Turno> listaDeTurnos;
//-----------------------------------//
}
