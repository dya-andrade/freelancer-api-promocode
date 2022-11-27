package br.com.api.model;

public enum TipoEventoManual {
    RET("RET"),
    ADD("ADD");

    private String descricao;

    TipoEventoManual(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
