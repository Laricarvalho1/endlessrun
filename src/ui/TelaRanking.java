package ui;
import core.RankingService;
import entidades.jogador.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaRanking extends JFrame {

    public TelaRanking() {
        setTitle("DINOCOMP - Ranking");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(0, 1, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setBackground(Color.DARK_GRAY);

        ArrayList<JogadorRanking> ranking = RankingService.lerRanking();

        int posicao = 1;
        for (JogadorRanking j : ranking) {
            if (posicao > 5) break;
            PodioPanel podio = new PodioPanel(
                    posicao,
                    j.getNome(),
                    j.getPontos()
            );
            painelPrincipal.add(podio);
            posicao++;
        }

        JScrollPane scroll = new JScrollPane(painelPrincipal);
        scroll.setBorder(null);

        add(scroll);
    }

    public class PodioPanel extends JPanel {

    public PodioPanel(int posicao, String nome, int pontos) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 50,10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(corPorPosicao(posicao));

        JLabel lblPosicao = new JLabel(posicao + "º", SwingConstants.CENTER);
        lblPosicao.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel lblNome = new JLabel(nome, SwingConstants.CENTER);
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblPontos = new JLabel(pontos + " pts", SwingConstants.CENTER);
        lblPontos.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 30,0));
        centro.setOpaque(false);
        centro.add(lblNome);
        centro.add(lblPontos);
        

        add(lblPosicao);
        add(centro);
    }
    
    // define a cor do painel com base na posição
    private Color corPorPosicao(int posicao) {
        return switch (posicao) {
            case 1 -> new Color(212, 175, 55);   // Ouro
            case 2 -> new Color(192, 192, 192); // Prata
            case 3 -> new Color(205, 127, 50);  // Bronze
            default -> new Color(120, 120, 120); // Ferro
        };
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaRanking telaRanking = new TelaRanking();
            telaRanking.setVisible(true);
        });
    }
}
