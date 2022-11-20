package com.example.fxmailclient.Model;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mailbox {
    private String address;
    private String sentDateTime;
    private String subject;
    private String body;


}

