package com.example.gesvet;

import com.example.gesvet.dto.UserDto;
import com.example.gesvet.models.Mascota;
import com.example.gesvet.models.User;
import com.example.gesvet.service.EspecieService;
import com.example.gesvet.service.MascotaService;
import com.example.gesvet.service.RazaService;
import com.example.gesvet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class GesvetApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private MascotaService mascotaService;
//
//    @MockBean
//    private RazaService razaService;
//
//    @MockBean
//    private EspecieService especieService;
//
    //PRUEBAS AUTOMATIZADAS REGISTRO DE USUARIO
//    @Test
//    public void testRegister() throws Exception {
//        // Datos de prueba
//        UserDto userDto = new UserDto();
//        userDto.setUsername("yenescobar@gmail.com");
//        userDto.setPassword("Yeny.123");
//        userDto.setConfirmPassword("Yeny.123");
//
//        // Realizar la solicitud POST para registrar un usuario
//        mockMvc.perform(post("/api/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDto)))
//                .andExpect(status().isOk()) // Verificar que el código de estado sea 200 OK
//                .andDo(result -> System.out.println("Código de estado: " + result.getResponse().getStatus()));
//    }
//    @Test
//    public void testconfirmarmal() throws Exception {
//        // Datos de prueba
//        UserDto userDto = new UserDto();
//        userDto.setUsername("yenylorenapascuasescobar@gmail.com");
//        userDto.setPassword("Yeny.123");
//        userDto.setConfirmPassword("Yeny");
//
//        // Realizar la solicitud POST para registrar un usuario
//        mockMvc.perform(post("/api/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDto)))
//                .andExpect(status().isBadRequest()) // Verificar que el código de estado sea 200 OK
//                .andDo(result -> System.out.println("Código de estado: " + result.getResponse().getStatus()));
//    }
//
//    @Test
//    public void testusuarioexistente() throws Exception {
//        // Datos de prueba
//        UserDto userDto = new UserDto();
//        userDto.setUsername("yenylorenapascuasescobar@gmail.com");
//        userDto.setPassword("Yeny.123");
//        userDto.setConfirmPassword("Yeny.123");
//        // Simular el servicio para devolver que el usuario ya existe
//        when(userService.findByUsername("yenylorenapascuasescobar@gmail.com"))
//                .thenReturn(new User()); // Simular que se encuentra un usuario con el mismo nombre de usuario
//        // Realizar la solicitud POST para registrar un usuario
//        mockMvc.perform(post("/api/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDto)))
//                .andExpect(status().isOk()) // Verificar que el código de estado sea 200 OK
//                .andDo(result -> System.out.println("Código de estado: " + result.getResponse().getStatus()));
//    }
//    @Test
//    public void testRegistervacio() throws Exception {
//        // Datos de prueba
//        UserDto userDto = new UserDto();
//        userDto.setUsername("");
//        userDto.setPassword("");
//        userDto.setConfirmPassword("");
//
//        // Realizar la solicitud POST para registrar un usuario
//        mockMvc.perform(post("/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userDto)))
//                .andExpect(status().isOk()) // Verificar que el código de estado sea 200 OK
//                .andDo(result -> System.out.println("Código de estado: " + result.getResponse().getStatus()));
//    }
//    PRUEBAS ATOMATIZADAS LOGIN
//    @Autowired
//    private MockMvc mockMvc;
//
////    private String authToken;
//    
//
//    @Test
//    public void testLogin() throws Exception {
//        // Definir el cuerpo de la solicitud
//        String requestBody = "{\"username\": \"yenescobar@gmail.com\", \"password\": \"Yeny.123\"}";
//        
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody))
//                // Verificar que se recibe un código de estado 200 OK
//                .andExpect(status().isOk())
//                // Verificar que el cuerpo de la respuesta contiene el token
//                .andExpect(jsonPath("$.token").exists())
//                // Verificar que el campo authStatus es LOGIN_SUCCESS
//                .andExpect(jsonPath("$.authStatus").value("LOGIN_SUCCESS"))
//                // Obtener el valor del token y guardarlo en una variable
//                .andExpect(jsonPath("$.token").value(Matchers.notNullValue()));
//        // Guardar el token JWT para su uso en otras pruebas
//        // .andDo(result -> authToken = result.getResponse().getContentAsString());
//
//    }
//    private String resultado;
//
//    public void Login() throws Exception {
//        // Definir el cuerpo de la solicitud
//        String requestBody = "{\"username\": \"yenylorena@gmail.com\", \"password\": \"Yeny123\"}";
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody))
//                // Guardar el token JWT para su uso en otras pruebas
//                .andDo(result -> resultado = result.getResponse().getContentAsString());
//        var objectMapper = new ObjectMapper();
//        var json = objectMapper.readTree(resultado);
//        authToken = json.get("token").asText();
//    }
//
//    @Test
//    public void testsaveMascotas() throws Exception {
//        //Test de guardar mascota
//        Login();
//        Mascota mascota = new Mascota();
//        mascota.setNombre("Fido");
//        mascota.setEdad("");
//        mascota.setColor("Negro");
//        mascota.setDetalles("Juguetón");
//        mascota.setGenero("Macho");
//        mascota.setTiempo("Meses");
//        mascota.setRaza(1)
//        
//        // Realizar la solicitud GET a /api/mascota/detailsmascota con el token JWT
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/mascota/mascota/save")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(mascota)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", is("Mascota guardada correctamente")));
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private MascotaService mascotaService;
//
//    @Mock
//    private RazaService razaService;
//
//    @Mock
//    private EspecieService especieService;
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//    @Mock
//    private UserService userService;
//
//    @Test
//    public void testGetMascotas() throws Exception {
//        // Configurar comportamiento del servicio
//        when(userService.findByUsername("usuario")).thenReturn(new User());
//        when(razaService.getAllRazas()).thenReturn(Collections.emptyList());
//        when(especieService.getAllEspecies()).thenReturn(Collections.emptyList());
//
//        // Realizar la solicitud GET
//        mockMvc.perform(get("/api/mascota/detailsmascota")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer <eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5ZjMxZTUzZC0xMzhiLTQ2NDUtYmI2Yy1mOWUyNGI3N2JmMDkiLCJpc3MiOiJjb2Rpbmdfc3RyZWFtc19hdXRoX3NlcnZlciIsInN1YiI6Inllbnlsb3JlbmFwYXNjdWFzZXNjb2JhckBnbWFpbC5jb20iLCJpYXQiOjE3MTIxMTQyMDAsImV4cCI6MTcxMjExNDgwMH0.oqXXTvJ1haII-kYx9OVQizdnbyjrkJoM_3rAsix2kTM>")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.razas").isEmpty())
//                .andExpect(jsonPath("$.especies").isEmpty())
//                .andExpect(jsonPath("$.mascotas").isEmpty());
//    }
    // Agrega aquí más pruebas para otros métodos del controlador
    // Método para convertir un objeto a JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
