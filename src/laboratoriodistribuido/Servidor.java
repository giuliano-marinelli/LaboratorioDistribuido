package laboratoriodistribuido;

import java.io.*;
import java.net.*;
//import java.util.logging.*;

public class Servidor {

    public static void main(String args[]) throws IOException {
        //creo el socket del servidor
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(20000);
        } catch (IOException e) {
            System.err.println("Servidor no puede escuchar en puerto 20000.");
            System.exit(1);
        }

        System.out.println("Servidor inicializado correctamente.");

        //creo el socket para un cliente
        Socket clientSocket = null;
        try {
            System.out.println("Servidor a la espera de cliente...");
            //accept: bloquea hasta que recibe la conexion de un cliente
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Falla de conexion con el cliente.");
            System.exit(1);
        }

        System.out.println("Cliente conectado exitosamente.");

        //crea un buffer de escritura o de salida (out): para enviar al cliente
        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);

        //crea un buffer de lectura o de entrada (in): para recibir del cliente
        BufferedReader in = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));

        //variables para manejar lo que recibe y lo que envia al cliente
        String inputLine = "", outputLine = "";

        
        while (!inputLine.equals("adios")) {
            System.out.println("Esperando mensaje del cliente...");
            //readLine: bloquea hasta que recibe un mensaje del cliente
            inputLine = in.readLine();
            //pasa el mensaje recibido a minusculas
            inputLine = inputLine.toLowerCase();
            System.out.println("Mensaje recibido: "+inputLine);
            
            //lee el mensaje del cliente y decide una respuesta (en outputLine)
            if (inputLine.equals("hola")) {
                outputLine = "Buen dia";
            } else if (inputLine.equals("adios")) {
                outputLine = "Hasta luego";
            } else {
                outputLine = "No entiendo";
            }

            //envia la respuesta al cliente
            out.println(outputLine);
        }

        //cierra los buffer de comunicacion con el cliente
        out.close();
        in.close();

        //cierra los socket del servidor y del cliente
        clientSocket.close();
        serverSocket.close();
    }
}
