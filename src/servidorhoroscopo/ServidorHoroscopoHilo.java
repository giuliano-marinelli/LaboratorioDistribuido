package servidorhoroscopo;

import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorHoroscopoHilo extends Thread {

    private Socket clientSocket;
    private DataInputStream inBuffer;
    private DataOutputStream outBuffer;
    private int idSession;

    public ServidorHoroscopoHilo(Socket clientSocket, int idSession) {
        this.clientSocket = clientSocket;
        this.idSession = idSession;
        //inicializa los buffers para comunicarse con el cliente
        try {
            inBuffer = new DataInputStream(clientSocket.getInputStream());
            outBuffer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> " + ex.getMessage());
        }
    }

    public void desconnectar() {
        //cierra la conexion con cliente
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> " + ex.getMessage());
        }
        System.out.println("Cliente[" + idSession + "]> Se desconecto.");
    }

    @Override
    public void run() {
        try {
            String query = "";
            String answer = "";

            //bloquea hasta recibir una consulta
            query = inBuffer.readUTF();

            System.out.println("Cliente[" + idSession + "]> Consulta: " + query);

            answer = giveHoroscope(query);

            System.out.println("Servidor Horoscopo> Respuesta Cliente[" + idSession + "]: " + answer);

            //envia la respuesta segun lo consultado
            outBuffer.writeUTF(answer);

        } catch (IOException ex) {
            System.err.println("Servidor Horoscopo> " + ex.getMessage());
        } finally {
            desconnectar();
        }
    }

    public String giveHoroscope(String sign) {
        String[] horoscopes = {
            "En este dia, le ganara la desconfianza. Tendra algunas dificultades para manifestar lo que siente y expresar sus pensamientos.",
            "No es un momento para buscar grandes emociones y aventuras. Mejor dediquese a disfrutar al maximo de los pequeños placeres diarios.",
            "No quiera abarcar mas de lo que puede. Solo reconociendo sus limitaciones superara los obstaculos que se le presenten.",
            "Reservese este dia para reunirse con amigos y compartir intereses en comun relacionados al estudio o la elaboracion de proyectos.",
            "Explote al maximo su perfeccionismo, ya que en este dia su energia personal estara puramente dirigida hacia los objetivos especificos.",
            "Sera una jornada propicia para sentarse a medir las consecuencias de sus propias acciones. Evite los exabruptos.",
            "Sepa que no siempre los obstaculos son situaciones negativas. Procure descubrir las opciones que se esconden detras de las dificultades.",
            "Debera ser mas claro a la hora de evaluar las decisiones trascendentales. Sus dudas y confusiones pueden dañar su inteligencia natural.",
            "Intente mantener una actitud optimista. Si logra desarrollar la confianza en lo que la vida le dee, todo saldra como usted lo soño.",
            "Gracias a que las energias se renovaron, tendra la posibilidad de vivir cada momento con la intensidad que desea hace tiempo.",
            "Guiese por las percepciones, ya que su agudeza psicologica estara de su lado. Superara los obstaculos que se le presenten.",
            "La Luna en posicion benefica, hara que aproveche la apertura a mundos desconocidos a los que podra ingresar sin temores."
        };

        long seed = sign.hashCode();
        Random random = new Random(seed);
        return horoscopes[random.nextInt(11)];

        /*String horoscope = "";
        switch (sign) {
            case "aries":
                horoscope = "algo 1";
                break;
            case "tauro":
                horoscope = "algo 2";
                break;
            case "geminis":
                horoscope = "algo 3";
                break;
            case "cancer":
                horoscope = "algo 4";
                break;
            case "leo":
                horoscope = "algo 5";
                break;
            case "virgo":
                horoscope = "algo 6";
                break;
            case "libra":
                horoscope = "algo 7";
                break;
            case "escorpio":
                horoscope = "algo 8";
                break;
            case "sagitario":
                horoscope = "algo 9";
                break;
            case "capricornio":
                horoscope = "algo 10";
                break;
            case "acuario":
                horoscope = "algo 11";
                break;
            case "piscis":
                horoscope = "algo 12";
                break;
        }
        return horoscope;*/
    }

}
