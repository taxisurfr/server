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
        logger.info("confirmation to:"+toEmail+" "+subject);
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlBody);
        Mail mail = new Mail(from, subject, to, content);

        logger.info("key" + profile.getSendGridKey());
        SendGrid sg = new SendGrid(profile.getSendGridKey());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("statusCode:"+response.getStatusCode());
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private static final String EMAIL = "[{\"email\":\"XXX\"}]";

    public void addRecepient(Profile profile, String email) {
        try {
            SendGrid sg = new SendGrid(profile.getSendGridKey());

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("contactdb/recipients");

            request.setBody(EMAIL.replace("XXX",email));
            Response response = sg.api(request);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
