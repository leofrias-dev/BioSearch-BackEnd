package br.com.biosearch.service;

import br.com.biosearch.model.Animal;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class CatalogoService {
    private static final Path ARQUIVO_DADOS = Path.of("animais.csv");
    private final ArrayList<Animal> catalogo = new ArrayList<>();

    public CatalogoService() {
        carregarDados();
    }

    public synchronized List<Animal> listarAnimais() {
        return catalogo.stream()
                .sorted(Comparator.comparing(Animal::getNomeComum, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public synchronized Animal buscarPorId(long id) {
        return catalogo.stream()
                .filter(animal -> animal.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized List<Animal> buscarPorNome(String termo) {
        String termoNormalizado = normalizar(termo);
        return catalogo.stream()
                .filter(animal -> normalizar(animal.getNomeComum()).contains(termoNormalizado)
                        || normalizar(animal.getNomeCientifico()).contains(termoNormalizado))
                .sorted(Comparator.comparing(Animal::getNomeComum, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public synchronized Animal cadastrarAnimal(String nomeComum, String nomeCientifico) {
        long novoId = catalogo.stream()
                .mapToLong(Animal::getId)
                .max()
                .orElse(0L) + 1L;
        Animal novoAnimal = new Animal(novoId, nomeComum.trim(), nomeCientifico.trim(), 0.0);
        catalogo.add(novoAnimal);
        salvarDados();
        return novoAnimal;
    }

    public synchronized Animal editarAnimal(long id, String novoNomeComum, String novoNomeCientifico) {
        Animal animal = buscarPorId(id);
        if (animal == null) {
            return null;
        }

        animal.setNomeComum(novoNomeComum.trim());
        animal.setNomeCientifico(novoNomeCientifico.trim());
        salvarDados();
        return animal;
    }

    public synchronized boolean removerAnimal(long id) {
        boolean removido = catalogo.removeIf(animal -> animal.getId() == id);
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.ROOT);
    }

    private void salvarDados() {
        try (BufferedWriter writer = Files.newBufferedWriter(ARQUIVO_DADOS, StandardCharsets.UTF_8)) {
            for (Animal animal : catalogo) {
                writer.write(animal.getId() + ";" + animal.getNomeComum() + ";" + animal.getNomeCientifico());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível salvar os dados dos animais.", e);
        }
    }

    private void carregarDados() {
        if (Files.notExists(ARQUIVO_DADOS)) {
            adicionarAnimaisPadrao();
            salvarDados();
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_DADOS, StandardCharsets.UTF_8);
            if (linhas.stream().allMatch(String::isBlank)) {
                adicionarAnimaisPadrao();
                salvarDados();
                return;
            }

            for (int i = 0; i < linhas.size(); i++) {
                String[] atributos = linhas.get(i).split(";", -1);
                if (atributos.length != 3) {
                    throw new IllegalStateException("Formato inválido em " + ARQUIVO_DADOS
                            + ", linha " + (i + 1) + ".");
                }

                try {
                    long id = Long.parseLong(atributos[0]);
                    catalogo.add(new Animal(id, atributos[1], atributos[2], 0.0));
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("ID inválido em " + ARQUIVO_DADOS
                            + ", linha " + (i + 1) + ".", e);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível carregar os dados dos animais.", e);
        }
    }

    private void adicionarAnimaisPadrao() {
        catalogo.add(new Animal(1L, "Vespa Oleira", "Trypoxylon politum", 0.0));
        catalogo.add(new Animal(2L, "Sabiá-laranjeira", "Turdus rufiventris", 0.0));
        catalogo.add(new Animal(3L, "Leão", "Panthera leo", 0.0));
    }
}
