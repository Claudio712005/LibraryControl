
# LibraryControl - API(JAVA PURO)

LibraryControl é uma API REST para controle de aluguel de livros, criada como um desafio pessoal. A proposta foi ir além do comum: desenvolver tudo sem frameworks, usando Java puro, com o mínimo de bibliotecas possíveis.

A ideia foi explorar o que acontece “por baixo dos panos” em aplicações Java — entender como funciona cada parte, da criação das rotas à autenticação e persistência de dados, sem depender da mágica dos frameworks.

> **Atenção:** Este projeto é **estritamente educacional** e **não está pronto para produção**. Foi desenvolvido para fins de aprendizado e experimentação, não para uso em ambientes reais.
Este projeto **não será publicado em produção (deploy)**.

## Bibliotecas utilizadas
Apesar de ser Java puro, algumas bibliotecas foram essenciais para facilitar tarefas específicas:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```
## Stack utilizada
**Back-end:** Java(JDK 17)

**Banco de dados:** MySQL

**Gerenciamento de dependências:** Maven

## Variáveis de Ambiente

Antes de rodar a aplicação, é preciso configurar algumas variáveis de ambiente (ou colocá-las em um `.properties`)

`DB_USER`

`DB_URL`

`JWT_SECRET`

`JWT_EXPIRATION_TIME`

### Arquitetura e conceitos aplicados

Para fazer tudo funcionar sem frameworks, precisei aplicar vários conceitos fundamentais de Java. Aqui estão alguns dos principais:

#### - Singleton para roteamento manual

Para controlar as rotas da API, criei uma classe `Router` que segue o padrão **Singleton**. Isso garante que exista apenas uma instância centralizada para registrar e aplicar as rotas no servidor:

```java
public class Router {
    private static final Router instance = new Router();
    private final Map<String, Map<String, HttpHandler>> routes = new HashMap<>();

    private Router() {}

    public static Router getInstance() {
        return instance;
    }

    public void addRoute(String path, String method, HttpHandler handler) {
        routes.putIfAbsent(path, new HashMap<>());
        routes.get(path).put(method, handler);
    }

    // ...
}
```

#### - Uso Extensivo de OOP
A estrutura da aplicação foi toda baseada em princípios da Programação Orientada a Objetos, com entidades anotadas por anotações customizadas (@Table, @Id, @Column, etc), DAO genérico e mapeamento objeto-relacional feito “na unha”.

```java
@Table(name = "livros")
public class Livro {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "titulo")
  private String titulo;

  @ManyToOne(nameColumn = "autor_id", foreignKey = "id")
  private Autor autor;

  @Column(name = "ano_publicacao")
  private Integer anoPublicacao;

  @ManyToOne(nameColumn = "genero_id", foreignKey = "id")
  private Genero genero;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "disponivel")
  private Boolean disponivel;
  
  // ...
}
```

#### - DAO Genérico com Java Reflection e Generics
Implementei um `GenericDAO<T>` que consegue salvar, atualizar, buscar e deletar qualquer entidade anotada corretamente, tudo com uso de reflection e generics:
```java
public class GenericDAO<T> {
    private final Class<T> clazz;

    public GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
        // ...
    }

    public void save(T entity) {
        //...
    }

    public T findById(Long id) {
        // ...
    }

    // ...
}
```

#### - HTTP Server com Java Nativo
Para criar o servidor HTTP, utilizei `com.sun.net.httpserver.HttpServer`, que permite manipular requisições e respostas diretamente, sem abstrações de frameworks:

```java
public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    ValidateModels validateModels = new ValidateModels();

    validateModels.validarDataModels();

    RouteScanner.scanPackage("com.clau.controller");

    Router.getInstance().applyRoutes(server);

    server.setExecutor(null);
    server.start();
    System.out.println("Servidor rodando em http://localhost:8080/");

    AluguelAtrasoScheduler scheduler = new AluguelAtrasoScheduler();
    scheduler.iniciar();
  }
}
````
## Como testar este projeto localmente

Você pode testar a API rapidamente usando Docker e Docker Compose, sem precisar configurar o ambiente manualmente.

### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Subindo os containers

1. **Clone o repositório:**

```bash
git clone https://github.com/Claudio712005/LibraryControl.git
cd LibraryControl
```
2. **Navegue até a pasta do projeto:**

    Este projeto já vem com um arquivo `docker-compose.yml` configurado para subir o MySQL e a aplicação Java.


3. **Execute o comando para subir os containers:**
````dockerfile
docker-compose up --build
````
4. **Variáveis de ambientes já configuradas no `docker-compose.yml`:**
```yaml
DB_USER: root
DB_PASSWORD: root_password
DB_URL: jdbc:mysql://db:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
JWT_SECRET: mysecretkey
JWT_EXPIRATION_TIME: 360000
```
5. **Acesse a API em `http://localhost:8080/`.**

## Rotas da API
### Autenticação
A API possui uma rota para autenticação de usuários, que retorna um token JWT para acesso às demais rotas protegidas.

| Método | Rota             | Descrição                         | Permissão |
| ------ | ---------------- | --------------------------------- | --------- |
| `POST` | `/usuarios/auth` | Realiza login e retorna token JWT | Pública   |

### Usuários
| Método | Rota        | Descrição               | Permissão |
| ------ | ----------- | ----------------------- | --------- |
| `GET`  | `/usuarios` | Lista todos os usuários | `ADMIN`   |
| `POST` | `/usuarios` | Cadastra novo usuário   | `ADMIN`   |

### Livros
| Método   | Rota           | Descrição                  | Permissão |
| -------- | -------------- | -------------------------- | --------- |
| `GET`    | `/livros`      | Lista todos os livros      | Pública   |
| `GET`    | `/livros/{id}` | Busca livro por ID         | Pública   |
| `POST`   | `/livros`      | Cadastra novo livro        | `ADMIN`   |
| `PUT`    | `/livros/{id}` | Atualiza dados de um livro | `ADMIN`   |
| `DELETE` | `/livros/{id}` | Exclui um livro            | `ADMIN`   |

### Autores
| Método   | Rota            | Descrição                  | Permissão |
| -------- | --------------- | -------------------------- | --------- |
| `GET`    | `/autores`      | Lista todos os autores     | Pública   |
| `GET`    | `/autores/{id}` | Busca autor por ID         | Pública   |
| `POST`   | `/autores`      | Cadastra novo autor        | `ADMIN`   |
| `PUT`    | `/autores/{id}` | Atualiza dados de um autor | `ADMIN`   |
| `DELETE` | `/autores/{id}` | Remove um autor            | `ADMIN`   |

### Gêneros
| Método   | Rota            | Descrição                   | Permissão |
| -------- | --------------- | --------------------------- | --------- |
| `GET`    | `/generos`      | Lista todos os gêneros      | Pública   |
| `GET`    | `/generos/{id}` | Busca gênero por ID         | Pública   |
| `POST`   | `/generos`      | Cadastra novo gênero        | `ADMIN`   |
| `PUT`    | `/generos/{id}` | Atualiza dados de um gênero | `ADMIN`   |
| `DELETE` | `/generos/{id}` | Remove um gênero            | `ADMIN`   |

### Aluguel de Livros
| Método   | Rota                                                                         | Descrição                               | Permissão          |
| -------- | ---------------------------------------------------------------------------- | --------------------------------------- | ------------------ |
| `POST`   | `/aluguel-livros`                                                            | Realiza o aluguel de um livro           | `CLIENTE`          |
| `GET`    | `/aluguel-livros/usuario/{idUsuario}`                                        | Lista todos os aluguéis de um usuário   | `CLIENTE`, `ADMIN` |
| `PUT`    | `/aluguel-livros/devolver/{idAluguel}`                                       | Devolve um livro                        | `CLIENTE`          |
| `GET`    | `/aluguel-livros/{idAluguel}`                                                | Busca aluguel por ID                    | `CLIENTE`, `ADMIN` |
| `DELETE` | `/aluguel-livros/{idAluguel}`                                                | Exclui um aluguel                       | `ADMIN`            |
| `PUT`    | `/aluguel-livros/estender/{idAluguel}?novaDataDevolucao=YYYY-MM-DDTHH:mm:ss` | Estende o prazo de devolução do aluguel | `ADMIN`            |
| `GET`    | `/aluguel-livros/atrasados`                                                  | Lista aluguéis em atraso                | `ADMIN`            |
