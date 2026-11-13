package br.com.biosearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class RegistroFoto {
    private final Long id;
    private final String nomeEspecie;
    private final LocalDate dataRegistro;
    private final String nomeArquivo;

    public RegistroFoto(Long id, String nomeEspecie, LocalDate dataRegistro, String nomeArquivo) {
        this.id = id;
        this.nomeEspecie = nomeEspecie;
        this.dataRegistro = dataRegistro;
        this.nomeArquivo = nomeArquivo;
    }

    public Long getId() {
        return id;
    }

    public String getNomeEspecie() {
        return nomeEspecie;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public String getFotoUrl() {
        return "/api/album/" + id + "/foto";
    }

    @JsonIgnore
    public String getNomeArquivo() {
        return nomeArquivo;
    }
}
