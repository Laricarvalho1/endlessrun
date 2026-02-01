package ui;
import core.JogoEndlessRunner;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class TelaInicial extends JFrame {

    private ButtonGroup grupoPersonagens = new ButtonGroup(); //garante a escolha de um personagem por vez
    private String personagemSelecionado = null; //guarda o caminho da pasta escolhida da skin

    public TelaInicial() {
        setTitle("DINOCOMP - Seleção de Personagem");
        setSize(800, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TÍTULO
        JLabel tituloPrincipal = new JLabel("DINOCOMP", SwingConstants.CENTER);
        tituloPrincipal.setFont(new Font("Courier New", Font.BOLD, 46));
        tituloPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(tituloPrincipal, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel(new GridLayout(1, 2));
        add(painelConteudo, BorderLayout.CENTER);

        // PAINEL ESQUERDA (BOTÕES)
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(20, 20, 40, 40); //margem
        gbc.gridx = 0; //coluna 0
        gbc.fill = GridBagConstraints.BOTH; //estica em tudo
        gbc.weightx = 1.0; // Ocupa toda a largura disponível proporcionalmente
        gbc.weighty = 1.0; // Ocupa toda a altura disponível proporcionalmente

        /* Botão começar jogo */
        JButton btnComecar = new JButton("COMEÇAR");
        btnComecar.setFont(new Font("Courier New", Font.BOLD, 35));
        btnComecar.setBackground(Color.WHITE);
        btnComecar.setBorder(new LineBorder(new Color(50, 205, 50), 2));
        btnComecar.addActionListener(e -> {
            if (personagemSelecionado != null) {
                String caminhoImagem = personagemSelecionado;

               //primeiro abre o jogo
                iniciarJogo(caminhoImagem);

                //depois fecha a tela inicial
                this.setVisible(false);
                this.dispose();
            } else {
                // exibe uma mensagem de erro se o usuario tentar iniciar o jogo sem selecionar um personagem
                JOptionPane.showMessageDialog(this, "Selecione um personagem!");
            }
        });

        /* Botão Ranking */
        JButton btnRanking = new JButton("RANKING");
        btnRanking.setFont(new Font("Courier New", Font.BOLD, 35));
        btnRanking.setBackground(Color.YELLOW);
        btnRanking.addActionListener(e -> {
            new TelaRanking().setVisible(true);
        });

        /* Botão Sair */
        JButton btnSair = new JButton("SAIR");
        btnSair.setFont(new Font("Courier New", Font.BOLD, 35));
        btnSair.setBackground(Color.WHITE);
        btnSair.setBorder(new LineBorder(new Color(220, 20, 60), 2));
        btnSair.addActionListener(e -> System.exit(0)); 

        // Botão 1 - Começar o jogo
        gbc.gridy = 0;
        painelBotoes.add(btnComecar, gbc);

        // Botão 2 - Ranking
        gbc.gridy = 1;
        painelBotoes.add(btnRanking, gbc);
        
        //Botão 3 - Sair
        gbc.gridy = 2;
        painelBotoes.add(btnSair, gbc);

        painelConteudo.add(painelBotoes);
        
        // PAINEL DIREITA (GRID)
        JPanel painelPersonagens = new JPanel(new BorderLayout());
        JLabel lblTituloPers = new JLabel("Escolha um avatar e escape dos inimigos", SwingConstants.CENTER);
        lblTituloPers.setFont(new Font("Courier New", Font.BOLD, 15));
        lblTituloPers.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelPersonagens.add(lblTituloPers, BorderLayout.NORTH);

        JPanel gridImagens = new JPanel(new GridLayout(2, 3, 10, 10));
        gridImagens.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 20));

        for (int i = 1; i <= 6; i++) {
            String nomeDino = "DINO " + i;
            String pastaPerso = "src/assets/Skins/perso" + i;
            String caminhoFoto = pastaPerso + "/frame (1).png";

            JToggleButton btn = new JToggleButton();
            btn.setActionCommand(pastaPerso); // Envia a pasta para o jogo
            btn.setIcon(criarCard(nomeDino, caminhoFoto, false)); // Cria a caixa de seleção de personagem
            btn.setSelectedIcon(criarCard(nomeDino, caminhoFoto, true)); // Se o personagem for selecionado cria uma versão alternativa da caixa
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            
            btn.addActionListener(e -> personagemSelecionado = e.getActionCommand());
            grupoPersonagens.add(btn);
            gridImagens.add(btn);
        }
        painelPersonagens.add(gridImagens, BorderLayout.CENTER);
        painelConteudo.add(painelPersonagens);
    }

    // Cria o card de seleção do personagem
    private ImageIcon criarCard(String txt, String path, boolean sel) {
        /* Dimensões do card */
        int w = 90, h = 130;

        /* Reserva espaço para carregar a imagem do personagem de forma otimizada */
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tenta carregar a imagem do personagem
        try {
            File f = new File(path);
            if (f.exists()) g2.drawImage(ImageIO.read(f), 10, 10, w-20, h-45, null);
        } catch (IOException e) {}

        // Aplica uma formatação caso o personagem seja selecionado
        if (sel) {
            g2.setColor(new Color(50, 205, 50, 35));
            g2.fillRect(2, 2, w-4, h-4);
            g2.setColor(new Color(50, 205, 50));
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.drawRect(2, 2, w-4, h-4);

        // Escreve o nome do personagem
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(txt, (w - g2.getFontMetrics().stringWidth(txt))/2, h-10);
        g2.dispose();
        return new ImageIcon(img);
    }

    private void iniciarJogo(String pasta) {
        JFrame frame = new JFrame("DINOCOMP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JogoEndlessRunner(pasta));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}