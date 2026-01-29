package src.entidades.inimigos;
public class Python extends Inimigo {
    public Python(int x, int y){
        super(x, y, 40, 40);
        carregarImagem("src/Assets/Inimigos/Python.png");
    }
}
