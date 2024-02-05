package com.example.chatapplicationserverfx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private AnchorPane apMain;

    @FXML
    private Button buttonSend;

    @FXML
    private TextField tfMessage;

    @FXML
    private ScrollPane spMain;

    @FXML
    private VBox vboxMessages;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            server = new Server(new ServerSocket(1234));
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("ERROR CREATING THE SERVER");
        }

        // makes the scroll start at the bottom if there is a new messages
        vboxMessages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                spMain.setVvalue((Double) newValue);

            }
        });

        // appending the message received to the vertical box
        // this is will be on a separate thread because the waiting for message is a blocking operation
        // we want to be able to send messages at the same time as well
        server.receiveMessageFromClient(vboxMessages);

        // adding functionality to the send button
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // send the message to the client
                String messageToSend = tfMessage.getText();

                // check if the message is not empty
                if(!messageToSend.isEmpty()){
                    // message is not empty
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));

                    Text text = new Text(messageToSend);
                    // allows the text object to be styled so that it can be wrapped, etc
                    TextFlow textFlow = new TextFlow(text);

                    // the styling
                    textFlow.setStyle("-fx-color: rgb(239, 242, 255 " + "-fx-background-color: rgb(15, 125, 242)" +
                            " -fx-background-radius: 20px");

                    textFlow.setPadding(new Insets(5, 10, 5,10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    // add this to the hBox
                    hBox.getChildren().add(textFlow);
                    vboxMessages.getChildren().add(hBox);

                    server.sendMessageToClient(messageToSend);
                    tfMessage.clear();


                }
            }
        });







    }

    public static void addLabel(String messageFromClient, VBox vBox){
        HBox hBox = new HBox();
        // on the left this time because we are receiving the message
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle( "-fx-background-color: rgb(233, 233, 235)" +
                " -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5,10));

        hBox.getChildren().add(textFlow);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });


    }
}