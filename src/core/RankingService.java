package core;
import entidades.jogador.JogadorRanking;
import java.io.*;
import java.util.*;

public class RankingService {
    private static final int MAX_JOGADORES = 5;
    private static final String ARQUIVO = "ranking.txt";

    public static void criarArquivoRanking() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            // Cria um arquivo vazio
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<JogadorRanking> lerRanking() {

        ArrayList<JogadorRanking> ranking = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                String nome = dados[0];
                int pontos = Integer.parseInt(dados[1]);

                ranking.add(new JogadorRanking(nome, pontos));
            }

        } catch (IOException e) {
            System.err.println("Arquivo de ranking não encontrado. Será criado.");
            criarArquivoRanking();
        }

        return ranking;
    }
    public static boolean verificaCandidato(int pontuacao) {

        ArrayList<JogadorRanking> ranking = lerRanking();

        if (ranking.size() < MAX_JOGADORES) return true;

        ranking.sort((a, b) -> b.getPontos() - a.getPontos());

        return pontuacao > ranking.get(ranking.size() - 1).getPontos();
    }
    public static void adicionarJogador(String nome, int pontos) {

        ArrayList<JogadorRanking> ranking = lerRanking();
        ranking.add(new JogadorRanking(nome, pontos));

        ranking.sort((a, b) -> b.getPontos() - a.getPontos());

        if (ranking.size() > MAX_JOGADORES) {
            ranking = new ArrayList<>(ranking.subList(0, MAX_JOGADORES));
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (JogadorRanking j : ranking) {
                pw.println(j.getNome() + ";" + j.getPontos());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
