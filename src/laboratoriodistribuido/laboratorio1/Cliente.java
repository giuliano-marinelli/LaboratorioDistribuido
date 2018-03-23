package laboratoriodistribuido.laboratorio1;

import java.io.*;
import java.net.*;

public class Cliente {

    private final static int CENTRAL_PORT = 10000;
    private final static String SERVER = "localhost";

    public static void main(String[] args) throws IOException {
        try {
            System.out.print("Cliente> Iniciando...");
            //crea el socket para comunicar con el servidor
            Socket querySocket = new Socket(SERVER, CENTRAL_PORT);

            //inicializa los buffers para comunicarse con el servidor
            DataInputStream inBuffer;
            DataOutputStream outBuffer;
            outBuffer = new DataOutputStream(querySocket.getOutputStream());
            inBuffer = new DataInputStream(querySocket.getInputStream());

            //inicializa el buffer para leer por consola           
            BufferedReader inConsole = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\t[OK]");

            String query = "", answer = "";

            while (!query.equals("salir")) {
                System.out.print("Cliente> Escriba consulta: ");

                //captura consulta escrita por consola
                query = inConsole.readLine();

                //envia peticion al servidor
                outBuffer.writeUTF(query);

                //recibe respuesta del servidor
                answer = inBuffer.readUTF();

                System.out.println("Servidor> Respuesta: " + answer);
            }

            //cierra el socket con el servidor
            querySocket.close();
        } catch (IOException ex) {
            System.err.println("Cliente> " + ex.getMessage());
        }
    }
}
