package com.example.fxmailclient.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import lombok.Data;
@Data
public class SendMailController implements Initializable {
    private Session session;
    @FXML
    private TextField sendTo;
    @FXML
    private TextField header;
    @FXML
    private TextArea content;
    @FXML
    private Button buttonSend;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String receive = sendTo.getText();
                String headers = header.getText();
                String contents = content.getText();
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("huynguyen-langd19ptit@outlook.com"));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(receive));
                    message.setSubject(headers);
                    message.setText(contents);

                    Transport.send(message);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Bạn đã gửi thành công");

                    if(alert.showAndWait().get() == ButtonType.OK) {
                        sendTo.setText("");
                        header.setText("");
                        content.setText("");

                    }
                } catch (Exception e) {
                    System.out.println("false");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Faile");
                    alert.setContentText("Bạn gửi thật bại");
                    if(alert.showAndWait().get() == ButtonType.OK) {
                        sendTo.setText("");
                        header.setText("");
                        content.setText("");

                    }
                }
            }
        });
    }
}
