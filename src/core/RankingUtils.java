package core;
import entidades.jogador.JogadorRanking;
import java.io.*;
import java.util.*;

public class RankingUtils {
    private static final int MAX_JOGADORES = 5;
    private static final String ARQUIVO = "ranking.txt";

    // Cria o arquivo de ranking com valores iniciais
    public static void criarArquivoRanking() {
        //  pw é fechado automaticamente quando sai do bloco try-with-resources
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            pw.println("Dino1;1000");
            pw.println("Dino2;800");
            pw.println("Dino3;600");
            pw.println("Dino4;400");
            pw.println("Dino5;200");
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo de ranking.");
            e.printStackTrace();
        }
    }
    
    // Lê o arquivo de ranking e retorna uma lista de jogadores
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

    // Verifica se a pontuação do candidato é suficiente para entrar no ranking
    public static boolean verificaCandidato(int pontuacao) {

        ArrayList<JogadorRanking> ranking = lerRanking();

        if (ranking.size() < MAX_JOGADORES) return true;

        ranking.sort((a, b) -> b.getPontos() - a.getPontos());

        return pontuacao > ranking.get(ranking.size() - 1).getPontos();
    }

    // Adiciona um novo jogador ao ranking
    public static void adicionarJogador(String nome, int pontos) {

        ArrayList<JogadorRanking> ranking = lerRanking();
        ranking.add(new JogadorRanking(nome, pontos));

        ranking.sort((a, b) -> b.getPontos() - a.getPontos());

        if (ranking.size() > MAX_JOGADORES) {
            ranking = new ArrayList<>(ranking.subList(0, MAX_JOGADORES));
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            // pra cada jogador no ranking, escreve no arquivo
            for (JogadorRanking j : ranking) {
                pw.println(j.getNome() + ";" + j.getPontos());
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de ranking.");
            e.printStackTrace();
        }
    }
}
