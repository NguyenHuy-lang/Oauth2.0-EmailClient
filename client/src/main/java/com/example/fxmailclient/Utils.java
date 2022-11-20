package com.example.fxmailclient;

import com.example.fxmailclient.Controller.ReadListMailController;
import com.example.fxmailclient.Controller.SendMailController;
import com.example.fxmailclient.Model.Mailbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import javax.mail.Session;
import java.io.IOException;

public class Utils {

    public static void changeSceneToSend(ActionEvent event, String fxml, String title, Session session) throws IOException {
        Parent root = null;

        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxml));
        root = loader.load();

        SendMailController cvc = loader.getController();
        cvc.setSession(session);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }

    public static void changeSceneToRead(ActionEvent event, String fxml, String title, Session session) throws IOException {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxml));
        root = loader.load();
        ReadListMailController cvc = loader.getController();
        cvc.setSession(session);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600,400));
        stage.show();

    }


}
