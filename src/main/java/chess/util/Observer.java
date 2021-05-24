package chess.util;

import chess.figures.Figure;

public interface Observer {
	public void updateBeatenFigures(int posX, int posY);
	public void updateExtraMoves(int posX, int posY, int newX, int newY);
	public void updateChange(int posX, int posY, int changeTo, boolean isBlackTeam);
}
