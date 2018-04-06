package laboratoriodistribuido.laboratorio1;

/**
 * Recibir parámetro: [PORT]
 */
import java.io.*;
import java.net.*;

public class ServidorHoroscopo {

    private static int PORT;

    public static void main(String args[]) throws IOException {
        PORT = Integer.parseInt(args[0]);
        ServerSocket serverSocket;
        System.out.print("Servidor Horoscopo> Iniciando... ");
        try {
            //crea el socket del servidor
            serverSocket = new ServerSocket(PORT);
            System.out.println("\t[OK]");
            System.out.println("Servidor Horoscopo> Esperando conexiones...");
            int idSession = 0;
            while (true) {
                Socket clientSocket;
                //bloquea hasta que entra una conexion de cliente (se encolan)
                clientSocket = serverSocket.accept();
                System.out.println("Servidor Horoscopo> Nueva conexión entrante: " + clientSocket);
                //lanza un hilo para atender al cliente
                ((ServidorHoroscopoHilo) new ServidorHoroscopoHilo(clientSocket, idSession)).start();
                idSession++;
            }
        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> " + ex.getMessage());
        }
    }

}
