package com.damas.objetos;

import java.util.ArrayList;
import java.util.List;

public class Jogo {

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private int vezAtual;
    private int jogadas;
    private int jogadasSemComerPeca;
    private List<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");

        vezAtual = 1;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        colocarPecas(tabuleiro);
    }

    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Peca peca = origem.getPeca();

        if (peca == null) return;
        if (!pertenceAoJogadorAtual(peca)) return;
        if (casaBloqueadaOrigem != null && !origem.equals(casaBloqueadaOrigem)) return;

        if (peca.isMovimentoValido(destino)) {
            if (simularMovimentoEValidar(origem, destino)) {
                peca.mover(destino);
                jogadas++;

                if (!pecasAComer.isEmpty()) {
                    comerPecas();
                    if (deveContinuarJogando(destino)) {
                        casaBloqueadaOrigem = destino;
                    } else {
                        casaBloqueadaOrigem = null;
                        trocarDeVez();
                    }
                } else {
                    jogadasSemComerPeca++;
                    casaBloqueadaOrigem = null;
                    trocarDeVez();
                }

                if (podeTransformarParaDama(destino)) {
                    transformarPedraParaDama(destino);
                }
            }
        }
    }

    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        if (destino.getPeca() != null) return false;

        Peca peca = origem.getPeca();
        int sentidoX = Integer.signum(destino.getX() - origem.getX());
        int sentidoY = Integer.signum(destino.getY() - origem.getY());

        int casasComPecaSeguidas = 0;
        int x = origem.getX() + sentidoX;
        int y = origem.getY() + sentidoY;

        pecasAComer.clear();

        while (x != destino.getX() || y != destino.getY()) {
            Casa alvo = tabuleiro.getCasa(x, y);
            Peca pecaAlvo = alvo.getPeca();

            if (pecaAlvo != null) {
                casasComPecaSeguidas++;

                if (isMesmaCor(peca, pecaAlvo) || casasComPecaSeguidas == 2) {
                    pecasAComer.clear();
                    return false;
                }
                pecasAComer.add(alvo);
            } else {
                casasComPecaSeguidas = 0;
            }

            x += sentidoX;
            y += sentidoY;
        }

        int distanciaX = Math.abs(destino.getX() - origem.getX());
        if (distanciaX == 2 && !(peca instanceof Dama) && pecasAComer.isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean deveContinuarJogando(Casa origem) {
        int[][] direcoes = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
        for (int[] direcao : direcoes) {
            if (percorrerEVerificar(origem, direcao[0], direcao[1])) {
                return true;
            }
        }
        return false;
    }

    private boolean percorrerEVerificar(Casa origem, int deltaX, int deltaY) {
        Peca peca = origem.getPeca();
        int x = origem.getX() + deltaX;
        int y = origem.getY() + deltaY;
        int pecasSeguidas = 0;
        boolean isDama = peca instanceof Dama;

        while (x >= 0 && x < Tabuleiro.MAX_LINHAS && y >= 0 && y < Tabuleiro.MAX_COLUNAS) {
            Peca pecaAlvo = tabuleiro.getCasa(x, y).getPeca();

            if (pecaAlvo != null) {
                if (isMesmaCor(peca, pecaAlvo)) return false;
                pecasSeguidas++;

                if (pecasSeguidas == 2) return false;

                if (!isDama) {
                    int proximoX = x + deltaX;
                    int proximoY = y + deltaY;
                    if (proximoX >= 0 && proximoX < Tabuleiro.MAX_LINHAS && proximoY >= 0 && proximoY < Tabuleiro.MAX_COLUNAS) {
                        return tabuleiro.getCasa(proximoX, proximoY).getPeca() == null;
                    }
                    return false;
                }
            } else {
                if (pecasSeguidas == 1) return true;
                if (!isDama) return false;
            }
            x += deltaX;
            y += deltaY;
        }
        return false;
    }

    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        if (getVez() == 1) jogadorUm.addPonto(pecasComidas);
        if (getVez() == 2) jogadorDois.addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.clear();
        jogadasSemComerPeca = 0;
    }

    private boolean podeTransformarParaDama(Casa casa) {
        if (casa.getPeca().getTipo() == Peca.PEDRA_BRANCA && casa.getY() == 7) return true;
        if (casa.getPeca().getTipo() == Peca.PEDRA_VERMELHA && casa.getY() == 0) return true;
        return false;
    }

    private void transformarPedraParaDama(Casa casa) {
        Peca pedra = casa.getPeca();
        if (pedra.getTipo() == Peca.PEDRA_BRANCA) {
            new Dama(casa, Peca.DAMA_BRANCA);
        } else if (pedra.getTipo() == Peca.PEDRA_VERMELHA) {
            new Dama(casa, Peca.DAMA_VERMELHA);
        }
    }

    public void colocarPecas(Tabuleiro tabuleiro) {
        for (int x = 0; x < Tabuleiro.MAX_LINHAS; x++) {
            for (int y = 0; y < 3; y++) {
                if ((x + y) % 2 == 0) {
                    new Pedra(tabuleiro.getCasa(x, y), Peca.PEDRA_BRANCA);
                }
            }
            for (int y = 5; y < Tabuleiro.MAX_COLUNAS; y++) {
                if ((x + y) % 2 == 0) {
                    new Pedra(tabuleiro.getCasa(x, y), Peca.PEDRA_VERMELHA);
                }
            }
        }
    }

    public void trocarDeVez() {
        vezAtual = (vezAtual == 1) ? 2 : 1;
    }

    private boolean pertenceAoJogadorAtual(Peca peca) {
        boolean pecaBranca = (peca.getTipo() == Peca.PEDRA_BRANCA || peca.getTipo() == Peca.DAMA_BRANCA);
        return (vezAtual == 1 && pecaBranca) || (vezAtual == 2 && !pecaBranca);
    }

    private boolean isMesmaCor(Peca p1, Peca p2) {
        boolean p1Branca = (p1.getTipo() == Peca.PEDRA_BRANCA || p1.getTipo() == Peca.DAMA_BRANCA);
        boolean p2Branca = (p2.getTipo() == Peca.PEDRA_BRANCA || p2.getTipo() == Peca.DAMA_BRANCA);
        return p1Branca == p2Branca;
    }

    public int getGanhador() {
        if (jogadorUm.getPontos() == 12) return 1;
        if (jogadorDois.getPontos() == 12) return 2;
        return 0;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    public void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    public Jogador getJogadorUm() {
        return jogadorUm;
    }

    public Jogador getJogadorDois() {
        return jogadorDois;
    }

    public int getVez() {
        return vezAtual;
    }

    public int getJogadasSemComerPecas() {
        return jogadasSemComerPeca;
    }

    public int getJogada() {
        return jogadas;
    }

    public Casa getCasaBloqueada() {
        return casaBloqueadaOrigem;
    }

    @Override
    public String toString() {
        String retorno = "Vez: " + (getVez() == 1 ? jogadorUm.getNome() : jogadorDois.getNome()) + "\n";
        retorno += "Nº de jogadas: " + getJogada() + "\n";
        retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n\n";

        retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorDois.getPontos()) + "\n\n";

        retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        retorno += "Nº de peças restantes: " + (12 - jogadorUm.getPontos()) + "\n";

        if (casaBloqueadaOrigem != null) {
            retorno += "\nMova a peça na casa " + casaBloqueadaOrigem.getX() + ":" + casaBloqueadaOrigem.getY() + "!";
        }

        return retorno;
    }
}