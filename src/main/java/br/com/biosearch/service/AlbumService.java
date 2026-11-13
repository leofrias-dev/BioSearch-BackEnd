package br.com.biosearch.service;

import br.com.biosearch.model.RegistroFoto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AlbumService {
    private static final Path DIRETORIO_FOTOS = Path.of("fotos");
    private static final Path ARQUIVO_REGISTROS = Path.of("observacoes.csv");
    private static final long TAMANHO_MAXIMO_FOTO = 15 * 1024 * 1024;
    private static final byte[] ASSINATURA_PNG = {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    private final ArrayList<RegistroFoto> registros = new ArrayList<>();

    public AlbumService() {
        carregarRegistros();
    }

    public synchronized List<RegistroFoto> listarRegistros() {
        return List.copyOf(registros);
    }

    public synchronized RegistroFoto cadastrarRegistro(String nomeEspecie, MultipartFile foto) {
        String especie = validarNomeEspecie(nomeEspecie);
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("Envie uma foto.");
        }
        if (foto.getSize() > TAMANHO_MAXIMO_FOTO) {
            throw new IllegalArgumentException("A foto deve ter no máximo 15 MB.");
        }

        byte[] conteudo;
        try {
            conteudo = foto.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível ler a foto enviada.", e);
        }

        String extensao = identificarExtensao(conteudo);
        long novoId = registros.stream()
                .mapToLong(RegistroFoto::getId)
                .max()
                .orElse(0L) + 1L;
        String nomeArquivo = UUID.randomUUID() + extensao;
        Path caminhoFoto = DIRETORIO_FOTOS.resolve(nomeArquivo);

        try {
            Files.createDirectories(DIRETORIO_FOTOS);
            Files.write(caminhoFoto, conteudo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível salvar a foto.", e);
        }

        RegistroFoto registro = new RegistroFoto(novoId, especie, LocalDate.now(), nomeArquivo);
        try {
            String linha = registro.getId() + ";" + registro.getNomeEspecie() + ";"
                    + registro.getDataRegistro() + ";" + registro.getNomeArquivo()
                    + System.lineSeparator();
            Files.writeString(
                    ARQUIVO_REGISTROS,
                    linha,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
            registros.add(registro);
            return registro;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(caminhoFoto);
            } catch (IOException falhaAoRemoverFoto) {
                e.addSuppressed(falhaAoRemoverFoto);
            }
            throw new UncheckedIOException("Não foi possível salvar o registro do álbum.", e);
        }
    }

    public synchronized Path buscarFoto(long id) {
        return registros.stream()
                .filter(registro -> registro.getId() == id)
                .findFirst()
                .map(registro -> DIRETORIO_FOTOS.resolve(registro.getNomeArquivo()))
                .filter(Files::isRegularFile)
                .orElse(null);
    }

    private String validarNomeEspecie(String nomeEspecie) {
        if (nomeEspecie == null || nomeEspecie.isBlank()) {
            throw new IllegalArgumentException("Informe o nome da espécie.");
        }

        String especie = nomeEspecie.trim();
        if (especie.length() > 120 || especie.contains(";")
                || especie.contains("\n") || especie.contains("\r")) {
            throw new IllegalArgumentException("O nome da espécie é inválido ou muito longo.");
        }
        return especie;
    }

    private String identificarExtensao(byte[] conteudo) {
        if (conteudo.length >= ASSINATURA_PNG.length && comecaCom(conteudo, ASSINATURA_PNG)) {
            return ".png";
        }
        if (conteudo.length >= 3 && (conteudo[0] & 0xFF) == 0xFF
                && (conteudo[1] & 0xFF) == 0xD8 && (conteudo[2] & 0xFF) == 0xFF) {
            return ".jpg";
        }
        throw new IllegalArgumentException("Formato não suportado. Envie uma foto PNG ou JPG.");
    }

    private boolean comecaCom(byte[] conteudo, byte[] assinatura) {
        for (int i = 0; i < assinatura.length; i++) {
            if (conteudo[i] != assinatura[i]) {
                return false;
            }
        }
        return true;
    }

    private void carregarRegistros() {
        if (Files.notExists(ARQUIVO_REGISTROS)) {
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_REGISTROS, StandardCharsets.UTF_8);
            for (int i = 0; i < linhas.size(); i++) {
                String[] atributos = linhas.get(i).split(";", -1);
                if (atributos.length != 4) {
                    throw new IllegalStateException("Formato inválido em " + ARQUIVO_REGISTROS
                            + ", linha " + (i + 1) + ".");
                }

                try {
                    long id = Long.parseLong(atributos[0]);
                    LocalDate dataRegistro = LocalDate.parse(atributos[2]);
                    validarNomeArquivo(atributos[3]);
                    registros.add(new RegistroFoto(id, atributos[1], dataRegistro, atributos[3]));
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException("Registro inválido em " + ARQUIVO_REGISTROS
                            + ", linha " + (i + 1) + ".", e);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível carregar os registros do álbum.", e);
        }
    }

    private void validarNomeArquivo(String nomeArquivo) {
        if (!nomeArquivo.matches("[a-f0-9-]{36}\\.(png|jpg)")) {
            throw new IllegalArgumentException("Nome de foto inválido.");
        }
    }
}
