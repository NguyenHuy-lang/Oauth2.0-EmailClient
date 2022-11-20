module com.example.fxmailclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires static lombok;
    requires org.jsoup;
    requires commons.email;
    requires javafx.web;
    requires json.simple;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    opens com.example.fxmailclient to javafx.fxml;
    exports com.example.fxmailclient;
    exports com.example.fxmailclient.Model;
    opens com.example.fxmailclient.Model to javafx.fxml;
    exports com.example.fxmailclient.Controller;
    opens com.example.fxmailclient.Controller to javafx.fxml;
    exports com.example.fxmailclient.Solves;
    opens com.example.fxmailclient.Solves to javafx.fxml;
}