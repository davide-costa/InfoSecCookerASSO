package InfoSecCooker.Communication.ClientMessengers;

import InfoSecCooker.Communication.CommunicationStaticData;
import InfoSecCooker.Communication.Messages.InformationMessages.InfoSecFullReportInformationMessage;
import InfoSecCooker.GraphLogic.GraphController;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphWireShark.IncomingAndOutgoingPacketRegistry;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TickEntry;
import com.cedarsoftware.util.io.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationClientMessenger extends ClientMessenger
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
        if (graphController == null || runTimeConfigurations == null)
        {
            sendResponseAsBody(httpExchange, "Session with id " + sessionId + " doesn't exist");
            return;
        }

        System.out.println("reporting information");
        try
        {
            Thread.sleep(runTimeConfigurations.informationConnectionUpdateIntervalMilliSecs.get());
            List<TickEntry> tickRegistrySinceLastUpdate = null;
            tickRegistrySinceLastUpdate = graphController.getAndClearTickRegistry();
            List<TickEntry> tickRegistrySinceEver = graphController.getTickRegistySinceEver();
            HashMap<Long, String> taskNodeStates = graphController.getTaskNodesStates();
            HashMap<Long, IncomingAndOutgoingPacketRegistry> incomingAndOutgoingPacketRegistryHashMap = graphController.getPacketCaptureRegistryByNode();
            HashMap<Long, Long> currentTickCountOfAllNodes = graphController.getCurrentTickCountOfAllNodes();

            InfoSecFullReportInformationMessage infoSecFullReportInformationMessage =
                    new InfoSecFullReportInformationMessage(tickRegistrySinceLastUpdate,
                            tickRegistrySinceEver, taskNodeStates,
                            incomingAndOutgoingPacketRegistryHashMap,
                            currentTickCountOfAllNodes);

            String responseMsgJson = JsonWriter.objectToJson(infoSecFullReportInformationMessage);
            sendResponseAsBody(httpExchange, responseMsgJson);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

}
