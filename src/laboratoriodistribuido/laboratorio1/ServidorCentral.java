package laboratoriodistribuido.laboratorio1;

import java.io.*;
import java.net.*;

public class ServidorCentral {

    private final static int CENTRAL_PORT = 10000;

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket;
        System.out.print("Servidor> Iniciando... ");
        try {
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
                ((ServidorCentralHilo) new ServidorCentralHilo(clientSocket, idSession)).start();
                idSession++;
            }
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        }
    }
}
