package com.cvg.mockito.test.controller;

import com.cvg.mockito.test.Datos;
import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.models.TransactionDto;
import com.cvg.mockito.test.service.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired MockMvc mvc;
    @MockBean CuentaService cuentaService;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    //@ParameterizedTest(name = "Iteración {index} ejecutando con valor {0}")
    //@CsvSource({"1,21000,CRISTHIAN", "2,11000,ANGELA"})
    @Test
    void testDetalle() throws Exception {
        when( cuentaService.findById( 1L ) ).thenReturn(Datos.CUENTA_001().orElseThrow());

        mvc.perform(MockMvcRequestBuilders.get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() )
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect( jsonPath("$.persona").value("CRISTHIAN") )
                .andExpect( jsonPath("$.saldo").value("21000") );

        verify( cuentaService ).findById(1L);
    }

    @Test
    void testTransferir() throws Exception {
        // GIVEN
        TransactionDto dto = new TransactionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        System.out.println(mapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put( "status", "OK" );
        response.put("message", "Transacción exitosa");
        response.put("data", dto);

        System.out.println(mapper.writeValueAsString(response));
        //
        mvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content( mapper.writeValueAsString( dto ) ))
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath("$.date").value(LocalDate.now().toString()) )
                .andExpect( jsonPath("$.message").value("Transacción exitosa") )
                .andExpect( jsonPath("$.data.cuentaOrigen").value(dto.getCuentaOrigen()) )
                .andExpect( jsonPath("$.data.cuentaDestino").value(dto.getCuentaDestino()) )
                .andExpect( content().json( mapper.writeValueAsString(response) ) );
    }

    @Test
    void testFindAll() throws Exception {
        // GIVEN
        List<Cuenta> cuentas = Arrays.asList(
                Datos.CUENTA_001().orElseThrow(),
                Datos.CUENTA_002().orElseThrow() );
        when( cuentaService.findAll() ).thenReturn( cuentas );

        // WHEN
        mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType( MediaType.APPLICATION_JSON ))
                .andExpect( jsonPath("$[0].persona").value( "CRISTHIAN" ) )
                .andExpect( jsonPath("$[1].persona").value( "ANGELA" ) )
                .andExpect( jsonPath("$[0].saldo").value( "21000" ) )
                .andExpect( jsonPath("$[1].saldo").value( "11000" ) )
                .andExpect( jsonPath("$", hasSize(2)) )
                .andExpect( content().json( mapper.writeValueAsString(cuentas) ) );

        verify( cuentaService ).findAll();

    }

    @Test
    void testSave() throws Exception {
        // GIVEN
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        when( cuentaService.save(any()) ).then(invocationOnMock -> {
            Cuenta c = invocationOnMock.getArgument(0);
            c.setId(3L);
            return c;
        });

        //
        mvc.perform(post("/api/cuentas/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString( cuenta ) ))
                .andExpect( status().isCreated() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath("$.id", is(3)))
                .andExpect( jsonPath("$.persona", is("Pepe")) )
                .andExpect( jsonPath("$.saldo", is(3000)) );

        verify( cuentaService ).save(any());
    }
}