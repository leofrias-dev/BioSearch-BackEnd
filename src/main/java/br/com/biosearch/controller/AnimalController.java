package br.com.biosearch.controller;

import br.com.biosearch.dto.AnimalRequest;
import br.com.biosearch.model.Animal;
import br.com.biosearch.service.CatalogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {
    private final CatalogoService catalogoService;

    public AnimalController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public List<Animal> listarAnimais() {
        return catalogoService.listarAnimais();
    }

    @GetMapping("/busca")
    public List<Animal> buscarPorNome(@RequestParam String termo) {
        return catalogoService.buscarPorNome(termo);
    }

    @GetMapping("/{id}")
    public Animal buscarPorId(@PathVariable long id) {
        return buscarAnimalOuErro(id);
    }

    @PostMapping
    public ResponseEntity<Animal> cadastrarAnimal(@Valid @RequestBody AnimalRequest request) {
        Animal animal = catalogoService.cadastrarAnimal(request.nomeComum(), request.nomeCientifico());
        return ResponseEntity.created(URI.create("/api/animais/" + animal.getId())).body(animal);
    }

    @PutMapping("/{id}")
    public Animal editarAnimal(@PathVariable long id, @Valid @RequestBody AnimalRequest request) {
        Animal animal = catalogoService.editarAnimal(id, request.nomeComum(), request.nomeCientifico());
        if (animal == null) {
            throw animalNaoEncontrado(id);
        }
        return animal;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAnimal(@PathVariable long id) {
        if (!catalogoService.removerAnimal(id)) {
            throw animalNaoEncontrado(id);
        }
        return ResponseEntity.noContent().build();
    }

    private Animal buscarAnimalOuErro(long id) {
        Animal animal = catalogoService.buscarPorId(id);
        if (animal == null) {
            throw animalNaoEncontrado(id);
        }
        return animal;
    }

    private ResponseStatusException animalNaoEncontrado(long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado: " + id);
    }
}
