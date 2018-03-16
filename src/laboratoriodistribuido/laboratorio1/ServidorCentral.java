package laboratoriodistribuido.laboratorio1;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServidorCentral {

    private final static int CENTRAL_PORT = 10000;
    private static HashMap<String, String> cacheClima;
    private static HashMap<String, String> cacheHoroscopo;

    public static void main(String args[]) throws IOException, InterruptedException {
        ServerSocket serverSocket;
        System.out.print("Servidor> Iniciando... ");
        try {
            //inicializar las caches
            cacheClima = new HashMap<>();
            cacheHoroscopo = new HashMap<>();
            //crea el socket del servidor
            serverSocket = new ServerSocket(CENTRAL_PORT);
            System.out.println("\t[OK]");
            System.out.println("Servidor> Esperando conexiones...");
            int idSession = 0;
            while (true) {
                Socket clientSocket;
                //bloquea hasta que entra una conexion de cliente (se encolan)
                clientSocket = serverSocket.accept();
                System.out.println("Servidor> Nueva conexión entrante: " + clientSocket);
                //lanza un hilo para atender al cliente
                ((ServidorCentralHilo) new ServidorCentralHilo(clientSocket, idSession, cacheClima, cacheHoroscopo)).start();
                idSession++;
            }
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        }
    }
}
