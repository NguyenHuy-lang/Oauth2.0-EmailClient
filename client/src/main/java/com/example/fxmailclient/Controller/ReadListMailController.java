package com.example.fxmailclient.Controller;
import com.example.fxmailclient.Model.Mailbox;
import com.example.fxmailclient.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Data;
import org.json.JSONArray;

import javax.mail.Session;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ResourceBundle;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

@Data
@Getter
@Setter
public class ReadListMailController implements Initializable {
    private Session session;
    @FXML
    private TableView<Mailbox> table;
    @FXML
    private TableColumn<Mailbox, String> address;
    @FXML
    private TableColumn<Mailbox, String> sentDateTime;
    @FXML
    private TableColumn<Mailbox, String> subject;
    @FXML
    private TableColumn<Mailbox, String> body;

    @FXML
    private Label sendto;
    @FXML
    private Label subjectsend;
    @FXML
    private Button buttonsend;
    @FXML
    private TextArea inputcontent;
    @FXML
    private TextField inputsendto;
    @FXML
    private TextField inputsubject;

    ObservableList<Mailbox> observableList;
    private static ArrayList<Mailbox> readMailByOauth(String token) throws IOException {
        URL obj = new URL("https://graph.microsoft.com/v1.0/me/messages");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String response = "";
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
                System.out.println(response.getClass().getSimpleName());
            }
            in.close();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray geodata = jsonObject.getJSONArray("value");

            final int n = geodata.length();
            ArrayList<Mailbox> list = new ArrayList<>();

            for (int i = 0; i < n; ++i) {
                final JSONObject mail = geodata.getJSONObject(i);
                String from = "";
                try{
                    from = mail.getJSONObject("sender").getJSONObject("emailAddress").getString("address");
                } catch (JSONException e){
                    from = "none";
                }
                Mailbox mailbox = new Mailbox(
                        from,
                        mail.getString("sentDateTime"),
                        mail.getString("subject"),
                        mail.getString("bodyPreview")

                );
                list.add(mailbox);
            }
            return list;
        }

        return null;
    }
    private void init() throws IOException {
        ArrayList<Mailbox> list = new ArrayList<>();
        list = readMailByOauth(getToken());
        observableList = FXCollections.observableArrayList(
            list
        );
    }

    private String getToken() throws IOException {
        URLConnection connection = new URL("http://localhost:8080/token").openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        int c;
        String token = "";
        while ((c = bufferedReader.read()) != - 1) {
            token += (char)c;
        }

        bufferedReader.close();
        is.close();
        return token;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        address.setCellValueFactory(new PropertyValueFactory<Mailbox, String>("address"));
        sentDateTime.setCellValueFactory(new PropertyValueFactory<Mailbox, String>("sentDateTime"));
        subject.setCellValueFactory(new PropertyValueFactory<Mailbox, String>("subject"));
        body.setCellValueFactory(new PropertyValueFactory<Mailbox, String>("body"));
        table.setItems(observableList);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("OH SNAP");
        alert.setContentText("Bạn đã gửi vượt quá số tin nhắn được phép trong ngày");
        buttonsend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String sendto = inputsendto.getText();
                String subject = inputsubject.getText();
                String content = inputcontent.getText();






                HttpClient httpClient = HttpClientBuilder.create().build();
                try {
                    HttpPost request = new HttpPost("https://graph.microsoft.com/v1.0/me/sendMail");
                    request.addHeader("Authorization", "Bearer " + getToken());
                    // child body
                    JSONObject tmp = new JSONObject();
                    tmp.put("contentType", "Text");
                    tmp.put("content", content);
                    // json body
                    JSONObject tmp1 = new JSONObject();
                    tmp1.put("address", sendto);
                    JSONObject emailAddress = new JSONObject();
                    emailAddress.put("emailAddress", tmp1);
                    JSONArray array = new JSONArray();
                    array.put(emailAddress);
                    JSONObject json = new JSONObject();
                    json.put("subject", subject);
                    json.put("body", tmp);
                    json.put("toRecipients", array);
                    JSONObject json2 = new JSONObject();
                    json2.put("message", json);
                    StringEntity bodys = new StringEntity(json2.toString());
                    request.addHeader("content-type", "application/json");
                    request.setEntity(bodys);
                    HttpResponse response = httpClient.execute(request);
                    int len = response.getStatusLine().toString().length();
                    String error = "429";
                    String status = response.getStatusLine().toString().substring(len - 4 , len - 1);
                    if (status.compareTo(error) == 0) {
                        System.out.println("show");
                        alert.showAndWait();
                    } else {
                        System.out.println(status.compareTo(error) + status.length() + " " + error.length());
                    }
                    System.out.println(response.getStatusLine().toString());
                } catch (Exception ex) {
                }
            }


        });

    }
}
