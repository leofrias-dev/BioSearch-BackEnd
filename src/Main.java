import java.util.ArrayList;
import java.util.Scanner;

void main() {
    ArrayList<Animal> catalogo = new ArrayList<>();
    catalogo.add(new Animal(1L, "Vespa Oleira", "Sceliphron caementarium", 95.5));
    catalogo.add(new Animal(2L, "Sabiá-laranjeira", "Turdus rufiventris", 95.5));
    catalogo.add(new Animal(3L, "Leão", "Panthera leo", 95.5));

    Scanner scanner = new Scanner(System.in);
    menu: while (true) {
        System.out.println("--- Menu BioSearch ---");
        System.out.println("1 - Listar Catálogo Completo");
        System.out.println("2 - Buscar Animal por ID");
        System.out.println("3 - Cadastrar Novo Animal");
        System.out.println("4 - Remover Animal");
        System.out.println("0 - Sair");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                for (Animal animal : catalogo) {
                    animal.exibirFichaTecnica();
                }
                break;
            case 2:
                System.out.print("Digite o ID do animal para busca: ");
                long idBusca = scanner.nextLong();
                for (Animal animal : catalogo) {
                    if (animal.getId() == idBusca) {
                        System.out.println("🔍 Resultado da Busca:");
                        animal.exibirFichaTecnica();
                        break;
                    }
                }
                if (catalogo.stream().noneMatch(animal -> animal.getId() == idBusca)) {
                    System.out.println("❌ Espécie não catalogada no BioSearch.");
                }
                break;
            case 3:
                System.out.print("Nome Comum: ");
                String nomeComum = scanner.nextLine();
                System.out.print("Nome Científico: ");
                String nomeCientifico = scanner.nextLine();
                Animal novoAnimal = new Animal(catalogo.size() + 1L, nomeComum, nomeCientifico, 0.0);
                catalogo.add(novoAnimal);
                System.out.println("✅ Animal cadastrado com sucesso!");
                break;
            case 4:
                System.out.print("Digite o ID do animal que deseja excluir: ");
                long idRemover = scanner.nextLong();
                boolean removido = catalogo.removeIf(animal -> animal.getId() == idRemover);
                if (removido) {
                    System.out.println("🗑️ Animal removido com sucesso!");
                } else {
                    System.out.println("ID não encontrado no sistema.");
                }
                break;
            case 0:
                System.out.println("A encerrar o BioSearch...");
                break menu;
            default:
                System.out.println("Opção inválida");
        }
    }
    scanner.close();
}