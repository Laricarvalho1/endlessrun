import java.awt.*;
// NÍVEL 2: Classe Intermediária
class Inimigo extends ElementoDoJogo {
    protected int velocidade;

    // Polimorfismo de Sobrecarga (Construtor 1)
    public Inimigo(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.velocidade = 5; // velocidade padrão
    }

    // Polimorfismo de Sobrecarga (Construtor 2)
    public Inimigo(int x, int y, int w, int h, int velocidade) {
        super(x, y, w, h);
        this.velocidade = velocidade;
    }

    @Override // Polimorfismo de Sobrescrita
    public void atualizar() {
        this.x -= velocidade; // Inimigos andam para a esquerda
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, largura, altura);
    }
}