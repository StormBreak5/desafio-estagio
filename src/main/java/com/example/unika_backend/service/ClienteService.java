package com.example.unika_backend.service;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public Cliente save(Cliente cliente) throws IllegalArgumentException {

        Optional<Cliente> clienteExiste = clienteRepository.findByCpfCnpj(cliente.getCpfCnpj());
        if(clienteExiste.isPresent() && (cliente.getId() == null || !clienteExiste.get().getId().equals(cliente.getId()))) {
            throw new IllegalArgumentException("Já existe cliente nesse CPF/CNPJ");
        }

        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
