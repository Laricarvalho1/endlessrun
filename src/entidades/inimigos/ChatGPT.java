package entidades.inimigos;
public class ChatGPT extends Inimigo {
    public ChatGPT(int x, int y){
        super(x, y, 140, 50);
        carregarImagem("src/Assets/Inimigos/ChatGPT.png");
    }
}
