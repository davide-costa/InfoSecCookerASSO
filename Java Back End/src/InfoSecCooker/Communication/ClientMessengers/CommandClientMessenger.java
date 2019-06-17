package InfoSecCooker.Communication.ClientMessengers;

import InfoSecCooker.Communication.CommandDispatcher;
import InfoSecCooker.Communication.CommunicationStaticData;
import InfoSecCooker.Communication.Messages.CommandResponseMessages.UnRecognizedCommandResponseMsg;
import InfoSecCooker.Communication.Messages.InfoSecRequestMessage;
import InfoSecCooker.Communication.Messages.InfoSecResponseMessage;
import InfoSecCooker.GraphLogic.GraphController;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class CommandClientMessenger extends ClientMessenger
{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException
    {
        Map<String, Object> parameters = getParameters(httpExchange);
        String sessionIdStr = (String) parameters.get("sessionId");
        Long sessionId = Long.valueOf(sessionIdStr);
        if (sessionId == null)
        {
            sendResponseAsBody(httpExchange, "Received bad session id on InformationClientMessenger. The request will not be satisfied. Received " + sessionIdStr);
            return;
        }

        GraphController graphController = CommunicationStaticData.getGraphControllerOfSessionId(sessionId);
        RunTimeConfigurations runTimeConfigurations = CommunicationStaticData.getRunTimeConfigurationsOfSessionId(sessionId);
        CommandDispatcher commandDispatcher = CommunicationStaticData.getCommandDispatcherOfSessionId(sessionId);
        if (graphController == null || runTimeConfigurations == null || commandDispatcher == null)
        {
            sendResponseAsBody(httpExchange, "Session with id " + sessionId + " doesn't exist");
            return;
        }

        String receivedMsg = (String) parameters.get("message");


            InfoSecRequestMessage infoSecRequestMessage;
            try
            {
                infoSecRequestMessage = (InfoSecRequestMessage) JsonReader.jsonToJava(receivedMsg);
            }
            catch (Exception e)
            {
                 return;
            }

            System.out.println("received JSON message");
            System.out.println(infoSecRequestMessage);
            InfoSecResponseMessage responseMsg;

            if (infoSecRequestMessage == null)
                responseMsg = new UnRecognizedCommandResponseMsg("Message got was: " + infoSecRequestMessage);
            else
                responseMsg = commandDispatcher.parseAndDispatch(infoSecRequestMessage);

            String responseMsgJson = JsonWriter.objectToJson(responseMsg);
            sendResponseAsBody(httpExchange, responseMsgJson);
    }
}
