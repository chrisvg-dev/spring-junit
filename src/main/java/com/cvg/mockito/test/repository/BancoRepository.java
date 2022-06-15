package com.cvg.mockito.test.repository;

import com.cvg.mockito.test.models.Banco;

import java.util.List;

public interface BancoRepository {
    List<Banco> findAll();
    Banco findById(Long id);
    void update(Banco banco);
}
