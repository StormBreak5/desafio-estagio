package com.example.unika_backend.repository;

import com.example.unika_backend.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    //Buscar cliente por CPF ou CNPJ
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);

    List<Cliente> findByTipoPessoa(String tipoPessoa);

    @Override
    Page<Cliente> findAll(Pageable pageable);
}
