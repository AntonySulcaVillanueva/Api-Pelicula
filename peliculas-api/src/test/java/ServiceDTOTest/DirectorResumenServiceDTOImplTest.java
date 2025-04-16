package ServiceDTOTest;

import com.peliculasapi.ServiceDTO.impl.DirectorResumenServiceDTOImpl;
import com.peliculasapi.dto.DirectorResumenDTO;
import com.peliculasapi.entity.Director;
import com.peliculasapi.repository.DirectorRepository;
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

class DirectorResumenServiceDTOImplTest {

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorResumenServiceDTOImpl directorResumenServiceDTO;

    @BeforeEach
    void setUp() {
        // Inicializar los mocks para que Mockito pueda simular las dependencias
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodosLosDirectores_deberiaDevolverListaDeDirectorResumenDTO() {
        // Arrange - Crear una lista de directores simulados
        Director director1 = new Director();
        director1.setNombre("John");
        director1.setApellido("Smith");
        director1.setImagenUrl("http://example.com/john");

        Director director2 = new Director();
        director2.setNombre("Jane");
        director2.setApellido("Doe");
        director2.setImagenUrl("http://example.com/jane");

        when(directorRepository.findAll()).thenReturn(Arrays.asList(director1, director2));

        // Act - Llamar al método que se está probando
        List<DirectorResumenDTO> directors = directorResumenServiceDTO.getAllDirectors();

        // Assert - Verificar que el resultado es correcto
        assertNotNull(directors); // Asegurarse de que la lista no sea nula
        assertEquals(2, directors.size()); // Verificar el tamaño de la lista
        assertEquals("John", directors.get(0).getNombre()); // Verificar el primer director
        assertEquals("Jane", directors.get(1).getNombre()); // Verificar el segundo director
    }

    @Test
    void obtenerDirectorPorId_deberiaDevolverDirectorResumenDTO() {
        // Arrange - Preparar un director simulado
        Director director = new Director();
        director.setNombre("John");
        director.setApellido("Smith");
        director.setImagenUrl("http://example.com/john");

        when(directorRepository.findById(1L)).thenReturn(Optional.of(director));

        // Act - Llamar al método que se está probando
        DirectorResumenDTO directorDTO = directorResumenServiceDTO.getDirectorById(1L);

        // Assert - Verificar que el resultado es correcto
        assertNotNull(directorDTO); // Asegurarse de que no sea nulo
        assertEquals("John", directorDTO.getNombre()); // Verificar nombre
        assertEquals("Smith", directorDTO.getApellido()); // Verificar apellido
        assertEquals("http://example.com/john", directorDTO.getImagenUrl()); // Verificar URL de la imagen
    }

    @Test
    void obtenerDirectorPorId_deberiaDevolverNullCuandoDirectorNoExiste() {
        // Arrange - Simular que el director no existe
        when(directorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act - Llamar al método que se está probando
        DirectorResumenDTO directorDTO = directorResumenServiceDTO.getDirectorById(1L);

        // Assert - Verificar que el resultado sea nulo
        assertNull(directorDTO);
    }

    @Test
    void obtenerTodosLosDirectores_deberiaDevolverListaVaciaCuandoNoExistenDirectores() {
        // Arrange - Simular una base de datos vacía
        when(directorRepository.findAll()).thenReturn(Arrays.asList());

        // Act - Llamar al método que se está probando
        List<DirectorResumenDTO> directors = directorResumenServiceDTO.getAllDirectors();

        // Assert - Verificar que la lista está vacía
        assertNotNull(directors); // Asegurarse de que la lista no sea nula
        assertTrue(directors.isEmpty()); // Verificar que la lista está vacía
    }
}