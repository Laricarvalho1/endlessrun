package src.entidades.inimigos;
import java.awt.*;
import java.io.File;
import javax.swing.ImageIcon;

import src.entidades.ElementoDoJogo;

public class Inimigo extends ElementoDoJogo {
    protected double velocidade;
    protected Image imagem;

    public Inimigo(int x, int y, int largura, int altura) {
        super(x, y, largura, altura);
        this.velocidade = 5;
    }

    public Inimigo(int x, int y, int w, int h, int velocidade) {
        super(x, y, w, h);
        this.velocidade = velocidade;
    }

    @Override
    public void atualizar(){}

    public void atualizar(double velocidadeDoJogo) {
        this.x -= velocidade*velocidadeDoJogo; // Inimigos andam para a esquerda
    }

    protected void carregarImagem(String caminho) {
        try {
            File arquivo = new File(caminho);
            if (arquivo.exists()) {
                ImageIcon ref = new ImageIcon(caminho);
                this.imagem = ref.getImage();
            } else {
                System.err.println("Imagem de inimigo não encontrada: " + caminho);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void desenhar(Graphics g) {
        if(imagem != null){
            g.drawImage(imagem, x, y, largura, altura, null);
        }
        else{
            g.setColor(Color.RED);
            g.fillRect(x, y, largura, altura);
        }
    }
}