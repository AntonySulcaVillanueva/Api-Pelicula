package ServiceDTOTest;

import com.peliculasapi.ServiceDTO.impl.ActorResumenServiceDTOImpl;
import com.peliculasapi.dto.ActorResumenDTO;
import com.peliculasapi.entity.Actor;
import com.peliculasapi.repository.ActorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActorResumenServiceDTOImplTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorResumenServiceDTOImpl actorResumenServiceDTO;

    @BeforeEach
    void setUp() {
        // Inicializar los mocks para que Mockito pueda simular las dependencias
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllActors_shouldReturnListOfActorResumenDTO() {
        // Arrange - Crear una lista de actores simulados
        Actor actor1 = new Actor();
        actor1.setNombre("John");
        actor1.setApellido("Doe");
        actor1.setFotoUrl("http://example.com/john");

        Actor actor2 = new Actor();
        actor2.setNombre("Jane");
        actor2.setApellido("Doe");
        actor2.setFotoUrl("http://example.com/jane");

        when(actorRepository.findAll()).thenReturn(Arrays.asList(actor1, actor2));

        // Act - Llamar al método que se está probando
        List<ActorResumenDTO> actors = actorResumenServiceDTO.getAllActors();

        // Assert - Verificar que el resultado es correcto
        assertNotNull(actors); // Asegurarse de que la lista no sea nula
        assertEquals(2, actors.size()); // Verificar el tamaño de la lista
        assertEquals("John", actors.get(0).getNombre()); // Verificar el primer actor
        assertEquals("Jane", actors.get(1).getNombre()); // Verificar el segundo actor
    }

    @Test
    void getActorById_shouldReturnActorResumenDTO() {
        // Arrange - Preparar un actor simulado
        Actor actor = new Actor();
        actor.setNombre("John");
        actor.setApellido("Doe");
        actor.setFotoUrl("http://example.com/john");

        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));

        // Act - Llamar al método que se está probando
        ActorResumenDTO actorDTO = actorResumenServiceDTO.getActorById(1L);

        // Assert - Verificar que el resultado es correcto
        assertNotNull(actorDTO); // Asegurarse de que no sea nulo
        assertEquals("John", actorDTO.getNombre()); // Verificar nombre
        assertEquals("Doe", actorDTO.getApellido()); // Verificar apellido
        assertEquals("http://example.com/john", actorDTO.getFotoUrl()); // Verificar URL de la foto
    }

    @Test
    void getActorById_shouldReturnNullWhenActorNotFound() {
        // Arrange - Simular que el actor no existe
        when(actorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act - Llamar al método que se está probando
        ActorResumenDTO actorDTO = actorResumenServiceDTO.getActorById(1L);

        // Assert - Verificar que el resultado sea nulo
        assertNull(actorDTO);
    }

    @Test
    void getAllActors_shouldReturnEmptyListWhenNoActorsExist() {
        // Arrange - Simular una base de datos vacía
        when(actorRepository.findAll()).thenReturn(Arrays.asList());

        // Act - Llamar al método que se está probando
        List<ActorResumenDTO> actors = actorResumenServiceDTO.getAllActors();

        // Assert - Verificar que la lista está vacía
        assertNotNull(actors); // Asegurarse de que la lista no sea nula
        assertTrue(actors.isEmpty()); // Verificar que la lista está vacía
    }
}