package tema1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplyServer {

    public static void main(String[] args) {
        int portNumber = 12345;

        System.out.println("Servidor en ejecución, esperando conexión...");

        try (ServerSocket serverSocket = new ServerSocket(portNumber);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Se ha conectado un cliente");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Se ha recibido: " + inputLine);

                try {
                    long received = Long.parseLong(inputLine);
                    out.println(received * 2);
                } catch (NumberFormatException e) {
                    out.println("El texto recibido no es un número válido");
                }
            }

            System.out.println("Se ha desconectado el cliente");

        } catch (IOException e) {
            System.out.println("Se ha producido un error: " + e.getMessage());
        }

        System.out.println("Programa finalizado");
    }
}
