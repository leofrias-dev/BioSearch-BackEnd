import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CatalogoService {
    private static final Path ARQUIVO_DADOS = Path.of("animais.csv");
    private final ArrayList<Animal> catalogo;

    public CatalogoService() {
        catalogo = new ArrayList<>();
        carregarDados();
    }

    public void listarAnimais() {
        for (Animal animal : catalogo) {
            animal.exibirFichaTecnica();
        }
    }

    public Animal buscarPorId(long id) {
        for (Animal animal : catalogo) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null;
    }

    public void buscarPorNome(String termo) {
        String termoLowercase = removerAcentos(termo).toLowerCase();
        boolean encontrado = false;

        for (Animal animal : catalogo) {
            if (removerAcentos(animal.getNomeComum()).toLowerCase().contains(termoLowercase)
                    || removerAcentos(animal.getNomeCientifico()).toLowerCase().contains(termoLowercase)) {
                animal.exibirFichaTecnica();
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Nenhum animal encontrado com esse termo.");
        }
    }

    private String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public void cadastrarAnimal(String nomeComum, String nomeCientifico) {
        long novoId = catalogo.stream()
                .mapToLong(animal -> animal.getId())
                .max()
                .orElse(0L) + 1L;
        Animal novoAnimal = new Animal(novoId, nomeComum, nomeCientifico, 0.0);
        catalogo.add(novoAnimal);
        salvarDados();
    }

    public boolean removerAnimal(long id) {
        boolean removido = catalogo.removeIf(animal -> animal.getId() == id);
        salvarDados();
        return removido;
    }

    public boolean editarAnimal(long id, String novoNomeComum, String novoNomeCientifico) {
        Animal animal = buscarPorId(id);
        if (animal == null) {
            return false;
        }

        animal.setNomeComum(novoNomeComum);
        animal.setNomeCientifico(novoNomeCientifico);
        salvarDados();
        return true;
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
