package chess.util;

import chess.figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class Observable {
	private static List<Observer> observers = new ArrayList<Observer>();
	
	public static void addObserver(Observer obs) {
		observers.add(obs);
	}
	
	public static void removeObserver(Observer obs) {
		observers.remove(obs);
	}
	
	public static void clearObservers() {
		observers.clear();
	}
	
	public static void notifyObserversForDelete(int posX, int posY) {
		for (Observer o : observers) {
			o.updateBeatenFigures(posX, posY);
		}
	}

	public static void notifyObserversForMovement(int posX, int posY, int newX, int newY) {
		for (Observer o : observers) {
			o.updateExtraMoves(posX, posY, newX, newY);
		}
	}

	public static void notifyObserversForChange(int posX, int posY, int changeTo, boolean isBlackTeam) {
		for (Observer o : observers) {
			o.updateChange(posX, posY, changeTo, isBlackTeam);
		}
	}
}
