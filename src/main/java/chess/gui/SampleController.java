package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static javafx.scene.paint.Color.*;

public class SampleController {

    private static CoreGame coreGame;
    private Rectangle actualPos;

    public Move handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle r = (Rectangle) mouseEvent.getTarget();
            if(actualPos==null){
                actualPos = r;
                actualPos.setStroke(CYAN);
                actualPos.setStrokeWidth(5);
                actualPos.setStrokeType(StrokeType.INSIDE);

            }else{
                Move move = new Move(new Position((int)actualPos.getX()-1,(int)actualPos.getY()-1),new Position((int)r.getX()-1,(int)r.getY()-1));
                actualPos.setStroke(color(0.97,0.69,0.53));
                actualPos.setStrokeWidth(1);
                actualPos.setStrokeType(StrokeType.OUTSIDE);
                actualPos = null;
                return move;
            }
        }
        return null;
    }

}