package core;
import entidades.inimigos.*;
import entidades.jogador.Jogador;
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
import ui.*;

public class JogoEndlessRunner extends JPanel implements ActionListener, KeyListener {
    
    private Jogador jogador;

    /* Controle de velocidade do jogo - influencia direta na dificuldade */
    private double velocidadeDoJogo = 1.5;
    private final double velocidadeMaxima = 15;

    /* Tamanho da tela */
    private final int larguraOriginal = 800;
    private final int alturaOriginal = 400;

    /* Gerenciamento dos inimigos */
    private final ArrayList<Inimigo> inimigos;
    private int cooldownSpawn = 0;
    private final int distanciaMinimaSpawn = 35; /*em pixels */
    private final int distanciaMaximaSpawn = 60; /*em pixels */

    private final Timer timer;
    private boolean gameOver = false;
    private final Random random;
    private Image imagemFundo;
    private int pontuacao = 0;

    /* Efeitos sonoros */
    private Clip musica; 

    /* Caminho da skin atual */
    private final String caminhoSkinAtual; 

    // Construtor recebe o caminho das imagens do personagem selecionado
    public JogoEndlessRunner(String caminhoImagem) {
        this.caminhoSkinAtual = caminhoImagem;
        
        /* Configuração da janela */
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.PINK);
        setFocusable(true);
        addKeyListener(this);

        // Cria o jogador passando o caminho do arquivo
        jogador = new Jogador(caminhoSkinAtual);

        // Inicia o array de inimigos
        inimigos = new ArrayList<>();

        random = new Random();

        // Carrega o plano de fundo e a musica
        carregarRecursos();
        tocarMusica();

        // Game Loop: Roda a cada 16ms (~60 FPS)
        timer = new Timer(16, this);
        timer.start();
    }

    private void carregarRecursos() {
        System.out.println("O Java está procurando arquivos aqui: " + new File(".").getAbsolutePath());
        try {
            // Tenta carregar a imagem do plano de fundo
            File file = new File("src/assets/fundo.png");
            if(file.exists()) {
                this.imagemFundo = ImageIO.read(file);
            } else{
                System.err.println("Imagem fundo.png não encontrada!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar recursos: " + e.getMessage());
            // O jogo continua rodando mesmo sem a imagem
        }
    }

    public void tocarMusica() {
        try {
            // Carrega o arquivo de áudio 
            // CRÉDITOS - http://opengameart.org/
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

    public void pararmusica(){
    musica.stop(); 
    }

    private void gerarInimigoAleatorio(){
        // Sorteia um numero aleatorio para gerar um inimigo;
        int tipo = random.nextInt(11);
        if(tipo < 2){//20%
            inimigos.add(new Python(800,300));
        }
        else if(tipo < 3){//10%
            inimigos.add(new Aws(800, 210));
        }
        else if(tipo < 4){//10%
            inimigos.add(new ChatGPT(800, 220));
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
        // 20% chance de não gerar um inimigo
        // Torna o jogo mais aleatorio
    }

    private void processarGameOver() {
    gameOver = true;
    /* Para a musica e o timer */
    timer.stop();
    pararmusica();
    
    // Chama a tela de pontuação após um breve delay
    Timer delayTimer = new Timer(800, new ActionListener() {
        @Override public void actionPerformed(ActionEvent evt) {
            mostrarTelaPontuacao();
        }
    });
    delayTimer.setRepeats(false);
    delayTimer.start();
    }

    // Atualiza os dados do jogo a cada frame
    @Override public void actionPerformed(ActionEvent e) {
        if(gameOver) return;

        // Aumenta a velocidade do jogo
        if(velocidadeDoJogo < velocidadeMaxima){
            velocidadeDoJogo += 0.001;
        }

        pontuacao++;
        jogador.atualizar();

        // Verifica o cooldown de inimigos
        if(cooldownSpawn > 0) {
            cooldownSpawn--;
        }

        // Gera um inimigo aleatoriamente e define um novo cooldown de spawn
        else{
            gerarInimigoAleatorio();
            cooldownSpawn =(int)(random.nextInt(distanciaMinimaSpawn, distanciaMaximaSpawn) - velocidadeDoJogo);
        }

        /* Interações com inimigos */
        for (int i = inimigos.size() - 1; i >= 0; i--) {
            Inimigo inimigo = inimigos.get(i);
            inimigo.atualizar(velocidadeDoJogo);

            // Testa a colisão para gerar um GameOver
            if (jogador.colideCom(inimigo.x, inimigo.y, inimigo.largura, inimigo.altura)) {
                processarGameOver();
                return;
            }

            // Remove inimigos que saíram da tela
            if (inimigo.x + inimigo.largura < -50) {
                inimigos.remove(i);
            }
        }

        // Chama o metodo paint para atualizar a tela
        repaint(); 
    }

    // Gera cada frame do jogo
    @Override protected void paintComponent(Graphics g) {
        // Limpa a tela
        super.paintComponent(g);
        
        /* Ajuste para a resolução de tela cheia (resolução original é 800 x 400)*/
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        double escalaX = (double) getWidth() / larguraOriginal;
        double escalaY = (double) getHeight() / alturaOriginal;

        g2d.scale(escalaX, escalaY);

        // Evita perca de frames no linux
        Toolkit.getDefaultToolkit().sync();

        if (this.imagemFundo != null){
            // Desenha imagem na posição 0,0 (canto superior esquerdo)
            g.drawImage(this.imagemFundo, 0, 0, larguraOriginal, alturaOriginal, null);
        }

        /* Desenha o jogador e os inimigos */
        jogador.desenhar(g);

        for (Inimigo i : inimigos) {
            i.desenhar(g);
        }

        /* Desenha a pontuação */
        g.setColor(Color.BLUE); 
        g.setFont(new Font("Courier New", Font.BOLD, 20)); 
       
       // Desenha no X=20 (esquerda) e Y=30 (topo)
       g.drawString("PONTUAÇÃO: " + pontuacao, 20, 30); 

       g2d.setTransform(oldTransform);
    }

    @Override public void keyPressed(KeyEvent e) {
        // precionar W ou SPACE faz o jogador pular
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W) {
            jogador.pular();
        }

        // precionar S ou CTRL faz o jogador diminuir
        if (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_S){
            jogador.Encolher();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override public void keyReleased(KeyEvent e){
        // parar de precionar W ou SPACE faz o jogador interromper o pulo
        if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W){
            jogador.interromperPulo();
        }

        // parar de precionar S ou CTRL faz o jogador voltar ao tamanho normal
        if(e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_S){
            jogador.ResetarTamanho();
        }
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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { System.err.print(e); }
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}