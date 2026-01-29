package src.entidades.inimigos;
public class Teams extends Inimigo {
    public Teams(int x, int y){
        super(x, y, 65, 70);
        carregarImagem("src/Assets/Inimigos/Teams.png");
    }
}
