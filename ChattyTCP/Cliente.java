package ChattyTCP;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public Cliente(Socket socket, String nombreCliente){
        try{
            this.socket = socket;
            this.nombreCliente = nombreCliente;
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            cerrarComunicacion();
        }
    }

    private void cerrarComunicacion(){
        try{
            if (socket != null) socket.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void enviarMensaje(){
        try{
            // Enviar nombre al servidor
            bufferedWriter.write(nombreCliente);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()){
                String mensaje = scanner.nextLine();
                bufferedWriter.write(nombreCliente + ": " + mensaje);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e){
            cerrarComunicacion();
        }
    }

    public void escucharMensaje(){
        new Thread(() -> {
            String mensaje;
            try {
                while ((mensaje = bufferedReader.readLine()) != null){
                    System.out.println(mensaje);
                }
            } catch (IOException e){
                cerrarComunicacion();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombreCliente = scanner.nextLine();

        Socket socket = new Socket("localhost", 5000);

        Cliente cliente = new Cliente(socket, nombreCliente);
        cliente.escucharMensaje();
        cliente.enviarMensaje();
    }
}
