# API de Votação

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=crqcastro_desafio-votacao&metric=bugs)](https://sonarcloud.io/summary/new_code?id=crqcastro_desafio-votacao) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=crqcastro_desafio-votacao&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=crqcastro_desafio-votacao)

Esta API REST se destina a gerenciar votações de pautas em uma assembleia.
A API permite gerenciar e realizar votações em assembleias de maneira eficiente e automatizada.

## Rotas

- **Cadastrar nova pauta** [POST] /v1/pauta
- **Abrir sessão** [PATCH] /v1/pauta/{id_pauta}/abrir-sessao?minutos=5
- **Encerrar sessão** [PATCH] /v1/pauta/{id_pauta}/encerrar-sessao
- **Votar em sessão** [PATCH] /v1/pauta/votar
- **Listar Votações** [GET] /v1/pauta
- **Consultar pauta** [GET] /v1/pauta/{id}
- **Contabilizar pauta** [GET] /v1/pauta/{id}/contabilizar

## Schedulers

- **Encerrar sessões** Encerra sessões de votação que já passaram do tempo limite
- **Habilita sessões** Habilita sessões de votação que ainda não foram habilitadas e estão dentro do prazo

## Tecnologias utilizadas

- Java 17
- Spring 3
- JPA
- Postegresql
- Mockito
- OpenAPI
- Lombok
- Mapstruct
- java-dotenv
- Jacoco
- Sonarqube
- Maven
- H2
- Easy-Random
- Docker
- K6

## Serviços de terceiros

- **jsonplaceholder** - API falsa e confiável gratuita para testes e
  prototipagem. [JSON PlaceHolder](https://jsonplaceholder.typicode.com/)

## Instalação - Local

### Pré-requisitos

- Java 17
- Maven
- Docker

### Passos

- Clone o repositório
- na raiz do projeto docker

```bash
docker compose up -d
```

> [!IMPORTANT]  
> Este comando irá subir um container com o banco de dados postgresql, banco de dados do sonar e o sonarqube

- Acesse o sonarqube em http://localhost:9000 e configure o usuario.
- Gere um token de conexão para poder colocar o projeto no
  sonarqube. [Criar token de acesso](https://docs.sonarsource.com/sonarqube/9.9/user-guide/user-account/generating-and-using-tokens/)
- Faça o build do projeto

```bash
mvn clean install verify sonar:sonar -Dsonar.host.url=http://localhost:9000  -Dsonar.login=5e2fe7187c818fffcef035c79cd51334f9002dd5
```

> [!IMPORTANT]  
> Substitua o token do script acima pelo token gerado no passo anterior

> [!NOTE]  
> Não é obrigatório para executar o projeto o sonarqube, porém é uma boa prática para garantir a qualidade do código

> [!NOTE]  
> Caso queira usar outro servidor do sonarqube, altere a url no script acima

- Suba as variáveis de ambientes necessárias para executar o projeto

### Linux

```bash
PROFILE="local"
DIR=${1:-"./docs"}
REGEX=${2:-".*-$PROFILE\.env$"}

ENV_FILE=$(find "$DIR" -maxdepth 1 -type f -regextype posix-extended -regex ".*/$REGEX" | head -n 1)

if [ -z "$ENV_FILE" ]; then
    echo "Nenhum arquivo .env encontrado com o padrão '$REGEX' no diretório '$DIR'."
    exit 1
fi

export $(grep -v '^#' "$ENV_FILE" | xargs)
```

### Windows

```bat
@echo off

set "PROFILE=local"
set "DIR=%~1"
if "%DIR%"=="" set "DIR=.\docs"
set "REGEX=.*-%PROFILE%.env$"

for /f "delims=" %%f in ('dir /b "%DIR%" ^| findstr /R /C:"%REGEX%"') do (
    set "ENV_FILE=%DIR%\%%f"
    goto :found
)

echo Nenhum arquivo .env encontrado com o padrão '%REGEX%' no diretório '%DIR%'.
exit /b 1

:found
for /f "usebackq delims=" %%a in (%ENV_FILE%) do (
    set "line=%%a"
    setlocal enabledelayedexpansion
    if "!line:~0,1!" neq "#" (
        for /f "tokens=1,* delims==" %%b in ("!line!") do set "%%b=%%c"
    )
    endlocal
)
```

> [!NOTE]  
> Estes scripts carregam para o ambiente o conteudo do arquivo .env que está no diretório docs

- Execute o projeto

```bash
java -jar ./target/votacao.jar --spring.profiles.active=local
```

> [!TIP]
> Para consultar se o projeto subiu, verifique a rota de
> health. [Actuator Health](http://localhost:8083/actuator/health)

## Documentação

- Postman: Importa a collection disponível dentro da pasta
  docs. [Collection](https://raw.githubusercontent.com/crqcastro/desafio-votacao/refs/heads/main/docs/Desafio-votacao.postman_collection.json)
- Api-Docs: Acessa a documentação no formato
  yml. [OpenAPI Docs](https://raw.githubusercontent.com/crqcastro/desafio-votacao/refs/heads/main/docs/votacao.yml)
- OpenAPI: Acesse a documentação da API no swagger [Swagger](http://localhost:8083/swagger-ui/index.html)

## Versionamento

O Versionamento da API segue o Versionamento Semântico. [SemVer](https://semver.org/)

## Contatos

<hr/>
<a target="_blank" href="https://www.linkedin.com/in/cesarrqcastro/">
  <img align="left" alt="LinkdeIn" width="22px" src="https://cdn.simpleicons.org/linkedin/f04f05" />
</a>
<a target="_blank" href="https://api.whatsapp.com/send?phone=5598992007999">
  <img align="left" alt="Whatsapp" width="22px" src="https://cdn.simpleicons.org/whatsapp/f04f05" />
</a>
<a target="_blank" href="https://dev.to/crqcastro/">
  <img align="left" alt="Devto" width="22px" src="https://cdn.simpleicons.org/devdotto/f04f05" />
</a>
<a target="_blank" href="mailto:cesarrqc@gmail.com">
  <img align="left" alt="Gmail" width="22px" src="https://cdn.simpleicons.org/gmail/f04f05" />
</a>
<a target="_blank" href="https://cesarcastro.com.br">
  <img align="left" alt="WebSite" width="22px" src="https://cdn.simpleicons.org/firefoxbrowser/f04f05" />
</a>
<a target="_blank" href="https://www.youracclaim.com/users/crqcastro/badges">
  <img align="left" alt="WebSite" width="22px" src="https://cdn.simpleicons.org/oracle/f04f05" />
</a>
<a target="_blank" href="https://github.com/crqcastro">
  <img align="left" alt="github" width="22px" src="https://cdn.simpleicons.org/github/f04f05" />
</a>
<br/>
<hr/>

> [!TIP]
> Se chegou até aqui, tenho mais uma ideia! existe um script que pode te ajudar a subir o projeto todo em containers!.
> Basta executar o [Script](https://raw.githubusercontent.com/crqcastro/desafio-votacao/refs/heads/main/run.sh) e ele
> irá subir o projeto em containers docker, caso o docker esteja configurado.
> Por padrão, deixo o docker configurado para ser executado com sudo, entao se for seu caso, execute o script e ele vai
> te pedir a senha do sudo.
> Caso queira alterar, porque você adicionou seu usuario no grupo do docker, e nao precisa do sudo, edit e remove o sudo
> do script na linha 18
