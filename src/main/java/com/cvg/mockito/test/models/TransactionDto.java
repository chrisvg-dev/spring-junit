package com.cvg.mockito.test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long cuentaOrigen;
    private Long cuentaDestino;
    private BigDecimal monto;
    private Long bancoId;
}
