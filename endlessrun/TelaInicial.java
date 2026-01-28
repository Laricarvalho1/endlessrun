import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class TelaInicial extends JFrame {

    private ButtonGroup grupoPersonagens = new ButtonGroup();
    private Map<String, String> mapaImagens = new HashMap<>();
    private Map<String, Color> mapaCoresBackup = new HashMap<>();
    private String personagemSelecionado = null;

    private final Color[] coresDisponiveis = {
        Color.BLUE, Color.BLACK, Color.ORANGE, 
        Color.MAGENTA, Color.CYAN, Color.DARK_GRAY
    };

    public TelaInicial() {
        setTitle("DINOCOMP - Seleção de Personagem");
        setSize(800, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel tituloPrincipal = new JLabel("DINOCOMP", SwingConstants.CENTER);
        tituloPrincipal.setFont(new Font("Arial", Font.BOLD, 36));
        tituloPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(tituloPrincipal, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel(new GridLayout(1, 2));
        add(painelConteudo, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 40; 
        gbc.ipady = 15; 

        JButton btnComecar = new JButton("COMEÇAR");
        btnComecar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnComecar.setBackground(new Color(50, 205, 50)); 
        btnComecar.setForeground(Color.BLACK);
        
        btnComecar.addActionListener(e -> {
            if (personagemSelecionado != null) {
                String caminhoImagem = personagemSelecionado;
                this.dispose(); 
                iniciarJogo(caminhoImagem);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um personagem!", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        gbc.gridy = 0;
        painelBotoes.add(btnComecar, gbc);

        JButton btnSair = new JButton("SAIR");
        btnSair.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSair.setBackground(new Color(220, 20, 60)); 
        btnSair.setForeground(Color.BLACK);
        btnSair.addActionListener(e -> System.exit(0)); 
        
        gbc.gridy = 1;
        painelBotoes.add(btnSair, gbc);

        painelConteudo.add(painelBotoes);

        JPanel painelPersonagens = new JPanel(new BorderLayout());
        JLabel lblTituloPers = new JLabel("ESCOLHA SEU AVATAR", SwingConstants.CENTER);
        lblTituloPers.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloPers.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painelPersonagens.add(lblTituloPers, BorderLayout.NORTH);

        JPanel gridImagens = new JPanel(new GridLayout(2, 3, 10, 10));
        gridImagens.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 20));

        for (int i = 0; i < 6; i++) {
            String nomeDino = "DINO " + (i + 1);
            String Caminho = "Assets/Skins/perso" + (i + 1);
            String nomeArquivo = "Assets/Rostos/rosto" + (i + 1) + ".png";
            Color corBackup = coresDisponiveis[i];

            mapaImagens.put(nomeDino, nomeArquivo);
            mapaCoresBackup.put(nomeDino, corBackup);

            JToggleButton btnPersonagem = new JToggleButton();
            btnPersonagem.setActionCommand(Caminho);
            
            btnPersonagem.setIcon(criarIconeCircular(nomeDino, nomeArquivo, corBackup, false));
            btnPersonagem.setSelectedIcon(criarIconeCircular(nomeDino, nomeArquivo, corBackup, true));
            
            btnPersonagem.setBorder(new LineBorder(Color.GRAY, 1));
            btnPersonagem.setFocusPainted(false);
            
            btnPersonagem.addActionListener(e -> {
                personagemSelecionado = e.getActionCommand();
            });

            grupoPersonagens.add(btnPersonagem);
            gridImagens.add(btnPersonagem);
        }

        painelPersonagens.add(gridImagens, BorderLayout.CENTER);
        painelConteudo.add(painelPersonagens);
    }

    // --- MÁGICA DO RECORTE CIRCULAR AQUI ---
    private ImageIcon criarIconeCircular(String texto, String nomeArquivo, Color corBackup, boolean selecionado) {
        int w = 90;
        int h = 130;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        
        // Ativa Anti-aliasing para o círculo ficar lisinho (sem serrilhado)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Image imagemCabeca = null;
        try {
            File arquivo = new File(nomeArquivo);
            if (arquivo.exists()) imagemCabeca = ImageIO.read(arquivo);
        } catch (IOException e) { }

        int centroX = w / 2;
        int yCabeca = 10;
        int tamanhoCabeca = 40; // Tamanho da bolinha

        // --- 1. DESENHAR A CABEÇA RECORTADA ---
        if (imagemCabeca != null) {
            // Define o formato do círculo
            Ellipse2D circulo = new Ellipse2D.Float(centroX - (tamanhoCabeca/2f), yCabeca, tamanhoCabeca, tamanhoCabeca);
            
            // Salva o estado original do recorte
            Shape clipOriginal = g2.getClip();
            
            // Aplica o recorte circular
            g2.setClip(circulo);
            
            // Desenha a imagem (ela será cortada automaticamente)
            g2.drawImage(imagemCabeca, (int)(centroX - (tamanhoCabeca/2f)), yCabeca, tamanhoCabeca, tamanhoCabeca, null);
            
            // Remove o recorte para desenhar o resto
            g2.setClip(clipOriginal);
            
            // Desenha uma borda branca fina ao redor da foto (estilo Google)
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.WHITE);
            g2.draw(circulo);
            
            // Se estiver selecionado, desenha uma borda colorida extra
            if (selecionado) {
                g2.setColor(new Color(50, 205, 50)); // Verde
                g2.setStroke(new BasicStroke(2));
                g2.draw(circulo);
            }
        } else {
            // Se não tiver imagem, bolinha colorida
            g2.setColor(corBackup); 
            g2.fillOval(centroX - (tamanhoCabeca/2), yCabeca, tamanhoCabeca, tamanhoCabeca);
        }

        // --- 2. DESENHAR O CORPO DE PALITO ---
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        
        int pescocoY = yCabeca + tamanhoCabeca;
        int cinturaY = pescocoY + 25;

        g2.drawLine(centroX, pescocoY, centroX, cinturaY); // Tronco
        g2.drawLine(centroX - 15, pescocoY + 10, centroX + 15, pescocoY + 10); // Braços
        g2.drawLine(centroX, cinturaY, centroX - 10, cinturaY + 20); // Perna E
        g2.drawLine(centroX, cinturaY, centroX + 10, cinturaY + 20); // Perna D

        // Borda do botão
        if (selecionado) {
            g2.setColor(new Color(50, 205, 50));
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(2, 2, w-4, h-4);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(2, 2, w-4, h-4);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(texto, (w - fm.stringWidth(texto)) / 2, h - 8);
        
        g2.dispose();
        return new ImageIcon(img);
    }

    private void iniciarJogo(String caminhoImagem) {
        JFrame frameJogo = new JFrame("Endless Runner POO");
        frameJogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameJogo.add(new JogoEndlessRunner(caminhoImagem));
        frameJogo.pack();
        frameJogo.setLocationRelativeTo(null);
        frameJogo.setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}