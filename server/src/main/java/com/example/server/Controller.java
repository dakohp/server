package com.example.server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button sendingButton;
    @FXML
    private TextField textField;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            server = new Server(new ServerSocket(1234));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Blad w tworzeniu");
        }

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

        server.receiveMessFromClient(vBox);

        sendingButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                extracted();
            }
        });

        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    extracted();
                }
            }
        });
    }
    public void extracted() {
        String messToSend = textField.getText();
        if (!messToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 10, 5, 10));

            Text text = new Text(messToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle(
                    "-fx-color:rgb(239,242,255);" +
                            "-fx-background-color:rgb(15,125,242);" +
                            "-fx-background-radius:10px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(1, 1, 1));

            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);

            server.sendMessToClinet(messToSend);
            textField.clear();
        }
    }
    public static void addLabel(String messFromClient, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(messFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
                "-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 10px;");

        textFlow.setPadding(new Insets(5, 10, 5, 5));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}