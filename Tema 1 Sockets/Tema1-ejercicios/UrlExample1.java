package tema1;

import java.net.URL;

public class UrlExample1 {
public static void main(String[] args) throws Exception  {
URL url=new URL("http://www.example.com:8080/index.php?user=test&password=test");

        System.out.println("Protocolo: " + url.getProtocol());
        System.out.println("Host: "+ url.getHost()); 
        System.out.println("Port: "+url.getPort()); 
        System.out.println("Path: "+ url.getPath());
        System.out.println("Query String: "+ url.getQuery());
        System.out.println("File: "+ url.getFile());
    }
}