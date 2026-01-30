package core;
import entidades.inimigos.*;
import entidades.jogador.Jogador;
import ui.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import ui.TelaInicial;

public class JogoEndlessRunner extends JPanel implements ActionListener, KeyListener {
    
    private Jogador jogador;
    private double velocidadeDoJogo = 1.5;
    private final double velocidadeMaxima = 15;

    /*tamanho da tela */
    private final int larguraOriginal = 800;
    private final int alturaOriginal = 400;

    /*gerenciamento dos inimigos */
    private final ArrayList<Inimigo> inimigos;
    private int cooldownSpawn = 0;
    private final int distanciaMinimaSpawn = 50; /*em pixels */
    private final int distanciaMaximaSpawn = 90; /*em pixels */

    private final Timer timer;
    private boolean gameOver = false;
    private final Random random;
    private Image imagemFundo;
    private int pontuacao = 0;
    private Clip musica; 
    // Mudança: Agora armazena o caminho da imagem (String)
    private final String caminhoSkinAtual; 

    // Construtor recebe o caminho da imagem (ex: "rosto1.png")
    public JogoEndlessRunner(String caminhoImagem) {
        this.caminhoSkinAtual = caminhoImagem;
        
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.PINK);
        setFocusable(true);
        addKeyListener(this);

        // Cria o jogador passando o caminho do arquivo
        jogador = new Jogador(caminhoSkinAtual);
        inimigos = new ArrayList<>();
        random = new Random();

        carregarRecursos();

        tocarMusica();

        // Game Loop: Roda a cada 16ms (~60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }

    private void carregarRecursos() {
        System.out.println("O Java está procurando arquivos aqui: " + new File(".").getAbsolutePath());

   
        try {
            // Tenta carregar uma imagem (apenas exemplo, não estamos usando ela no draw)
            // Se o arquivo não existir, o catch captura o erro
            File file = new File("src/Assets/bg.png");
            if(file.exists()) {
                this.imagemFundo = ImageIO.read(file);
            } else{
                System.out.println("Imagem bg.png não encontrada!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar recursos: " + e.getMessage());
            // O jogo continua rodando mesmo sem a imagem (tratamento gracioso)
        }
    }
    public void pararmusica(){
    musica.stop(); 
    }
    public void tocarMusica() {
        try {
            // Carrega o arquivo de áudio - ESSE AUDIO PRECISA COLOCAR OS CRÉDITOS - http://opengameart.org/
            File arquivoAudio = new File("src/Assets/musica.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
            
            // Configura o clip
             musica = AudioSystem.getClip();
            musica.open(audioStream);
            
            // Configura para repetir para sempre (loop)
            musica.loop(Clip.LOOP_CONTINUOUSLY);
            
            // Começa a tocar
            musica.start();
            
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Erro ao tocar música: " + e.getMessage());
        }
    }

    private void gerarInimigoAleatorio(){
        int tipo = random.nextInt(11);
        if(tipo < 2){//20%
            inimigos.add(new Python(800,300));
        }
        else if(tipo < 3){//10%
            inimigos.add(new Aws(800, 190));
        }
        else if(tipo < 4){//10%
            inimigos.add(new NotF(800, 200));
        }
        else if(tipo < 5){//10%
            inimigos.add(new Calculo(800, 270));
        }
        else if(tipo < 7){//20%
            inimigos.add(new Wifi(800,280));
        }
        else if(tipo < 8){//10%
            inimigos.add(new Teams(800,270));
        }
    }

    private void processarGameOver() {
    gameOver = true;
    timer.stop();
    pararmusica();
    
    // Chama a tela de pontuação após um breve delay
    Timer delayTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            mostrarTelaPontuacao();
        }
    });
    delayTimer.setRepeats(false);
    delayTimer.start();
    }

    @Override public void actionPerformed(ActionEvent e) {
        if(gameOver) return;

        if(velocidadeDoJogo < velocidadeMaxima)
            velocidadeDoJogo += 0.001;

        pontuacao++;
        jogador.atualizar();

        // verifica o cooldown de inimigos
        if(cooldownSpawn > 0) {
            cooldownSpawn--;
        }
        // gera um inimigo aleatoriamente
        else{
            gerarInimigoAleatorio();
           cooldownSpawn =(int)(random.nextInt(distanciaMinimaSpawn, distanciaMaximaSpawn) - velocidadeDoJogo);
        }

        for (int i = inimigos.size() - 1; i >= 0; i--) {
            Inimigo inimigo = inimigos.get(i);
            inimigo.atualizar(velocidadeDoJogo);

            // Usa matemática simples em vez de criar objetos Rectangle (getBounds)
            // Assumindo que Inimigo tem x, y, largura, altura acessíveis
            if (jogador.colideCom(inimigo.x, inimigo.y, inimigo.largura, inimigo.altura)) {
                processarGameOver();
                return;
            }

            // Remove inimigos que saíram da tela
            if (inimigo.x + inimigo.largura < -50) {
                inimigos.remove(i);
            }
        }

        repaint(); 
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        Toolkit.getDefaultToolkit().sync();

        double escalaX = (double) getWidth() / larguraOriginal;
        double escalaY = (double) getHeight() / alturaOriginal;

        g2d.scale(escalaX, escalaY);

        if (this.imagemFundo != null){
            //desenha imagem na posição 0,0 (canto superior esquerdo)
            g.drawImage(this.imagemFundo, 0, 0, null);
        }

        jogador.desenhar(g);

        for (Inimigo i : inimigos) {
            i.desenhar(g);
        }
        g.setColor(Color.BLUE); 
        g.setFont(new Font("Roboto", Font.BOLD, 20)); 
       
       // Desenha no X=650 (direita) e Y=30 (topo)
       g.drawString("Pontuação: " + pontuacao, 20, 30); 
       
        if (gameOver) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER - Pressione espaço para reiniciar", 150, 200);
        }

        g2d.setTransform(oldTransform);
        //g.dispose(); isso aqui tira a capacidade do java de desenhar a janela
    }

    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jogador.pular();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            reiniciarJogo();
        }
    }

    private void reiniciarJogo() {
        // Recria o jogador usando a mesma imagem
        jogador = new Jogador(caminhoSkinAtual);
        inimigos.clear();
        pontuacao = 0;
        gameOver = false;
        timer.start();
        velocidadeDoJogo = 1;
    }

    private void mostrarTelaPontuacao() {
    SwingUtilities.invokeLater(() -> {
        // Cria a tela de pontuação
        TelaPontuacao telaPontuacao = new TelaPontuacao(pontuacao, caminhoSkinAtual);
        telaPontuacao.setVisible(true);
        
        // Fecha a janela atual do jogo
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    });
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jogador.interromperPulo();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { System.err.print(e); }
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}