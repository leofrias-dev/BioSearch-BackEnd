# BioSearch Backend

API REST em Java para o BioSearch, um projeto em desenvolvimento de catálogo de animais e álbum de fotos.

## Funcionalidades atuais

- Listar, buscar, cadastrar, editar e remover espécies.
- Pesquisar nomes comuns e científicos, ignorando acentos e maiúsculas.
- Enviar fotos JPG ou PNG para o álbum e informar manualmente a espécie.
- Listar os registros do álbum e acessar as fotos.

## Tecnologias

- Java 17+
- Spring Boot
- Maven Wrapper
- CSV para persistência local durante o desenvolvimento

## Como executar

Com o Java 17 ou superior instalado, abra um terminal na pasta do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

## Endpoints

| Método | Endereço | Descrição |
|---|---|---|
| GET | `/api/animais` | Lista as espécies |
| GET | `/api/animais/{id}` | Busca uma espécie pelo ID |
| GET | `/api/animais/busca?termo=sabia` | Busca por nome |
| POST | `/api/animais` | Cadastra uma espécie |
| PUT | `/api/animais/{id}` | Edita uma espécie |
| DELETE | `/api/animais/{id}` | Remove uma espécie |
| GET | `/api/album` | Lista os registros do álbum |
| POST | `/api/album` | Adiciona uma foto ao álbum |
| GET | `/api/album/{id}/foto` | Acessa uma foto |

### Cadastrar uma espécie

Envie um `POST` para `/api/animais` com este JSON:

```json
{
  "nomeComum": "Capivara",
  "nomeCientifico": "Hydrochoerus hydrochaeris"
}
```

### Adicionar uma foto ao álbum

Envie um `POST` para `/api/album` usando `multipart/form-data`, com os campos `nomeEspecie` e `foto`. São aceitos arquivos JPG e PNG de até 15 MB.

## Armazenamento local

- Espécies: `animais.csv`
- Registros do álbum: `observacoes.csv`
- Fotos: pasta `fotos/`

Os registros e as fotos do álbum ficam no computador onde o backend está rodando. Esses arquivos locais não devem ser enviados ao GitHub.

## Próximos passos

- Criar uma interface web para o álbum.
- Usar um banco de dados.
- Adicionar contas de usuário.
- Integrar a identificação de espécies por IA.

**Observação:** as contas de usuário e a identificação por IA ainda não estão implementadas.
