package com.nextstep.nextstepBackEnd.service;

import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.model.Usuario;
import com.nextstep.nextstepBackEnd.repository.CategoriaRepository;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService; // Inyección del servicio que estamos probando

    @MockBean
    private CategoriaRepository categoriaRepository; // Mock del repositorio de categorías

    @MockBean
    private UserRepository userRepository; // Mock del repositorio de usuarios

    private Usuario mockUser;
    private Categoria mockCategoria;

    @BeforeEach
    public void setUp() {
        // Inicializar el contexto de Mockito y preparar mocks
        MockitoAnnotations.openMocks(this);

        // Configurar un usuario simulado
        mockUser = new Usuario();
        mockUser.setId(1);
        mockUser.setUsername("testuser");

        // Configurar una categoría simulada
        mockCategoria = new Categoria();
        mockCategoria.setId(1);
        mockCategoria.setNombre("Alimentación");
        mockCategoria.setDescripcion("Gastos en comida");
        mockCategoria.setUsuario(mockUser);

        // Simular la búsqueda de un usuario en el repositorio
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        // Simular el guardado de una categoría en el repositorio
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(mockCategoria);
    }

    @Test
    public void testCreateCategoria() {
        // Crear una nueva instancia de la categoría para el test
        Categoria newCategoria = new Categoria();
        newCategoria.setNombre("Transporte");
        newCategoria.setDescripcion("Gastos relacionados con transporte");

        // Simular la respuesta del repositorio cuando se guarda la categoría
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria categoria = invocation.getArgument(0);
            categoria.setId(1);  // Asignar un ID para simular que fue guardado
            return categoria;
        });

        // Ejecutar el metodo de creación
        Categoria result = categoriaService.createCategoria(1, newCategoria);

        // Verificar que el resultado no sea nulo
        assertNotNull(result, "La categoría creada no debería ser nula");
        // Verificar que el nombre y la descripción coincidan con lo esperado
        assertEquals("Transporte", result.getNombre(), "El nombre de la categoría debería coincidir");
        assertEquals("Gastos relacionados con transporte", result.getDescripcion(), "La descripción de la categoría debería coincidir");

        // Verificar que el metodo save del repositorio se llamó una vez
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }


    @Test
    public void testUpdateCategoria() {
        // Configurar una categoría existente en el repositorio
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(mockCategoria));

        // Crear una nueva categoría con información actualizada
        Categoria updatedCategoria = new Categoria();
        updatedCategoria.setNombre("Ocio");
        updatedCategoria.setDescripcion("Gastos en entretenimiento");

        // Ejecutar el metodo de actualización
        Categoria result = categoriaService.updateCategoria(1, updatedCategoria);

        // Verificar que los valores se actualizaron correctamente
        assertEquals("Ocio", result.getNombre(), "El nombre de la categoría debería ser 'Ocio'");
        assertEquals("Gastos en entretenimiento", result.getDescripcion(), "La descripción debería ser 'Gastos en entretenimiento'");

        // Verificar que el metodo save del repositorio se llamó una vez
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testDeleteCategoria() {
        // Simular la existencia de una categoría en el repositorio
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(mockCategoria));

        // Ejecutar el metodo de eliminación
        boolean result = categoriaService.deleteCategoria(1);

        // Verificar que el resultado es true (la categoría fue eliminada con éxito)
        assertTrue(result, "La eliminación debería haber sido exitosa");

        // Verificar que el metodo deleteById del repositorio se llamó una vez
        verify(categoriaRepository, times(1)).deleteById(1);
    }


    @Test
    public void testGetCategoriasByUsuarioId() {
        // Simular la búsqueda de categorías por usuario
        when(categoriaRepository.findByUsuario(mockUser)).thenReturn(List.of(mockCategoria));

        // Ejecutar el metodo para obtener las categorías de un usuario
        List<Categoria> categorias = categoriaService.getCategoriasByUsuarioId(1);

        // Verificar que la lista contiene la categoría simulada
        assertNotNull(categorias, "La lista de categorías no debería ser nula");
        assertEquals(1, categorias.size(), "La lista debería contener una sola categoría");
        assertEquals("Alimentación", categorias.get(0).getNombre(), "El nombre de la categoría debería ser 'Alimentación'");
    }
}
