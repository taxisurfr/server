package com.taxisurfr.rest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

public class GoogleVerifier {

    public static String getEmail(String idTokenString) throws GeneralSecurityException, IOException {
        String email = null;
//        String idTokenString =
//            "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1ODlhYzVjMmEwMTJmZjM1ZGJiYmJjNmIwODcyNzkwYzgzMjNjMWMifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNDgxNjExMTMzLCJleHAiOjE0ODE2MTQ3MzMsImF0X2hhc2giOiJQdm1nUC1xU1FHbjRxQjZkZ0otS3F3IiwiYXVkIjoiNDY4MTUwODY2NjQzLWh2aTU0NGIzanAwYmpwdGQ0NDRnc2czNjVuZDU3Nmo2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTAxMzc0NTEwNTE1NjA3NTAyNTQ1IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF6cCI6IjQ2ODE1MDg2NjY0My1odmk1NDRiM2pwMGJqcHRkNDQ0Z3NnMzY1bmQ1NzZqNi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImhkIjoidGF4aXN1cmZyLmNvbSIsImVtYWlsIjoiZGlzcGF0Y2hAdGF4aXN1cmZyLmNvbSIsIm5hbWUiOiJQZXRlciBIYWxsIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS8tcFZ0cXFSV3p5WU0vQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvTXB2cHBUbGZ3Vncvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IlBldGVyIiwiZmFtaWx5X25hbWUiOiJIYWxsIiwibG9jYWxlIjoiZW4tQVUifQ.UUCTmnivjjv9ZUgcKvJFkfh2YbFDaeaY11Yr6hAyJRmmN8UUIpYZ2bg7hbH6Ikw-BHBGmeBGD6LbxpDh1rmr9G_QKPAJp5FOEgAkGyQ1CRIakAAc5ycjGTiX_WhkvEhKYq2rv7t0X_ktrP8yPXlf_R6taoIjfPXb166Pxadh98oHkZTrNgsB4mLcfnmuO5i4XRGKzwZTcwMiaeU54su3EkqTX620B5q65guki1grVmYgDyJVGasIDeSIJOZFmh_reMGcSP67kc2BeCcPRcC0I6cT81DRCXnx-XgST7Cw9snyz-X7oqiQOlQL4j8VXFAhMZCGJp7OrRGjZwKFNnAtWg";
        String CLIENT_ID =
            "468150866643-hvi544b3jp0bjptd444gsg365nd576j6.apps.googleusercontent.com";
        GoogleIdTokenVerifier verifier =
            new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            email = payload.getEmail();
            System.out.println("email:"+email);
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...

        } else {
            System.out.println("Invalid ID token.");
        }
        return email;

    }
}
