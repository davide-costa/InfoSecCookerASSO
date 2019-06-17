package InfoSecCooker.Communication.TCPMessengers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPMessenger
{
    protected Socket socket;

    public TCPMessenger(Socket socket)
    {
        this.socket = socket;
    }

    public String readStringMessage()
    {
        String messageString = "";
        try
        {
            byte[] messageBytes = new byte[1]; // max size
            DataInputStream in = new DataInputStream(socket.getInputStream());
            do
            {
                in.read(messageBytes, 0, 1);
                messageString += new String(messageBytes, StandardCharsets.UTF_8);
                if(messageString.lastIndexOf('}') == messageString.length() - 1)
                    break;
            }
            while (true);

        } catch (IOException e)
        {
            //Socket closed, all data has been read and can be returned
        }

        return messageString;
    }

    public void writeMessage(byte[] messageBytes)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(messageBytes);
            out.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void closeSocket()
    {
        try
        {
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
