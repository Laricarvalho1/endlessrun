package entidades.jogador;
public class JogadorRanking {

    private String nome;
    private int pontos;

    public JogadorRanking(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }
}