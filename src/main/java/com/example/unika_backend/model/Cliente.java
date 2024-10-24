package com.example.unika_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "O tipo da pessoa não pode ser nulo.")
    private String tipoPessoa;

    @NotNull(message = "O CPF/CNPJ não pode ser nulo.")
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "CPF/CNPJ inválido!")
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
    @Email(message = "E-mail inválido.")
    private String email;
    private Boolean ativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    @JsonManagedReference
    private List<Endereco> enderecos = new ArrayList<>();
}
