package br.com.biosearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AnimalRequest(
        @NotBlank @Size(max = 120) @Pattern(regexp = "^[^;\\r\\n]*$") String nomeComum,
        @NotBlank @Size(max = 200) @Pattern(regexp = "^[^;\\r\\n]*$") String nomeCientifico
) {
}
