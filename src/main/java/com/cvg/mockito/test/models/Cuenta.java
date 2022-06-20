package com.cvg.mockito.test.models;

import com.cvg.mockito.test.exceptions.DineroInsuficienteException;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String persona;
    private BigDecimal saldo;

    public Cuenta() {
    }

    public Cuenta(Long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto){
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) throw new DineroInsuficienteException("Dinero insuficiente");
        this.saldo = nuevoSaldo;
    }
    public void credito(BigDecimal monto){
        BigDecimal nuevoSaldo = this.saldo.add(monto);
        this.saldo = nuevoSaldo;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Cuenta)) return false;

        Cuenta c = (Cuenta) o;
        if (this.persona == null || this.saldo == null) return false;
        return
                Objects.equals( id, c.id ) &&
                        Objects.equals( persona, c.persona ) &&
                        Objects.equals( saldo, c.saldo );
    }

}
