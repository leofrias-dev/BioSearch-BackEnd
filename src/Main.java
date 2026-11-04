import java.util.ArrayList;

void main() {
    ArrayList<Animal> catalogo = new ArrayList<>();
    catalogo.add(new Animal(1L, "Vespa Oleira", "Sceliphron caementarium", 95.5));
    catalogo.add(new Animal(2L, "Sabiá-laranjeira", "Turdus rufiventris", 95.5));
    catalogo.add(new Animal(3L, "Leão", "Panthera leo", 95.5));

    for (Animal animal : catalogo) {
        animal.exibirFichaTecnica();
    }
}