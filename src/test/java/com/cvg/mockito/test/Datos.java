package com.cvg.mockito.test;

import com.cvg.mockito.test.models.Banco;
import com.cvg.mockito.test.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static Optional<Cuenta> CUENTA_001(){
        return Optional.of(new Cuenta(1L, "CRISTHIAN", new BigDecimal("21000")));
    }
    public static Optional<Cuenta> CUENTA_002(){
        return Optional.of(new Cuenta(2L, "ANGELA", new BigDecimal("11000")));
    }
    public static Optional<Banco> BANCO_001(){
        return Optional.of(new Banco(1L, "BBVA", 0));
    }
}
