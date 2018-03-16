package laboratoriodistribuido.laboratorio1;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ServidorCentralHilo extends Thread {

    private final static int HOROSCOPO_PORT = 10001;
    private final static int CLIMA_PORT = 10002;
    private final static String SERVER = "localhost";

    private Socket clientSocket;
    private DataInputStream inBuffer;
    private DataOutputStream outBuffer;
    private int idSession;

    private HashMap<String, String> cacheWeather;
    private HashMap<String, String> cacheHoroscope;

    public ServidorCentralHilo(Socket clientSocket, int idSession,
            HashMap<String, String> cacheWeather, HashMap<String, String> cacheHoroscope) {
        this.clientSocket = clientSocket;
        this.idSession = idSession;
        this.cacheWeather = cacheWeather;
        this.cacheHoroscope = cacheHoroscope;
        //inicializa los buffers para comunicarse con el cliente
        try {
            inBuffer = new DataInputStream(clientSocket.getInputStream());
            outBuffer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        }
    }

    public void desconnectar() {
        //cierra la conexion con cliente
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        }
        System.out.println("Cliente[" + idSession + "]> Se desconecto.");
    }

    @Override
    public void run() {
        try {
            String query = "";
            String answer = "";
            while (!query.equals("salir")) {
                //bloquea hasta recibir una consulta
                query = inBuffer.readUTF();

                System.out.println("Cliente[" + idSession + "]> Consulta: " + query);

                //chequea si es un dato valido para consultar el clima o el horoscopo
                if (isValidDate(query)) {
                    //busca la consulta en la cache.
                    answer = cacheWeather.get(query);
                    //si no tiene dato entonces se lo pregunta al servidor
                    if (answer == null) {
                        System.out.println("Servidor> Consultando a servidor de clima...");
                        answer = askServer(query, CLIMA_PORT);
                        if (answer.equals("ERROR")) {
                            answer = "El servidor de clima no esta disponible, consulte mas tarde.";
                        } else {
                            cacheWeather.put(query, answer);
                        }
                    } else {
                        System.out.println("Servidor> Cache hit.");
                    }
                } else if (isValidSign(query)) {
                    //busca la consulta en la cache.
                    answer = cacheHoroscope.get(query);
                    //si no tiene dato entonces se lo pregunta al servidor
                    if (answer == null) {
                        System.out.println("Servidor> Consultando a servidor de horoscopo...");
                        answer = askServer(query, HOROSCOPO_PORT);
                        if (answer.equals("ERROR")) {
                            answer = "El servidor de horoscopo no esta disponible, consulte mas tarde.";
                        } else {
                            cacheHoroscope.put(query, answer);
                        }
                    } else {
                        System.out.println("Servidor> Cache hit.");
                    }
                } else if (query.equals("salir")) {
                    answer = "Hasta luego.";
                } else {
                    answer = "Consulta no valida.";
                }

                System.out.println("Servidor> Respuesta Cliente[" + idSession + "]: " + answer);

                //envia la respuesta segun lo consultado
                outBuffer.writeUTF(answer);
            }
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
        } finally {
            desconnectar();
        }
    }

    public String askServer(String query, int port) {
        String answer = "";
        try {
            //crea el socket para comunicar con el servidor de horoscopo
            Socket querySocket = new Socket(SERVER, port);

            //inicializa los buffers para comunicarse con el servidor
            DataInputStream inHBuffer;
            DataOutputStream outHBuffer;
            outHBuffer = new DataOutputStream(querySocket.getOutputStream());
            inHBuffer = new DataInputStream(querySocket.getInputStream());

            //envia peticion al servidor
            outHBuffer.writeUTF(query);

            //recibe respuesta del servidor
            answer = inHBuffer.readUTF();

            //cierra el socket con el servidor de horoscopo
            querySocket.close();
        } catch (IOException ex) {
            System.err.println("Servidor> " + ex.getMessage());
            answer = "ERROR";
        } finally {
            return answer;
        }
    }

    public boolean isValidDate(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(text.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public boolean isValidSign(String text) {
        String[] signs = {"aries", "tauro", "geminis", "cancer", "leo", "virgo",
            "libra", "escorpio", "sagitario", "capricornio", "acuario", "piscis"};
        for (int i = 0; i < signs.length; i++) {
            if (text.equals(signs[i])) {
                return true;
            }
        }
        return false;
    }
}
