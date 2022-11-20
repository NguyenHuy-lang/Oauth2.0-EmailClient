package com.example.demo.controllers;

import com.example.demo.Utilities;

import com.nimbusds.jose.shaded.json.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/", produces = "application/json")
public class HomeController {
    @GetMapping( "/home" )
    public String home( @AuthenticationPrincipal(expression = "claims['name']") String name) {

        return String.format( "Hello %s!  welcome to the Security app", name);
    }
    @GetMapping( "/message" )
    public String home(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient graphAuthorizedClient) throws  IOException {

        ArrayList<String> v = Utilities.graphUserProperties(graphAuthorizedClient);
        FileWriter myWriter = new FileWriter("token.txt");
        System.out.println(v.get(v.size() - 1));
        myWriter.write(v.get(v.size() - 1));
        myWriter.close();
        return "Bạn đã thực hiện xác thực thành công, vui vòng mở quay lại email-client app";
    }
    @RequestMapping("token")
    public String getToken() throws IOException {
        FileReader fr = new FileReader("C:\\Users\\Dell service\\Desktop\\fa-4_12\\server\\token.txt");
        int i;
        String token = "";
        while ((i = fr.read()) != -1) {
            token += (char) i;
        }
        fr.close();
        return token;
//        return sendGET(token);
    }
    private static String sendGET(String token) throws IOException {
        URL obj = new URL("https://graph.microsoft.com/v1.0/me/messages");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        String answer = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("error");
        }
        return answer;
    }
}
