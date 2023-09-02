package App.MediFour.MediFour.entidades;

import App.MediFour.MediFour.enumeraciones.ObraSocial;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
 @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY) 
    private List<Turno> ListaDeturnos;
//----Relacion con otras entidades----//
    //    @OneToOne //Un paciente tiene una historia clínica
//    @JoinColumn(name = "historia_clinica_id") //Foreign Key: historia_clinica_id
//    private HistoriaClinica historiaClinica;
}