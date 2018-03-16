package laboratoriodistribuido;

import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";

        //creo el socket del cliente
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(serverAddress, 20000);
        } catch (UnknownHostException e) {
            System.err.println("No se encuentra el servidor.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("No se puede conectar a " + serverAddress + ".");
            System.exit(1);
        }
        System.out.println("Cliente se conecto con " + serverAddress + " exitosamente.");

        //crea un buffer de escritura o de salida (out): para enviar al servidor
        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);

        //crea un buffer de lectura o de entrada (in): para recibir del servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));

        //crea un buffer para leer la entrada por consola
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        //variable para guardar lo escritor por consola
        String userInput = "";

        while (!userInput.equals("adios")) {
            System.out.print("Escriba un mensaje: ");
            //readLine: bloquea hasta que recibe un mensaje por consola
            userInput = stdIn.readLine();
            //envia el mensaje al servidor
            out.println(userInput);
            //readLine: bloquea hasta que recibe una respuesta del servidor
            System.out.println(in.readLine());
        }

        //cierra los buffer de comunicacion con el servidor y de entrada por consola
        out.close();
        in.close();
        stdIn.close();
        
        //cierra el socket del cliente
        clientSocket.close();
    }
}
