package src.entidades;
import java.awt.*;

public abstract class ElementoDoJogo {
    public int x, y, largura, altura;

    public ElementoDoJogo(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    public abstract void atualizar();
    public abstract void desenhar(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}