package ChattyTCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteHandler implements Runnable {

    private static ArrayList<ClienteHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public ClienteHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            this.nombreCliente = bufferedReader.readLine();

            clientHandlers.add(this);
            mandarMensaje("SERVER: Se ha unido el cliente " + nombreCliente);

        } catch (IOException e){
            cerrarComunicacion();
        }
    }

    @Override
    public void run() {
        String mensajeDesdeCliente;

        try {
            while ((mensajeDesdeCliente = bufferedReader.readLine()) != null){
                mandarMensaje(mensajeDesdeCliente);
            }
        } catch (IOException e){
            cerrarComunicacion();
        }
    }

    private void mandarMensaje(String mensaje){
        for (ClienteHandler clienteHandler : clientHandlers){
            try{
                clienteHandler.bufferedWriter.write(mensaje);
                clienteHandler.bufferedWriter.newLine();
                clienteHandler.bufferedWriter.flush();
            } catch (IOException e){
                cerrarComunicacion();
            }
        }
    }

    private void cerrarComunicacion(){
        clientHandlers.remove(this);
        mandarMensaje("SERVER: Se ha ido el cliente " + nombreCliente);

        try{
            if (socket != null) socket.close();
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
