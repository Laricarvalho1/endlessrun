package ui;
import core.JogoEndlessRunner;
import core.RankingService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TelaPontuacao extends JFrame {
    
    private int pontuacaoFinal;
    private String skinJogador;
    
    public TelaPontuacao(int pontuacao, String skinJogador) {
        this.pontuacaoFinal = pontuacao;
        this.skinJogador = skinJogador;
        
        setTitle("DINOCOMP - Pontuação Final");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        
        // Configura o fundo
        getContentPane().setBackground(new Color(240, 248, 255));
        
        // Painel superior com título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(70, 130, 180));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("PONTUAÇÃO FINAL");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        
        add(painelTitulo, BorderLayout.NORTH);
        
        // Painel central com a pontuação
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBackground(new Color(240, 248, 255));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(25, 0, 40, 0));
        
        JLabel lblPontuacao = new JLabel(String.valueOf(pontuacaoFinal));
        lblPontuacao.setFont(new Font("Arial", Font.BOLD, 72));
        lblPontuacao.setForeground(new Color(34, 139, 34));
        lblPontuacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTextoPontuacao = new JLabel("pontos");
        lblTextoPontuacao.setFont(new Font("Arial", Font.PLAIN, 28));
        lblTextoPontuacao.setForeground(Color.DARK_GRAY);
        lblTextoPontuacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensagem = new JLabel("");
        lblMensagem.setFont(new Font("Arial", Font.ITALIC, 16));
        lblMensagem.setForeground(new Color(139, 0, 0));
        lblMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Determina mensagem baseada na pontuação
        if (pontuacaoFinal < 100) {
            lblMensagem.setText("Você pode fazer melhor!");
        } else if (pontuacaoFinal < 500) {
            lblMensagem.setText("Bom trabalho!");
        } else if (pontuacaoFinal < 1000) {
            lblMensagem.setText("Excelente desempenho!");
        } else {
            lblMensagem.setText("LENDÁRIO! Você é incrível!");
        }
        
        painelCentral.add(lblPontuacao);
        painelCentral.add(lblTextoPontuacao);
        painelCentral.add(lblMensagem);
        
        add(painelCentral, BorderLayout.CENTER);
        
        // Painel inferior com botões
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 20, 0));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        painelBotoes.setBackground(new Color(240, 248, 255));
        
        // Botão Menu
        JButton btnMenu = criarBotaoEstilizado("MENU", new Color(70, 130, 180));
        btnMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Reseta o estilo da janela para o padrão do sistema (linux/windows/etc...)
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {}

                    // Gera uma nova tela inicial
                    new TelaInicial().setVisible(true);
                });
            }
        });
        
        // Botão Reiniciar
        JButton btnReiniciar = criarBotaoEstilizado("REINICIAR", new Color(34, 139, 34));
        btnReiniciar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                iniciarJogo(skinJogador); // Inicia um novo jogo com a mesma skin
            }
        });

        // Botão Ranking
        JButton btnRanking = criarBotaoEstilizado("RANKING", new Color(218, 165, 32));
        btnRanking.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                TelaRanking telaRanking = new TelaRanking();
                telaRanking.setVisible(true);
            }
        });
        
        // Botão Sair
        JButton btnSair = criarBotaoEstilizado("SAIR", new Color(178, 34, 34));
        btnSair.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        painelBotoes.add(btnMenu);
        painelBotoes.add(btnReiniciar);
        painelBotoes.add(btnRanking);
        painelBotoes.add(btnSair);
        
        add(painelBotoes, BorderLayout.SOUTH);

        if (RankingService.verificaCandidato(pontuacaoFinal)) {
            TelaNomeJogador telaNomeJogador = new TelaNomeJogador(this, pontuacaoFinal);
            telaNomeJogador.setVisible(true);
            telaNomeJogador.toFront();
        }
    }
    
    private JButton criarBotaoEstilizado(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setBackground(corFundo);
        botao.setForeground(Color.BLACK);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo.brighter());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo);
            }
        });
        
        return botao;
    }
    
    private void iniciarJogo(String caminhoImagem) {
        JFrame frameJogo = new JFrame("Endless Runner POO");
        frameJogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameJogo.add(new JogoEndlessRunner(caminhoImagem));
        frameJogo.pack();
        frameJogo.setLocationRelativeTo(null);
        frameJogo.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frameJogo.setVisible(true);
    }
}