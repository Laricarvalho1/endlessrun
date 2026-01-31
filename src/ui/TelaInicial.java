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

    private ButtonGroup grupoPersonagens = new ButtonGroup();
    private String personagemSelecionado = null;

    public TelaInicial() {
        setTitle("DINOCOMP - Seleção de Personagem");
        setSize(800, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TÍTULO ---
        JLabel tituloPrincipal = new JLabel("DINOCOMP", SwingConstants.CENTER);
        tituloPrincipal.setFont(new Font("Arial", Font.BOLD, 36));
        tituloPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(tituloPrincipal, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel(new GridLayout(1, 2));
        add(painelConteudo, BorderLayout.CENTER);

        // --- PAINEL ESQUERDA (BOTÕES) ---
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 60; 
        gbc.ipady = 20; 

        JButton btnComecar = new JButton("COMEÇAR");
        btnComecar.setFont(new Font("SansSerif", Font.BOLD, 18));
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
                JOptionPane.showMessageDialog(this, "Selecione um personagem!");
            }
        });
        
        gbc.gridy = 0;
        painelBotoes.add(btnComecar, gbc);

        JButton btnSair = new JButton("SAIR");
        btnSair.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnSair.setBackground(Color.WHITE);
        btnSair.setBorder(new LineBorder(new Color(220, 20, 60), 2));
        btnSair.addActionListener(e -> System.exit(0)); 
        
        gbc.gridy = 1;
        painelBotoes.add(btnSair, gbc);
        painelConteudo.add(painelBotoes);

        // --- PAINEL DIREITA (GRID) ---
        JPanel painelPersonagens = new JPanel(new BorderLayout());
        JLabel lblTituloPers = new JLabel("ESCOLHA SEU AVATAR", SwingConstants.CENTER);
        lblTituloPers.setFont(new Font("Arial", Font.BOLD, 14));
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
            btn.setIcon(criarCard(nomeDino, caminhoFoto, false));
            btn.setSelectedIcon(criarCard(nomeDino, caminhoFoto, true));
            btn.setBackground(Color.WHITE);
            btn.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            
            btn.addActionListener(e -> personagemSelecionado = e.getActionCommand());
            grupoPersonagens.add(btn);
            gridImagens.add(btn);
        }
        painelPersonagens.add(gridImagens, BorderLayout.CENTER);
        painelConteudo.add(painelPersonagens);
    }

    private ImageIcon criarCard(String txt, String path, boolean sel) {
        int w = 90, h = 130;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            File f = new File(path);
            if (f.exists()) g2.drawImage(ImageIO.read(f), 10, 10, w-20, h-45, null);
        } catch (IOException e) {}

        if (sel) {
            g2.setColor(new Color(50, 205, 50, 35));
            g2.fillRect(2, 2, w-4, h-4);
            g2.setColor(new Color(50, 205, 50));
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.drawRect(2, 2, w-4, h-4);

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