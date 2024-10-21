package com.nextstep.nextstepBackEnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextstep.nextstepBackEnd.model.Categoria;
import com.nextstep.nextstepBackEnd.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testGetCategoriasByUsuario() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Transporte");

        when(categoriaService.getCategoriasByUsuarioId(1)).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Transporte"));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testCreateCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Alimentación");

        when(categoriaService.createCategoria(eq(1), any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Alimentación"));
    }

    @Test
    @WithMockUser(roles = "normal")
    public void testUpdateCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Ocio");

        when(categoriaService.updateCategoria(eq(1), any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ocio"));
    }


    @Test
    @WithMockUser(roles = "normal")  // Simula un usuario autenticado
    public void testDeleteCategoria() throws Exception {
        // Simula el comportamiento del servicio
        doNothing().when(categoriaService).deleteCategoria(1);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isOk());

        // Verifica que el metodo deleteCategoria fue llamado con el ID correcto
        verify(categoriaService, times(1)).deleteCategoria(1);
    }



}
