import java.awt.*;
import java.awt.event.*;
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
    private ArrayList<Inimigo> inimigos;
    private Timer timer;
    private boolean gameOver = false;
    private Random random;
    private Image imagemFundo;
    private int pontuacao = 0;

    public JogoEndlessRunner() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.PINK);
        setFocusable(true);
        addKeyListener(this);

        jogador = new Jogador();
        inimigos = new ArrayList<>();
        random = new Random();

        // Tratamento de Exceção (Exemplo funcional: Carregar um recurso)
        carregarRecursos();

        tocarMusica();

        // Game Loop: Roda a cada 20ms (~50 FPS)
        timer = new Timer(20, this);
        timer.start();
    }

    // Exemplo de Tratamento de Exceção
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
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            pontuacao++;
            //return;
        }
        jogador.atualizar();
        
        // Gerar inimigos aleatoriamente
        if (random.nextInt(100) < 2) { // 2% de chance por frame
            inimigos.add(new Cacto(800, 250));
        }

        // Atualizar inimigos e checar colisão
        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo inimigo = inimigos.get(i);
            inimigo.atualizar();

            if (inimigo.getBounds().intersects(jogador.getBounds())) {
                gameOver = true;
                timer.stop();
            }

            if (inimigo.x + inimigo.largura < 0) {
                inimigos.remove(i);
                i--;
            }
        }

        repaint(); // Chama o paintComponent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
       //
        if (gameOver) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER - Pressione espaço para reiniciar", 150, 200);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jogador.pular();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            reiniciarJogo();
        }
    }

    private void reiniciarJogo() {
        jogador = new Jogador();
        inimigos.clear();
        pontuacao = 0;
        gameOver = false;
        timer.start();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Endless Runner POO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JogoEndlessRunner());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}