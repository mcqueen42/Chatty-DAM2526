package ChattyUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws Exception {

        byte[] buffer = new byte[1024];
        DatagramSocket socket = new DatagramSocket(5000);

        System.out.println("Servidor UDP iniciado en el puerto 5000");

        // Guardamos clientes (IP + puerto)
        ArrayList<InetAddress> direcciones = new ArrayList<>();
        ArrayList<Integer> puertos = new ArrayList<>();

        while (true) {

            DatagramPacket datagramaRecibido =
                    new DatagramPacket(buffer, buffer.length);

            socket.receive(datagramaRecibido);

            String mensaje = new String(
                    datagramaRecibido.getData(),
                    0,
                    datagramaRecibido.getLength()
            );

            InetAddress ipCliente = datagramaRecibido.getAddress();
            int puertoCliente = datagramaRecibido.getPort();

            System.out.println("Mensaje recibido: " + mensaje);
            System.out.println("IP origen: " + ipCliente);
            System.out.println("Puerto origen: " + puertoCliente);

            // Guardar cliente si es nuevo
            if (!puertos.contains(puertoCliente)) {
                direcciones.add(ipCliente);
                puertos.add(puertoCliente);
                System.out.println("Nuevo cliente conectado");
            }

            // Reenviar mensaje a todos los clientes
            for (int i = 0; i < direcciones.size(); i++) {
                DatagramPacket datagramaEnviado =
                        new DatagramPacket(
                                mensaje.getBytes(),
                                mensaje.length(),
                                direcciones.get(i),
                                puertos.get(i)
                        );

                socket.send(datagramaEnviado);
            }

            buffer = new byte[1024]; // limpiar buffer
        }
    }
}
