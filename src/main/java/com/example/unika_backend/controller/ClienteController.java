package com.example.unika_backend.controller;

import com.example.unika_backend.model.Cliente;
import com.example.unika_backend.model.Endereco;
import com.example.unika_backend.service.ClienteService;
import com.example.unika_backend.service.ImportClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:4200")
@Validated
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ImportClienteService importClienteService;

    @GetMapping
    public Page<Cliente> getAllClientes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id){
        Cliente cliente = clienteService.findById(id);
        if(cliente != null){
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente){
        Cliente novoCliente = clienteService.save(cliente);
        return ResponseEntity.ok(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente){
        Cliente clienteAtual = clienteService.findById(id);
        if(clienteAtual == null){
            return ResponseEntity.notFound().build();
        }

        clienteAtual.setId(id);

        try {
            Cliente updatedCliente = clienteService.save(cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable Long id){
        clienteService.deleteById(id);
    }

    @PostMapping("/{clienteId}/enderecos")
    public ResponseEntity<?> adicionaEndereco(@PathVariable Long clienteId, @RequestBody Endereco endereco){
        Endereco novoEndereco = clienteService.addAddress(clienteId, endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @PutMapping("/{clienteId}/enderecos/{enderecoId}")
    public ResponseEntity<?> atualizaEndereco(@PathVariable Long clienteId, @PathVariable Long enderecoId, @Valid @RequestBody Endereco endereco){
        try {
            Endereco enderecoAtualizado = clienteService.updateAddress(clienteId, enderecoId, endereco);
            return ResponseEntity.ok(enderecoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{clienteId}/enderecos/{enderecoId}")
    public ResponseEntity<?> deletaEndereco(@PathVariable Long clienteId, @PathVariable Long enderecoId){
        try {
            clienteService.deleteAddress(clienteId, enderecoId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir endereço: " + e.getMessage());
        }
    }

    @PostMapping("/importaPF")
    public ResponseEntity<String> importaClientesPF(@RequestParam("file") MultipartFile planilha){
        try {
            importClienteService.importClientesPF(planilha);
            return ResponseEntity.ok("Clientes importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao importar clientes: " + e.getMessage());
        }
    }

    @PostMapping("/importaPJ")
    public ResponseEntity<String> importaClientesPJ(@RequestParam("file") MultipartFile planilha){
        try {
            importClienteService.importClientesPJ(planilha);
            return ResponseEntity.ok("Clientes importados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao importar clientes: " + e.getMessage());
        }
    }
}
