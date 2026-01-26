import java.awt.*;

// NÍVEL 1: Classe Base Abstrata
abstract class ElementoDoJogo {
    protected int x, y, largura, altura;

    public ElementoDoJogo(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.largura = w;
        this.altura = h;
    }

    // Polimorfismo será aplicado aqui
    public abstract void atualizar();
    public abstract void desenhar(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}