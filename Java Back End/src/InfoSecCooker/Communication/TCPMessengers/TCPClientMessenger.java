package InfoSecCooker.Communication.TCPMessengers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientMessenger extends TCPMessenger
{

    public TCPClientMessenger(InetAddress address, int port) throws IOException
    {
        super(new Socket(address, port));
    }

    public TCPClientMessenger(String address, int port) throws IOException
    {
        this(InetAddress.getByName(address), port);
    }
}
