package test.com.damas.objetos;
import com.damas.objetos.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Teste {

    // testando a classe jogador
    @Test
    public void testeJogadorCriacaoNomeValido() {
        Jogador jogador = new Jogador("Willian");
        assertEquals("Willian", jogador.getNome(), "O nome do jogador deveria ser Willian");
        assertEquals(0, jogador.getPontos(), "O jogador deve começar com 0 pontos");
    }

    @Test
    public void testeJogadorCriacaoNomeInvalido() {
        Jogador jogador = new Jogador("UmNomeMuitoLongoParaOJogador");
        assertEquals(Jogador.DEFAULT_NAME, jogador.getNome(), "Nomes muito longos devem virar Anônimo");
    }

    @Test
    public void testeJogadorAdicionarPontos() {
        Jogador jogador = new Jogador("Willian");
        jogador.addPonto();
        jogador.addPonto(3);
        assertEquals(4, jogador.getPontos(), "O jogador deveria ter 4 pontos no total");
    }

    @Test
    public void testeTabuleiroInicializacao() {
        Tabuleiro tabuleiro = new Tabuleiro();

        Casa primeiraCasa = tabuleiro.getCasa(0, 0);
        Casa ultimaCasa = tabuleiro.getCasa(7, 7);

        assertNotNull(primeiraCasa, "A casa 0,0 não deveria ser nula");
        assertEquals(0, primeiraCasa.getX());
        assertEquals(0, primeiraCasa.getY());

        assertNotNull(ultimaCasa, "A casa 7,7 não deveria ser nula");
        assertEquals(7, ultimaCasa.getX());
        assertEquals(7, ultimaCasa.getY());
    }

    @Test
    public void testeCasaColocarERemoverPeca() {
        Casa casa = new Casa(2, 2);
        assertFalse(casa.possuiPeca(), "A casa deve iniciar vazia");

        Pedra pedra = new Pedra(casa, Peca.PEDRA_BRANCA);

        assertTrue(casa.possuiPeca(), "A casa deve possuir uma peça agora");
        assertEquals(pedra, casa.getPeca(), "A peça na casa deve ser a pedra instanciada");

        casa.removerPeca();
        assertFalse(casa.possuiPeca(), "A casa deve ficar vazia após remover a peça");
    }


    // testando a Pedra e Dama juntos

    @Test
    public void testePedraMovimentoValido() {
        Tabuleiro tab = new Tabuleiro();
        Casa origem = tab.getCasa(2, 2);
        Pedra pedra = new Pedra(origem, Peca.PEDRA_BRANCA);

        Casa destinoValido1 = tab.getCasa(3, 3);
        Casa destinoValido2 = tab.getCasa(4, 4);

        assertTrue(pedra.isMovimentoValido(destinoValido1), "movimento diagonal de 1 bloco deve ser valido para a Pedra");
        assertTrue(pedra.isMovimentoValido(destinoValido2), "movimento diagonal de 2 blocos deve ser valido para a Pedra");
    }

    @Test
    public void testePedraMovimentoInvalido() {
        Tabuleiro tab = new Tabuleiro();
        Casa origem = tab.getCasa(2, 2);
        Pedra pedra = new Pedra(origem, Peca.PEDRA_BRANCA);

        Casa destinoInvalidoReto = tab.getCasa(2, 3);
        Casa destinoInvalidoLonge = tab.getCasa(5, 5);

        assertFalse(pedra.isMovimentoValido(destinoInvalidoReto), "movimento em linha reta deve ser invalido");
        assertFalse(pedra.isMovimentoValido(destinoInvalidoLonge), "movimento diagonal de 3 blocos deve ser invalido para Pedra");
    }

    @Test
    public void testeDamaMovimentoValidoQualquerDistancia() {
        Tabuleiro tab = new Tabuleiro();
        Casa origem = tab.getCasa(0, 0);
        Dama dama = new Dama(origem, Peca.DAMA_BRANCA);

        Casa destinoLonge = tab.getCasa(7, 7);

        assertTrue(dama.isMovimentoValido(destinoLonge), "Dama deve poder mover várias casas na diagonal");
    }

    // testandi a classe jogo

    @Test
    public void testeJogoInicializacaoCorreta() {
        Jogo jogo = new Jogo();

        assertEquals(1, jogo.getVez(), "o jogo deve começar na vez do jogador 1 (Branco)");
        assertEquals(0, jogo.getJogada(), "o número de jogadas deve iniciar em 0");
        assertNotNull(jogo.getTabuleiro(), "o jogo deve ter instanciado um tabuleiro");

        Tabuleiro tab = jogo.getTabuleiro();
        assertTrue(tab.getCasa(0, 0).possuiPeca(), "Deve haver uma peça branca na posição inicial (0,0)");
        assertEquals(Peca.PEDRA_BRANCA, tab.getCasa(0, 0).getPeca().getTipo(), "A peça em (0,0) deve ser branca");

        assertTrue(tab.getCasa(1, 7).possuiPeca(), "Deve haver uma peça vermelha na posição inicial superior (1,7)");
        assertEquals(Peca.PEDRA_VERMELHA, tab.getCasa(1, 7).getPeca().getTipo(), "a peça em (1,7) deve ser vermelha");
    }

    @Test
    public void testeJogoTrocaDeVez() {
        Jogo jogo = new Jogo();
        assertEquals(1, jogo.getVez());

        jogo.trocarDeVez();
        assertEquals(2, jogo.getVez(), "a vez deve ir para o jogador 2");

        jogo.trocarDeVez();
        assertEquals(1, jogo.getVez(), "a vez deve voltar para o jogador 1");
    }
}
