package com.example.unika_backend.service;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.model.Endereco;
import com.example.unika_backend.repository.ClienteRepository;
import com.example.unika_backend.repository.EnderecoRepository;
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
    @Autowired
    private EnderecoRepository enderecoRepository;

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

    public Endereco addAddress(Long clienteId, Endereco endereco) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(
                () -> new IllegalArgumentException("Cliente não encontrado")
        );

        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);
        enderecoRepository.save(endereco);
        clienteRepository.save(cliente);
        return endereco;
    }

    public Endereco updateAddress(Long clienteId, Long enderecoId, Endereco endereco) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(
                () -> new IllegalArgumentException("Cliente não encontrado")
        );

        Endereco enderecoExistente = enderecoRepository.findById(enderecoId).orElseThrow(
                () -> new IllegalArgumentException("Endereço não encontrado")
        );

        if(!enderecoExistente.getCliente().equals(cliente)) {
            throw new IllegalArgumentException("O endereço não pertence ao cliente selecionado");
        }

        enderecoExistente.setLogradouro(endereco.getLogradouro());
        enderecoExistente.setNumero(endereco.getNumero());
        enderecoExistente.setCep(endereco.getCep());
        enderecoExistente.setBairro(endereco.getBairro());
        enderecoExistente.setCidade(endereco.getCidade());
        enderecoExistente.setUf(endereco.getUf());
        enderecoExistente.setTelefone(endereco.getTelefone());
        enderecoExistente.setEnderecoPrincipal(endereco.getEnderecoPrincipal());
        enderecoExistente.setComplemento(endereco.getComplemento());

        return enderecoRepository.save(enderecoExistente);
    }

    public void deleteAddress(Long clienteId, Long addressId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Endereco endereco = enderecoRepository.findById(addressId).orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

        if(!cliente.getEnderecos().contains(endereco)) {
            throw new IllegalArgumentException("O endereço não pertence a esse cliente");
        }

        cliente.getEnderecos().remove(endereco);
        enderecoRepository.delete(endereco);
        clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
