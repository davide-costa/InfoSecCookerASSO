package InfoSecCooker.Communication.TCPMessengers;


import java.io.IOException;
import java.net.ServerSocket;

public class TCPServerListenner
{
    private ServerSocket serverSocket;

    public TCPServerListenner(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public TCPServerListenner(int port, int timeoutInSeconds)
    {
        try
        {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeoutInSeconds * 1000);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public TCPMessenger acceptConnection() throws IOException
    {
        return new TCPMessenger(serverSocket.accept());
    }

}
