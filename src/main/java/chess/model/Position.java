package chess.model;

public class Position {

    private int posX, posY;

    public Position(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public void setPosX(int posX){
        this.posX = posX;
    }

    public void setPosY(int posY){
        this.posY = posY;
    }

    public int getPosX(){
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }
}
