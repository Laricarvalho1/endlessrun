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
import javax.swing.*;

public class JogoEndlessRunner extends JPanel implements ActionListener, KeyListener {
    
    private Jogador jogador;
    private double velocidadeDoJogo = 1;
    /*tamanho da tela */
    private final int larguraOriginal = 800;
    private final int alturaOriginal = 400;

    /*gerenciamento dos inimigos */
    private ArrayList<Inimigo> inimigos;
    private int cooldownSpawn = 0;
    private final int distanciaMinimaSpawn = 40; /*em pixels */
    private final int distanciaMaximaSpawn = 75; /*em pixels */

    private Timer timer;
    private boolean gameOver = false;
    private Random random;
    private Image imagemFundo;
    private int pontuacao = 0;
    
    // Mudança: Agora armazena o caminho da imagem (String)
    private String caminhoSkinAtual; 

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

        // Game Loop: Roda a cada 20ms (~50 FPS)
        timer = new Timer(20, this);
        timer.start();
    }

    private void carregarRecursos() {
        System.out.println("O Java está procurando arquivos aqui: " + new File(".").getAbsolutePath());

   
        try {
            // Tenta carregar uma imagem (apenas exemplo, não estamos usando ela no draw)
            // Se o arquivo não existir, o catch captura o erro
            File file = new File("c:\\Users\\laric\\Downloads\\endlessrun\\endlessrun\\bg.png");
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
    public void tocarMusica() {
        try {
            // Carrega o arquivo de áudio - ESSE AUDIO PRECISA COLOCAR OS CRÉDITOS - http://opengameart.org/
            File arquivoAudio = new File("musica.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(arquivoAudio);
            
            // Configura o clip
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            // Configura para repetir para sempre (loop)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            // Começa a tocar
            clip.start();
            
        } catch (Exception e) {
            System.err.println("Erro ao tocar música: " + e.getMessage());

    private void gerarInimigoAleatorio(){
        int tipo = random.nextInt(10);
        if(tipo < 6){
            inimigos.add(new CactoPequeno(800,300));
        }
        else{
            inimigos.add(new CactoGrande(800, 270));
        }
    }

            pontuacao++;
    @Override public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        velocidadeDoJogo += 0.001;
        jogador.atualizar();

        // verifica o cooldown de inimigos
        if(cooldownSpawn > 0) {
            cooldownSpawn--;
        }
        // gera um inimigo aleatoriamente
        else{
            gerarInimigoAleatorio();
            cooldownSpawn =(int)(random.nextInt(distanciaMinimaSpawn, distanciaMaximaSpawn)/velocidadeDoJogo);
        }

        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo inimigo = inimigos.get(i);
            inimigo.atualizar(velocidadeDoJogo);

            if (inimigo.getBounds().intersects(jogador.getBounds())) {
                gameOver = true;
                timer.stop();
            }

            if (inimigo.x + inimigo.largura < 0) {
                inimigos.remove(i);
                i--;
            }
        }

        repaint(); 
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        if (this.imagemFundo != null){
            //desenha imagem na posição 0,0 (canto superior esquerdo)
            g.drawImage(this.imagemFundo, 0, 0, null);
        }
        double escalaX = (double) getWidth() / larguraOriginal;
        double escalaY = (double) getHeight() / alturaOriginal;

        g2d.scale(escalaX, escalaY);

        jogador.desenhar(g);

        for (Inimigo i : inimigos) {
            i.desenhar(g);
        }
        g.setColor(Color.BLUE); 
        g.setFont(new Font("Roboto", Font.BOLD, 20)); 
       
       // Desenha no X=650 (direita) e Y=30 (topo)
       g.drawString("Pontuação: " + pontuacao, 20, 30); 
       //
        if (gameOver) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER - Pressione espaço para reiniciar", 150, 200);
        }

        g2d.setTransform(oldTransform);
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

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jogador.interromperPulo();
        }
    }
}