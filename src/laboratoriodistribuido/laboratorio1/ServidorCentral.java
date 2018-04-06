package laboratoriodistribuido.laboratorio1;

/**
 * Recibir parámetro: [CENTRAL_PORT]
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServidorCentral {

    private static int CENTRAL_PORT;
    private static HashMap<String, String> cacheWeather;
    private static HashMap<String, String> cacheHoroscope;

    public static void main(String args[]) throws IOException, InterruptedException {
        CENTRAL_PORT = Integer.parseInt(args[0]);
        ServerSocket serverSocket;
        System.out.print("Servidor> Iniciando... ");
        try {
            //inicializar las caches
            cacheWeather = new HashMap<>();
            cacheHoroscope = new HashMap<>();
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
                ((ServidorCentralHilo) new ServidorCentralHilo(clientSocket, idSession, cacheWeather, cacheHoroscope)).start();
                idSession++;
            }
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        }
    }
}
