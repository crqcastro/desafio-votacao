#!/bin/bash

PROFILE="local"
DIR=${1:-"./docs"}
REGEX=${2:-".*-$PROFILE\.env$"}

ENV_FILE=$(find "$DIR" -maxdepth 1 -type f -regextype posix-extended -regex ".*/$REGEX" | head -n 1)

if [ -z "$ENV_FILE" ]; then
    echo "Nenhum arquivo .env encontrado com o padrão '$REGEX' no diretório '$DIR'."
    exit 1
fi

export $(grep -v '^#' "$ENV_FILE" | xargs)
echo "Variáveis de ambiente carregadas a partir do arquivo $ENV_FILE"


mvn clean install -f ./pom.xml -DskipTests
cp ./target/votacao-*.jar ./target/votacao.jar
sudo docker compose up -d
java -jar ./target/votacao.jar --spring.profiles.active=$PROFILE
