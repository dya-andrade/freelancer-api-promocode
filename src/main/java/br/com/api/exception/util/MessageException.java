package br.com.api.exception.util;

public class MessageException {

    public static final String PROMOCODE_NAO_ENCONTRADO = "Erro ao tentar buscar promocode, promocode não encontrado.";
    public static final String PROMOCODE_JA_APLICADO = "Erro ao tentar buscar promocode, promocode não encontrado.";
    public static final String PROMOCODE_APLICACAO_PADRINHO = "Erro ao tentar aplicar promocode, cliente é padrinho do promocode.";
    public static final String PROMOCODE_EXPIRADO = "Erro ao tentar aplicar promocode, promocode está expirado.";
    public static final String APP_NAO_ENCONTRADO = "Erro ao tentar autenticar App, UID ou Token inválidos.";
    public static final String CLIENTE_NAO_ENCONTRADO = "Erro ao tentar buscar cliente, ID não encontrado.";
    public static final String EVENTO_SALDO_INSUFICIENTE = "Erro ao tentar criar evento, saldo insuficiente.";
    public static final String PRODUTO_NAO_ENCONTRADO = "Erro ao tentar buscar cliente, ID não encontrado.";

}
