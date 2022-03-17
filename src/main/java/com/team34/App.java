package com.team34;

import com.team34.model.Project;
import com.team34.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Project model = new Project();
        Project.UserPreferences userPrefs = model.getUserPreferences();
        MainView view = new MainView(stage, userPrefs.windowWidth, userPrefs.windowHeight, userPrefs.windowMaximized);
        new MainController(view, model);
    }

    public static void main(String[] args) {
        launch();
    }
}