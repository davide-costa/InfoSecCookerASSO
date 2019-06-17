package InfoSecCooker.Communication;

import InfoSecCooker.Communication.ClientMessengers.CommandClientMessenger;
import InfoSecCooker.Communication.ClientMessengers.InformationClientMessenger;
import InfoSecCooker.Communication.ClientMessengers.SessionStartMessenger;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NewClientsListener implements Runnable
{
    private static final int listenningPort = 4550;

    @Override
    public void run()
    {
        SessionStartMessenger sessionStartMessenger = new SessionStartMessenger();
        CommandClientMessenger commandClientMessenger = new CommandClientMessenger();
        InformationClientMessenger informationClientMessenger = new InformationClientMessenger();
        HttpServer httpServer = null;
        try
        {
            httpServer = HttpServer.create(new InetSocketAddress(listenningPort), listenningPort);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        httpServer.createContext("/InfoSecCooker/startSession", sessionStartMessenger);
        httpServer.createContext("/InfoSecCooker/executeCommand", commandClientMessenger);
        httpServer.createContext("/InfoSecCooker/getInformation", informationClientMessenger);
        httpServer.setExecutor(null);
        httpServer.start();
    }


}
