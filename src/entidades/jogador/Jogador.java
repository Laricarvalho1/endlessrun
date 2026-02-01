package entidades.jogador;
import entidades.ElementoDoJogo;
import java.awt.*;
import java.io.File;
import javax.swing.ImageIcon;

/*
Note que, o eixo das coordenadas y diminui conforme o ponto se aproxima do topo da tela.
Os metodos que influenciam essa coordenada foram feitos com essa logica em mente
*/

public class Jogador extends ElementoDoJogo {
    /*Controle de gravidade */
    private double velocidadeY = 0;
    private boolean noChao = false;
    private final int limiteChao;

    /*Controle da colizão */
    private final int margemDeFolga = 15;

    /*Controle da animação */
    private final Image[] sprites; // O Array que guarda as imagens
    private int frameAtual = 0; // Qual índice do array desenhar (0, 1, 2...)
    private int contadorFrames = 0; // Controla a velocidade da troca
    private final int velocidadeAnimacao = 5; // Troca de imagem a cada 5 atualizações
    private final int quantidadeDeFrames = 4; // quantidade de imagens na animação

    /*Controle da mudança de tamanho */
    private final int larguraOriginal;
    private final int alturaOriginal;
    private boolean Encolhido = false;

    public Jogador(String caminhoBase) {
        super(50, 220, 80, 112); 
        larguraOriginal = 80;
        alturaOriginal = 112;

        // Calcula a aultura y que vai ser adotada como chão
        // Apesar de ser o chão, a camada limita o jogodor apartir do topo(cabeça)
        limiteChao = 340 - alturaOriginal;

        // Inicializa o array que garda os sprits da animação
        sprites = new Image[quantidadeDeFrames];
        
        // Carrega as imagens que formam a animação
        carregarSprites(caminhoBase);
    }

    private void carregarSprites(String caminho) {
        for (int i = 0; i < quantidadeDeFrames; i++) {
            // Monta o nome do arquivo: "caminho" + "/Frame (i).png"
            String caminhoFinal = caminho + "/Frame (" + (i+1) + ").png";
            
            try {
                // Tenta carregar cada imagem, emite um erro para cada imagem faltante
                File arq = new File(caminhoFinal);
                if (arq.exists()) {
                    sprites[i] = new ImageIcon(caminhoFinal).getImage();
                } else {
                    System.err.println("Frame faltando: " + caminhoFinal);
                }
            } catch (Exception e) {}
        }
    }

    public void pular() {
        if (Encolhido) {
            // Garante que o personagem esteja no tamanho correto antes do pulo
            ResetarTamanho();
        }

        if (noChao) {
            // Se estiver no chão, gera uma aceleração no eixo y (pulo)
            velocidadeY = -14; // Aceleração negativa pois o eixo y diminui conforme se aproxima do topo
            noChao = false;
        }
    }

    public void interromperPulo(){
        if(!noChao && velocidadeY < 0){
            // Enquanto esta subindo no ar, diminui a velocidade de subida pela metade
            // Efeito pratico, o pulo se encerra mais cedo
            velocidadeY = velocidadeY * 0.5;
        }
    }

    public void Encolher(){
        if(noChao){
            // Se estiver no chão, diminui o tamanho do jogador pela metade
            largura = larguraOriginal/2;
            altura = alturaOriginal/2;

            // Posiciona o personagem na altura do chão
            y = limiteChao + (alturaOriginal - altura);

            Encolhido = true;
        }
    }

    public void ResetarTamanho(){
        if(Encolhido){
            // Se estiver encolhido, restaura o personagem ao seu tamanho original
            largura = larguraOriginal;
            altura = alturaOriginal;
            y = limiteChao;
            Encolhido = false;
        }
    }

    @Override public void atualizar() {
        // Aplica a gravidade
        y += velocidadeY;
        velocidadeY += 1;

        // Garante que o personagem não vai ficar abaixo do limite do solo
        if(!Encolhido){
            if (y >= limiteChao) { 
                y = limiteChao;
                velocidadeY = 0;
                noChao = true;
            }
        }
        /*
        Note que o limite do chão é diferente para o personagem regular e encolhido
        uma vez que ele é medido a partir do topo do personagem
        */
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

    @Override public void desenhar(Graphics g) {
        // Verifica se a imagem do índice atual existe no Array
        if (sprites[frameAtual] != null) {
            // Desenha a imagem armazenada no índice 'frameAtual'
            g.drawImage(sprites[frameAtual], x, y, largura, altura, null);
        } else {
            // Se não tiver imagem, desenha um retangulo magenta no lugar
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, largura, altura);
        }
    }

    public boolean colideCom(int inX, int inY, int inL, int inA) {
        /* Aplica uma margem de tolerancia */
        int hitX = x + margemDeFolga;
        int hitY = y + margemDeFolga;
        int hitL = largura -(margemDeFolga*2);
        int hitA = altura - (margemDeFolga*2);

        /* Teste AABB */
        return
        // 1. Esquerda do Jogador antes da Direita do Inimigo
        // False - jogador já passou do inimigo
        hitX < inX + inL && 

        // 2. Direita do Jogador depois da Esquerda do Inimigo
        // False - jogador não chegou no inimigo
        hitX + hitL > inX &&

        // 3. Topo do Jogador acima da Base do Inimigo
        // False - jogador está abaixo do inimigo
        hitY < inY + inA &&
        
        // 4. Base do Jogador abaixo do Topo do Inimigo
        //False - jogador está acima do inimigo 
        hitY + hitA > inY;
    }
}