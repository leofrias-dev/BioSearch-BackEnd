package br.com.biosearch.controller;

import br.com.biosearch.model.RegistroFoto;
import br.com.biosearch.service.AlbumService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/album")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public List<RegistroFoto> listarRegistros() {
        return albumService.listarRegistros();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegistroFoto> cadastrarRegistro(
            @RequestParam String nomeEspecie,
            @RequestParam MultipartFile foto
    ) {
        try {
            RegistroFoto registro = albumService.cadastrarRegistro(nomeEspecie, foto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registro);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<Resource> obterFoto(@PathVariable long id) {
        Path caminhoFoto = albumService.buscarFoto(id);
        if (caminhoFoto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto não encontrada.");
        }

        MediaType tipoConteudo = caminhoFoto.toString().endsWith(".png")
                ? MediaType.IMAGE_PNG
                : MediaType.IMAGE_JPEG;
        Resource foto = new FileSystemResource(caminhoFoto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(tipoConteudo)
                .body(foto);
    }
}
