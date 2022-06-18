package com.cvg.mockito.test.service;

import com.cvg.mockito.test.models.Banco;
import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.repository.BancoRepository;
import com.cvg.mockito.test.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencia(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferirSaldo(Long numOrigen, Long numDestino, BigDecimal monto, Long bancoId) {
        Cuenta origen = cuentaRepository.findById(numOrigen).orElseThrow();
        origen.debito( monto );
        cuentaRepository.save( origen );

        Cuenta destino = cuentaRepository.findById(numDestino).orElseThrow();
        destino.credito( monto );
        cuentaRepository.save( destino );

        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        Integer totalTransferencia = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencia);
        bancoRepository.save(banco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return this.cuentaRepository.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return this.cuentaRepository.save(cuenta);
    }
}
