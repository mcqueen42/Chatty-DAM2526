package ChattyUDP;

import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws Exception {

        InetAddress destino = InetAddress.getLocalHost();
        int puerto = 5000;

        DatagramSocket socket = new DatagramSocket();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Host del destino: " + destino.getHostName());
        System.out.println("IP Destino: " + destino.getHostAddress());
        System.out.println("Puerto local del socket: " + socket.getLocalPort());
        System.out.println("Puerto destino: " + puerto);

        byte[] bufferRecibir = new byte[1024];

        Thread recibir = new Thread(() -> {
            try {
                while (true) {
                    DatagramPacket datagramaRecibido =
                            new DatagramPacket(bufferRecibir, bufferRecibir.length);
                    socket.receive(datagramaRecibido);

                    String mensaje = new String(
                            datagramaRecibido.getData(),
                            0,
                            datagramaRecibido.getLength()
                    );
                    System.out.println(mensaje);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        recibir.start();

        while (true) {
            String texto = scanner.nextLine();
            String mensaje = nombre + ": " + texto;

            byte[] bufferEnviar = mensaje.getBytes();

            DatagramPacket datagramaEnviado =
                    new DatagramPacket(
                            bufferEnviar,
                            bufferEnviar.length,
                            destino,
                            puerto
                    );

            socket.send(datagramaEnviado);
        }
    }
}
