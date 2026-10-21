package com.damas.objetos;

public interface Peca {

    public static final int PEDRA_BRANCA = 0;
    public static final int DAMA_BRANCA = 1;
    public static final int PEDRA_VERMELHA = 2;
    public static final int DAMA_VERMELHA = 3;


    public void mover(Casa destino);


    public boolean isMovimentoValido(Casa destino);


    public int getTipo();
}
