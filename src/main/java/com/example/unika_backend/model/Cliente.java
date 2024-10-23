package com.example.unika_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoPessoa;

    private String cpfCnpj;

    //somente pessoa física
    private String nome;
    private String rg;
    private Date dataNascimento;

    //somente pessoa jurídica
    private String razaoSocial;
    private String inscricaoEstadual;
    private Date dataCriacao;

    //comum entre os dois tipos
    private String email;
    private Boolean ativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Endereco> enderecos = new ArrayList<>();
}
