package entidades.jogador;
import entidades.ElementoDoJogo;
import java.awt.*;
import java.io.File;
import javax.swing.ImageIcon;

public class Jogador extends ElementoDoJogo {
    private double velocidadeY = 0;
    private boolean noChao = false;
    private int limiteChao;

    /*Controle da colizão */
    private final int margemDeFolga = 15;

    /*Controle da animação */
    private final Image[] sprites;        // O Array que guarda as imagens
    private int frameAtual = 0;     // Qual índice do array desenhar (0, 1, 2...)
    private int contadorFrames = 0; // Controla a velocidade da troca
    private final int velocidadeAnimacao = 5; // Troca de imagem a cada 5 atualizações
    private final int quantidadeDeFrames = 4; // quantidade de imagens na animação

    /*Controle da mudança de tamanho */
    private final int larguraOriginal;
    private final int alturaOriginal;
    private boolean Encolhido = false;

    public Jogador(String caminhoBase) {// 50 70
        super(50, 220, 80, 112); 
        larguraOriginal = 80;
        alturaOriginal = 112;
        limiteChao = 340 - alturaOriginal;

        // Inicializa o array
        sprites = new Image[quantidadeDeFrames];
        
        carregarSprites(caminhoBase);
    }

    private void carregarSprites(String caminho) {
        for (int i = 0; i < quantidadeDeFrames; i++) {
            // Monta o nome do arquivo: "caminho" + "/Frame (0).png"
            String caminhoFinal = caminho + "/Frame (" + (i+1) + ").png";
            
            try {
                File arq = new File(caminhoFinal);
                if (arq.exists()) {
                    sprites[i] = new ImageIcon(caminhoFinal).getImage();
                } else {
                    System.err.println("Frame faltando: " + caminhoFinal);
                }
            } catch (Exception e) {
            }
        }
    }

    public void pular() {
        if (Encolhido) {
            ResetarTamanho();
        }

        if (noChao) {
            velocidadeY = -14;
            noChao = false;
        }
    }

    public void interromperPulo(){
        if(!noChao && velocidadeY < 0){
            velocidadeY = velocidadeY * 0.5;
        }
    }

    public void Encolher(){
        if(noChao){
            largura = larguraOriginal/2;
            altura = alturaOriginal/2;
            y = limiteChao + (alturaOriginal - altura);
            Encolhido = true;
        }
    }

    public void ResetarTamanho(){
        if(Encolhido){
            largura = larguraOriginal;
            altura = alturaOriginal;
            y = limiteChao;
            Encolhido = false;
        }
    }

    @Override
    public void atualizar() {
        /*controle da posição y do jogador */
        y += velocidadeY;
        velocidadeY += 1;

        if(!Encolhido){
            if (y >= limiteChao) { 
                y = limiteChao;
                velocidadeY = 0;
                noChao = true;
            }
        }
        
        else{
            if (y >= limiteChao + (alturaOriginal - altura)){
                y = limiteChao + (alturaOriginal - altura);
                velocidadeY = 0;
                noChao = true;
            }
        }

        /*Controle da animação */
        if (noChao) {
            // Só anima se estiver correndo no chão
            contadorFrames++;
            if (contadorFrames >= velocidadeAnimacao) {
                contadorFrames = 0;
                frameAtual++; 
                
                // Se chegar no fim do array, volta para o 0 (loop)
                if (frameAtual >= quantidadeDeFrames) {
                    frameAtual = 0;
                }
            }
        } else {
            // Frame fixo ao pular
            frameAtual = 2;
        }
    }

    @Override
    public void desenhar(Graphics g) {
        // Verifica se a imagem do índice atual existe no Array
        if (sprites[frameAtual] != null) {
            // Desenha a imagem armazenada no índice 'frameAtual'
            g.drawImage(sprites[frameAtual], x, y, largura, altura, null);
        } else {
            // Fallback caso a imagem falhe
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, largura, altura);
        }
    }

    public boolean colideCom(int inX, int inY, int inL, int inA) {
        int hitX = x + margemDeFolga;
        int hitY = y + margemDeFolga;
        int hitL = largura -(margemDeFolga*2);
        int hitA = altura - (margemDeFolga*2);

        return hitX < inX + inL &&
               hitX + hitL > inX &&
               hitY < inY + inA &&
               hitY + hitA > inY;
    }
}