package br.com.cesarcastro.votacao.support;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MessagesConstants {
    public static final String DATA_INICIO_MENOR_QUE_ATUAL = "Data de início da pauta não pode ser menor que a data atual.";
    public static final String DATA_INICIO_MAIOR_QUE_FIM = "Data de fim da pauta não pode ser menor que a data de início.";
    public static final String PAUTA_NAO_ENCONTRADA = "Pauta não encontrada.";
    public static final String PAUTA_JA_ABERTA = "Pauta já aberta.";
    public static final String PAUTA_JA_ENCERRADA = "Pauta já encerrada.";
    public static final String USUARIO_NAO_HABILITADO_PARA_VOTO = "Usuário não habilitado para votar.";
    public static final String USUARIO_JA_VOTOU_NA_PAUTA = "Usuário já votou nesta pauta.";
    public static final String PAUTA_NAO_ABERTA_PARA_VOTACAO = "Pauta não está aberta para votação.";
}
