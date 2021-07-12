/**
 * The main module of the chess application.
 */
module chess {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;

    exports chess.gui;
    opens chess.gui;
    exports chess.managers;
    opens chess.managers;
}
