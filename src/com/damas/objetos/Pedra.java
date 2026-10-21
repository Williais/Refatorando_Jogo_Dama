package com.damas.objetos;

public class Pedra implements Peca {

    protected Casa casa;
    protected int tipo;

    public Pedra(Casa casa, int tipo) {
        this.casa = casa;
        this.tipo = tipo;
        casa.colocarPeca(this);
    }
    
    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs(destino.getX() - casa.getX());
        int distanciaY = destino.getY() - casa.getY();

        if (distanciaX == 0 || Math.abs(distanciaY) == 0) return false;

        if (distanciaX == Math.abs(distanciaY)) {
            if (Math.abs(distanciaX) == 2) return true;

            if (Math.abs(distanciaX) == 1) {
                if (tipo == Peca.PEDRA_BRANCA) return distanciaY == 1;
                if (tipo == Peca.PEDRA_VERMELHA) return distanciaY == -1;
            }
        }

        return false;
    }

    @Override
    public int getTipo() {
        return tipo;
    }
}
