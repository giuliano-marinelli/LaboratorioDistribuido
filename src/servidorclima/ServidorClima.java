package servidorclima;

/**
 * Recibir parámetro: [PORT]
 */
import java.io.*;
import java.net.*;

public class ServidorClima {

    private static int PORT;

    public static void main(String args[]) throws IOException {
        PORT = Integer.parseInt(args[0]);
        ServerSocket serverSocket;
        System.out.print("Servidor Clima> Iniciando... ");
        try {
            //crea el socket del servidor
            serverSocket = new ServerSocket(PORT);
            System.out.println("\t[OK]");
            System.out.println("Servidor Clima> Esperando conexiones...");
            int idSession = 0;
            while (true) {
                Socket clientSocket;
                //bloquea hasta que entra una conexion de cliente (se encolan)
                clientSocket = serverSocket.accept();
                System.out.println("Servidor Clima> Nueva conexión entrante: " + clientSocket);
                //lanza un hilo para atender al cliente
                ((ServidorClimaHilo) new ServidorClimaHilo(clientSocket, idSession)).start();
                idSession++;
            }
        } catch (IOException ex) {
            System.err.println("Servidor Clima> " + ex.getMessage());
        }
    }

}
