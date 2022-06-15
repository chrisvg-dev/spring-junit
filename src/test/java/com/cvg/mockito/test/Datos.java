package com.cvg.mockito.test;

import com.cvg.mockito.test.models.Banco;
import com.cvg.mockito.test.models.Cuenta;

import java.math.BigDecimal;

public class Datos {
    public static Cuenta CUENTA_001(){
        return new Cuenta(1L, "CRISTHIAN", new BigDecimal("21000"));
    }
    public static Cuenta CUENTA_002(){
        return new Cuenta(2L, "ANGELA", new BigDecimal("11000"));
    }
    public static Banco BANCO_001(){
        return new Banco(1L, "BBVA", 0);
    }
}
