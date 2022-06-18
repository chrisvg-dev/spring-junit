package com.cvg.mockito.test;

import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.repository.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("CRISTHIAN", cuenta.orElseThrow().getPersona());
        assertEquals( "21000.00", cuenta.orElseThrow().getSaldo().toPlainString() );
    }

    @Test
    void testFindByName() {
        String name = "CRISTHIAN";
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona(name);
        assertTrue(cuenta.isPresent());
        assertEquals("CRISTHIAN", cuenta.orElseThrow().getPersona());
        assertEquals( "21000.00", cuenta.orElseThrow().getSaldo().toPlainString() );
    }
    @Test
    void testFindByNameThrowException() {
        String name = "PEPE";
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona(name);
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }
    @Test
    void testFindAll() {
        List<Cuenta> cuentas= cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {
        Cuenta cuenta = new Cuenta(null, "ESMERALDA", new BigDecimal("30000.00"));
        Cuenta saved = cuentaRepository.save(cuenta);

        assertEquals( "ESMERALDA", saved.getPersona() );
        assertEquals( "30000.00", saved.getSaldo().toPlainString() );
        assertEquals( 3, saved.getId() );

    }
    @Test
    void testUpdate() {
        Cuenta cuentaEsmeralda = new Cuenta(null, "ESMERALDA", new BigDecimal("30000.00"));
        Cuenta saved = cuentaRepository.save(cuentaEsmeralda);

        assertEquals( "ESMERALDA", saved.getPersona() );
        assertEquals( "30000.00", saved.getSaldo().toPlainString() );
//        assertEquals( 3L, saved.getId() );

        cuentaEsmeralda.setPersona( "ESMERALDA GARCIA" );
        cuentaEsmeralda.setSaldo( new BigDecimal("3800") );
        Cuenta updated = cuentaRepository.save( cuentaEsmeralda );

        assertEquals( "ESMERALDA GARCIA", cuentaEsmeralda.getPersona() );
        assertEquals( "3800", saved.getSaldo().toPlainString() );
    }
    @Test
    void testDelete() {
        Long id = 2L;
        Cuenta cuenta = cuentaRepository.findById(id).orElseThrow();
        assertEquals("ANGELA", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            //cuentaRepository.findByPersona("ANGELA").orElseThrow();
            cuentaRepository.findById( id ).orElseThrow();
        });
        assertEquals(1, cuentaRepository.findAll().size());
    }
}
