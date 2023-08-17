package App.MediFour.MediFour.repositorios;

import App.MediFour.MediFour.entidades.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, String> {
// métodos necesarios para guardar/actualizar pacientes en la base de datos,
// realizar consultas o dar de baja según corresponda.
//creo una interface que extiende de JpaRepository manejando la entidad Paciente cuya llave es un String.

    @Query("SELECT p FROM Paciente p WHERE p.dni = :dni")
    public Paciente buscarPorDNI(@Param("dni") Integer dni);

    @Query("SELECT p FROM Paciente p WHERE p.id = :id")
    public Paciente buscarPorId(@Param("id") String id);

    List<Paciente> findByActivoTrue(); // Utiliza el nombre de método con la convención para filtrar pacientes activos

}
