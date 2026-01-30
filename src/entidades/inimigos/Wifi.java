package entidades.inimigos;
public class Wifi extends Inimigo {
    public Wifi(int x, int y){
        super(x, y, 60, 60);
        carregarImagem("src/Assets/Inimigos/Wi-fi.png");
    }
}
