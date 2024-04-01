package com.example.gesvet;


import com.example.gesvet.dto.UserDto;
import com.example.gesvet.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class GesvetApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    //PRUEBAS AUTOMATIZADAS REGISTRO DE USUARIO
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void testRegister() throws Exception {
//        // Datos de prueba
//        UserDto userDto = new UserDto();
//        userDto.setUsername("yenylorenapascuasescobar@gmail.com");
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
//                .andExpect(status().isOk()) // Verificar que el código de estado sea 200 OK
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
    //PRUEBAS AUTOMATIZADAS GESTIÓN MASCOTAS

//    @MockBean
//    private MascotaService mascotaService;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void testAgregarMascota() throws Exception {
//        // Datos de prueba
//        Mascota mascotaDto = new Mascota();
//        mascotaDto.setNombre("Max");
//        mascotaDto.setColor("Negro");
//        // Simular que el usuario está autenticado
//        when(userService.findByUsername(any(String.class))).thenReturn(new User());
//
//        // Simular el servicio de mascotas para devolver una mascota guardada correctamente
//        when(mascotaService.save(any(Mascota.class))).thenReturn(new Mascota());
//
//        // Realizar la solicitud POST para agregar una mascota
//        mockMvc.perform(post("/api/mascota/mascota/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(mascotaDto)))
//                .andExpect(status().isOk()); // Verificar que el código de estado sea 200 OK
//    }


    // Método para convertir un objeto a JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
