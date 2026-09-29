package tema1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlExample2 {

    public static void main(String[] args) {
        try {
            URL url = new URL("https://ktvconstantina.es");  /* Se cambia la URL de java.com porque devuelve un error HTTP 403 al intentar acceder desde este programa.*/
        
            try 
            (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream(),
                            StandardCharsets.UTF_8))) {

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            }

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}