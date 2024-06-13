package com.nextstep.nextstepBackEnd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Utiliza la configuración de pruebas
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Test para probar que se puede, simplemente, acceder a la ruta /auth/register.
    // Solo acceder, no registrar ningún usuario.
    @Test
    public void testEndpoint() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk());
    }


    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"nombretest\",\"username\":\"test@test.com\",\"password\":\"testPw12\",\"rol\":\"normal\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isOk());
    }

}