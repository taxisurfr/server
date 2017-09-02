package com.taxisurfr.util;

import com.sendgrid.*;
import com.taxisurfr.domain.Profile;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Logger;

@Stateless
public class SendGridSender {
    @Inject
    Logger logger;

    public void send(String subject, String toEmail, String htmlBody, Profile profile) {
        sendWithFrom(subject,"dispatch@taxisurfr.com", toEmail, htmlBody,  profile);
    }
    public void sendWithFrom(String subject, String fromEmail, String toEmail, String htmlBody, Profile profile) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlBody);
        Mail mail = new Mail(from, subject, to, content);

        logger.info("key" + profile.getSendGridKey());
        SendGrid sg = new SendGrid(profile.getSendGridKey());
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private static final String EMAIL = "[{\"email\":\"XXX\"}]";

    public void addRecepient(Profile profile, String email) {
        try {
            SendGrid sg = new SendGrid(profile.getSendGridKey());

            Request request = new Request();
            request.method = Method.POST;
            request.endpoint = "contactdb/recipients";

            request.body = EMAIL.replace("XXX",email);
            Response response = sg.api(request);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
