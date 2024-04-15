package ch.hearc.heg.scl.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Generic {
    public static String getWebResponse(String service) {
        String response = "";

        try {
            URL url = new URL(service);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.connect();

            BufferedReader br;
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.lines().collect(Collectors.joining());
            } else {
                System.out.println("Erreur lors de la récupération de la réponse");
            }
        }catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la réponse");
            e.printStackTrace();
        }finally {
            //System.out.println(response);
            return response;
        }
    }
}
