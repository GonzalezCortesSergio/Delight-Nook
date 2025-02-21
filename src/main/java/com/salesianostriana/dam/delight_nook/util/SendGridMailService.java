package com.salesianostriana.dam.delight_nook.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridMailService {

    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    @Async
    public void sendMail(String to, String subject, String message) throws IOException {

        Email from = new Email("assper1122@gmail.com");
        Email emailTo = new Email(to);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, emailTo, content);

        SendGrid sg = new SendGrid(sendgridApiKey);

        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
    }
}
