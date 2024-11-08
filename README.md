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
- **jsonplaceholder** - API falsa e confiável gratuita para testes e prototipagem. [JSON PlaceHolder](https://jsonplaceholder.typicode.com/)

## Instalação
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
- Gere um token de conexão para poder colocar o projeto no sonarqube. [Criar token de acesso](https://docs.sonarsource.com/sonarqube/9.9/user-guide/user-account/generating-and-using-tokens/)
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
- ### Linux
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
> Para consultar se o projeto subiu, verifique a rota de health. [Actuator Health](http://localhost:8083/actuator/health)

## Documentação
- Postman: Importa a collection disponível dentro da pasta docs. [Collection](https://raw.githubusercontent.com/crqcastro/desafio-votacao/refs/heads/main/docs/Desafio-votacao.postman_collection.json)
- OpenAPI: Acesse a documentação da API em [OpenAPI](http://localhost:8083/swagger-ui/index.html)

## Versionamento
O Versionamento da API segue o Versionamento Semântico. [SemVer](https://semver.org/)