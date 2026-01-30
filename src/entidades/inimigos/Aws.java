package entidades.inimigos;
public class Aws extends Inimigo {
    public Aws(int x, int y){
        super(x, y, 70, 35);
        carregarImagem("src/Assets/Inimigos/Aws.png");
    }
}
