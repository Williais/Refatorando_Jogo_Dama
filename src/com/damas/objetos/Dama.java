package com.damas.objetos;


public class Dama extends Pedra{

    public Dama(Casa casa, int tipo) {
        super(casa, tipo);
    }

   @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));

       return (distanciaX == distanciaY) && (distanciaX > 0);
    }
}
