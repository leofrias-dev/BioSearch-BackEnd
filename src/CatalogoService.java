import java.util.ArrayList;

public class CatalogoService {
    private final ArrayList<Animal> catalogo;

    public CatalogoService() {
        catalogo = new ArrayList<>();
        catalogo.add(new Animal(1L, "Vespa Oleira", "Sceliphron caementarium", 95.5));
        catalogo.add(new Animal(2L, "Sabiá-laranjeira", "Turdus rufiventris", 95.5));
        catalogo.add(new Animal(3L, "Leão", "Panthera leo", 95.5));
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

    public void cadastrarAnimal(String nomeComum, String nomeCientifico) {
        Animal novoAnimal = new Animal(catalogo.size() + 1L, nomeComum, nomeCientifico, 0.0);
        catalogo.add(novoAnimal);
    }

    public boolean removerAnimal(long id) {
        return catalogo.removeIf(animal -> animal.getId() == id);
    }

    public boolean editarAnimal(long id, String novoNomeComum, String novoNomeCientifico) {
        Animal animal = buscarPorId(id);
        if (animal == null) {
            return false;
        }

        animal.setNomeComum(novoNomeComum);
        animal.setNomeCientifico(novoNomeCientifico);
        return true;
    }
}
