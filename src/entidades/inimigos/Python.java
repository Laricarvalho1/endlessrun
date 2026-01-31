package entidades.inimigos;
public class Python extends Inimigo {
    public Python(int x, int y){
        super(x, y, 60, 60);
        carregarImagem("src/Assets/Inimigos/Python.png");
    }
}
