package test.com.damas.objetos;

import com.damas.objetos.Jogador;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JogadorTest {

    @Test
    public void deveCriarJogadorComNomeValido() {
        // preciso criar o que vai ser testado
        String nome = "Willian";

        // preciso agora criar a açao que quero testar
        Jogador jogador = new Jogador(nome);

        // checando se o  resultado deu certo
        assertEquals("Willian", jogador.getNome(), "O nome do jogador deve ser igual a Willian");
        assertEquals(0, jogador.getPontos(), "O pontos deve ser igual a 0");
    }

    @Test
    public void deveAtribuirNomeAnonimoQuandoNomeForMuitoGrande() {
        String nomeGrande = "UmNomeComMais16Caracteres";
        String nomeEsperado = Jogador.DEFAULT_NAME;

        Jogador jogador = new Jogador(nomeGrande);

        assertEquals(nomeEsperado, jogador.getNome(), "Nome com mais de 16 caracteres");
    }

    @Test
    public void deveAtribuirPontosCorretamente() {
        Jogador j = new Jogador("Niko");

        j.addPonto();
        j.addPonto(3);

        assertEquals(4, j.getPontos(), "O pontos deve ser igual a 4");
    }
}
