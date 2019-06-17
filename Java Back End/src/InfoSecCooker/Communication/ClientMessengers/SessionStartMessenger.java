package InfoSecCooker.Communication.ClientMessengers;

import InfoSecCooker.Communication.CommunicationStaticData;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class SessionStartMessenger extends ClientMessenger
{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException
    {
        Long sessionId = CommunicationStaticData.startNewSessionAndGetId();
        String response = sessionId.toString();
        sendResponseAsBody(httpExchange, response);
        return;
    }
}
