package com.cvg.mockito.test.controller;

import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.hamcrest.Matchers.*;


/**
 * PRUEBAS DE INTEGRACION CON WEBCLIENTMVC
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerWebClientTest {
    private static final String URL = "http://localhost:8080/api/cuentas";

    private ObjectMapper mapper;

    @Autowired private WebTestClient client;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Order(1)
    @Test
    void transferir() throws JsonProcessingException {
        TransactionDto dto = new TransactionDto();
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put( "status", "OK" );
        response.put("message", "Transacción exitosa");
        response.put("data", dto);

        client.post().uri(URL + "/transferir")
                .contentType( MediaType.APPLICATION_JSON )
                .bodyValue( dto )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith( data -> {
                    try {
                        JsonNode json = mapper.readTree( data.getResponseBody() );
                        assertEquals( "Transacción exitosa", json.path("message").asText() );
                        assertNotNull( json.path("message") );
                        assertEquals( LocalDate.now().toString(), json.path("date").asText() );
                        assertEquals( 1L, json.path("data").path("cuentaOrigen").asLong() );
                        assertEquals( 2L, json.path("data").path("cuentaDestino").asLong() );
                        assertEquals( "100", json.path("data").path("monto").asText() );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } );
                //.jsonPath("$.message").isNotEmpty()
                //.jsonPath("$.message").value( is("Transacción exitosa") )
                //.jsonPath("$.message").isEqualTo("Transacción exitosa" )
                //.jsonPath("$.message").value( valor -> assertEquals("Transacción exitosa", valor))
                //.jsonPath("$.data.cuentaOrigen").isEqualTo( dto.getCuentaOrigen() )
                //.jsonPath("$.data.cuentaDestino").isEqualTo( dto.getCuentaDestino() )
                //.jsonPath( "$.date" ).isEqualTo( LocalDate.now().toString() )
                //.json( mapper.writeValueAsString( response ) );
    }

    @Test
    @Order(2)
    void testDetalle() {
        client.get().uri( URL + "/1" ).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBody(Cuenta.class)
                .consumeWith( data -> {
                    Cuenta cuenta = data.getResponseBody();
                    assertEquals( 1L, cuenta.getId() );
                    assertEquals( "CRISTHIAN", cuenta.getPersona() );
                    assertEquals( "20900.00", cuenta.getSaldo().toPlainString() );
                });
    }
    @Test
    @Order(3)
    void testDetalle2() {
        client.get().uri( URL + "/2" ).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBody(Cuenta.class)
                .consumeWith( data -> {
                    Cuenta cuenta = data.getResponseBody();
                    assertEquals( 2L, cuenta.getId() );
                    assertEquals( "ANGELA", cuenta.getPersona() );
                    assertEquals( "11100.00", cuenta.getSaldo().toPlainString() );
                });
    }

    @Test
    @Order(4)
    void testListar() {
        client.get().uri( URL ).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("CRISTHIAN")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(20900)
                .jsonPath("$[1].persona").isEqualTo("ANGELA")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(11100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }
    @Test
    @Order(5)
    void testListar2() {
        client.get().uri( URL ).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBodyList( Cuenta.class )
                .consumeWith( data -> {
                    List<Cuenta> cuentas = data.getResponseBody();
                    assertEquals( 2, cuentas.size() );
                    assertEquals( 1L, cuentas.get(0).getId() );
                    assertEquals( 2L, cuentas.get(1).getId() );
                    assertEquals( "CRISTHIAN", cuentas.get(0).getPersona() );
                    assertEquals( "ANGELA", cuentas.get(1).getPersona() );
                    assertEquals( "20900.0", cuentas.get(0).getSaldo().toPlainString() );
                    assertEquals( "11100.0", cuentas.get(1).getSaldo().toPlainString() );
                } )
                .hasSize(2);

    }

    @Test
    @Order(6)
    void testGuardar() {
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        client.post().uri(URL + "/registrar")
                .contentType( MediaType.APPLICATION_JSON )
                .bodyValue( cuenta )
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBody( Cuenta.class )
                .consumeWith( response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull( c.getId() );
                    assertEquals( 3L, c.getId() );
                    assertNotNull( c.getPersona() );
                    assertEquals( "Pepe", c.getPersona() );
                    assertNotNull( c.getSaldo().toPlainString() );
                    assertEquals( "3000", c.getSaldo().toPlainString() );
                } );
    }

    @Test
    @Order(7)
    void testEliminar() {
        client.get().uri(URL).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBodyList(Cuenta.class)
                .hasSize(3);

        client.delete().uri(URL+"/3").exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri(URL).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType( MediaType.APPLICATION_JSON )
                .expectBodyList(Cuenta.class)
                .hasSize(2);

        client.get().uri(URL+"/3").exchange()
                //.expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}