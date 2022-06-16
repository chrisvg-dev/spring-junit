package com.cvg.mockito.test.repository;

import com.cvg.mockito.test.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BancoRepository extends JpaRepository<Banco, Long> {

}
