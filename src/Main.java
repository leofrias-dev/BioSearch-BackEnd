import java.util.Scanner;

void main() {
    CatalogoService service = new CatalogoService();
    Scanner scanner = new Scanner(System.in);
    menu: while (true) {
        try {
            System.out.println("--- Menu BioSearch ---");
            System.out.println("1 - Listar Catálogo Completo");
            System.out.println("2 - Buscar Animal por ID");
            System.out.println("3 - Cadastrar Novo Animal");
            System.out.println("4 - Remover Animal");
            System.out.println("5 - Editar Animal");
            System.out.println("6 - Buscar Animal por Nome");
            System.out.println("0 - Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    service.listarAnimais();
                    break;
                case 2:
                    System.out.print("Digite o ID do animal para busca: ");
                    long idBusca = scanner.nextLong();
                    Animal animalEncontrado = service.buscarPorId(idBusca);
                    if (animalEncontrado != null) {
                        System.out.println("🔍 Resultado da Busca:");
                        animalEncontrado.exibirFichaTecnica();
                    } else {
                        System.out.println("❌ Espécie não catalogada no BioSearch.");
                    }
                    break;
                case 3:
                    System.out.print("Nome Comum: ");
                    String nomeComum = scanner.nextLine();
                    System.out.print("Nome Científico: ");
                    String nomeCientifico = scanner.nextLine();
                    service.cadastrarAnimal(nomeComum, nomeCientifico);
                    System.out.println("✅ Animal cadastrado com sucesso!");
                    break;
                case 4:
                    System.out.print("Digite o ID do animal que deseja excluir: ");
                    long idRemover = scanner.nextLong();
                    boolean removido = service.removerAnimal(idRemover);
                    if (removido) {
                        System.out.println("🗑️ Animal removido com sucesso!");
                    } else {
                        System.out.println("ID não encontrado no sistema.");
                    }
                    break;
                case 5:
                    System.out.print("Digite o ID do animal que deseja editar: ");
                    long idEditar = scanner.nextLong();
                    scanner.nextLine();
                    if (service.buscarPorId(idEditar) != null) {
                        System.out.print("Novo Nome Comum: ");
                        String novoNomeComum = scanner.nextLine();
                        System.out.print("Novo Nome Científico: ");
                        String novoNomeCientifico = scanner.nextLine();
                        service.editarAnimal(idEditar, novoNomeComum, novoNomeCientifico);
                        System.out.println("✏️ Animal atualizado com sucesso!");
                    } else {
                        System.out.println("ID não encontrado no sistema.");
                    }
                    break;
                case 6:
                    System.out.print("Digite um termo de busca (nome comum ou científico): ");
                    String termoBusca = scanner.nextLine();
                    service.buscarPorNome(termoBusca);
                    break;
                case 0:
                    System.out.println("A encerrar o BioSearch...");
                    break menu;
                default:
                    System.out.println("Opção inválida");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Entrada inválida! Por favor, digite um formato válido.");
            scanner.nextLine();
        }
    }
    scanner.close();
}