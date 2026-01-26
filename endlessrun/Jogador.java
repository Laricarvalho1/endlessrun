import java.awt.*;


class Jogador extends ElementoDoJogo {
    private int velocidadeY = 0;
    private boolean noChao = false;

    public Jogador() {
        super(50, 250, 40, 40);
    }

    public void pular() {
        if (noChao) {
            velocidadeY = -15; // Força do pulo
            noChao = false;
        }
    }

    @Override
    public void atualizar() {
        // Simples física de gravidade
        y += velocidadeY;
        velocidadeY += 1; // Gravidade

        if (y >= 260) { // Chão
            y = 260;
            velocidadeY = 0;
            noChao = true;
        }
    }

    @Override
    public void desenhar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, largura, altura);
    }
}