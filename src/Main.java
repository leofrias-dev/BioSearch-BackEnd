import java.util.ArrayList;
import java.util.Scanner;

void main() {
    ArrayList<Animal> catalogo = new ArrayList<>();
    catalogo.add(new Animal(1L, "Vespa Oleira", "Sceliphron caementarium", 95.5));
    catalogo.add(new Animal(2L, "Sabiá-laranjeira", "Turdus rufiventris", 95.5));
    catalogo.add(new Animal(3L, "Leão", "Panthera leo", 95.5));

    Scanner scanner = new Scanner(System.in);
    System.out.print("Digite o ID do animal que deseja buscar: ");
    long idBusca = scanner.nextLong();
    for (Animal animal : catalogo) {
        if (animal.getId() == idBusca) {
            System.out.println("🔍 Resultado da Busca:");
            animal.exibirFichaTecnica();
            break;
        }
    }
    scanner.close();
}