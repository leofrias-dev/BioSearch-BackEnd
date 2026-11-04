public class Animal {
    private Long id;
    private String nomeComum;
    private String nomeCientifico;
    private Double nivelDeConfianca;

    public Animal(Long id, String nomeComum, String nomeCientifico, Double nivelDeConfianca) {
        this.id = id;
        this.nomeComum = nomeComum;
        this.nomeCientifico = nomeCientifico;
        this.nivelDeConfianca = nivelDeConfianca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeComum() {
        return nomeComum;
    }

    public void setNomeComum(String nomeComum) {
        this.nomeComum = nomeComum;
    }

    public String getNomeCientifico() {
        return nomeCientifico;
    }

    public void setNomeCientifico(String nomeCientifico) {
        this.nomeCientifico = nomeCientifico;
    }

    public Double getNivelDeConfianca() {
        return nivelDeConfianca;
    }

    public void setNivelDeConfianca(Double nivelDeConfianca) {
        this.nivelDeConfianca = nivelDeConfianca;
    }

    public void exibirFichaTecnica() {
        System.out.println("+--------------------------------------+");
        System.out.println("|          FICHA TECNICA BIOLOGICA     |");
        System.out.println("+--------------------------------------+");
        System.out.println("| ID: " + id);
        System.out.println("| Nome comum: " + nomeComum);
        System.out.println("| Nome cientifico: " + nomeCientifico);
        System.out.println("| Nivel de confianca: " + nivelDeConfianca);
        System.out.println("+--------------------------------------+");
    }
}
