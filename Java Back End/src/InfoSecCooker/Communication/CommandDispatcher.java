package InfoSecCooker.Communication;

import InfoSecCooker.Communication.Messages.CommandMessages.EndSessionCommandMsg;
import InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding.*;
import InfoSecCooker.Communication.Messages.CommandMessages.GraphRunning.PauseGraphCommandMsg;
import InfoSecCooker.Communication.Messages.CommandMessages.GraphRunning.ResumeGraphCommandMsg;
import InfoSecCooker.Communication.Messages.CommandMessages.GraphRunning.StopAndResetGraphCommandMsg;
import InfoSecCooker.Communication.Messages.CommandMessages.InfoSecCommandMessage;
import InfoSecCooker.Communication.Messages.CommandResponseMessages.*;
import InfoSecCooker.Communication.Messages.GETMessages.GETAvailableTasksList;
import InfoSecCooker.Communication.Messages.GETMessages.GETMessage;
import InfoSecCooker.Communication.Messages.GETResponseMessages.AvailableTasksResponseMsg;
import InfoSecCooker.Communication.Messages.GETResponseMessages.GETResponseMessage;
import InfoSecCooker.Communication.Messages.GETResponseMessages.UnRecognizedGETRequestResponseMsg;
import InfoSecCooker.Communication.Messages.InfoSecRequestMessage;
import InfoSecCooker.Communication.Messages.InfoSecResponseMessage;
import InfoSecCooker.Communication.Messages.UnrecognizedRequestResponseMessage;
import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;
import InfoSecCooker.GraphLogic.GraphController;
import com.cedarsoftware.util.io.JsonWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class receives messages (instances of class InfoSecCommandMessage) and dispatches the controls to the GraphController, calling the appropriate method.
 * It acts as a translator between the communication language and the main class of the GraphLogic package.
 */
public class CommandDispatcher
{
    private GraphController graphController;
    private ArrayList<GraphBuildingCommandMsg> graphBuildingCommandMsgs = new ArrayList<>(); //command pattern

    public CommandDispatcher(long sessionId)
    {
        this.graphController = new GraphController(sessionId);
    }

    public GraphController getGraphController()
    {
        return graphController;
    }

    public InfoSecResponseMessage parseAndDispatch(InfoSecRequestMessage message)
    {
        if (message instanceof InfoSecCommandMessage)
            return parseAndDispatchCommandMessage((InfoSecCommandMessage) message);
        else if (message instanceof GETMessage)
            return parseAndDispatchGETMessage((GETMessage) message);

        return new UnrecognizedRequestResponseMessage();
    }

    public InfoSecCommandResponseMsg parseAndDispatchCommandMessage(InfoSecCommandMessage controlMessage)
    {
        if (controlMessage instanceof GraphBuildingCommandMsg)
            graphBuildingCommandMsgs.add((GraphBuildingCommandMsg) controlMessage);

        if (controlMessage instanceof StartNewGraphCommandMsg)
        {
            graphController.startNewGraph();
            return new StartNewGraphSuccessResponseMsg();
        }
        else if (controlMessage instanceof AddSourceTaskCommandMsg)
        {
            AddSourceTaskCommandMsg msg = (AddSourceTaskCommandMsg) controlMessage;
            try
            {
                long nodeId = graphController.addNewSourceTaskNode(msg.getTaskName(), msg.getAdditionalInfo());
                return new GraphBuildingTaskCreationSuccessResponseMsg(nodeId, msg.getTaskTemporaryId(), msg.getTaskName());
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingFailureResponseMsg(e);
            }
        }
        else if (controlMessage instanceof AddSinkTaskCommandMsg)
        {
            AddSinkTaskCommandMsg msg = (AddSinkTaskCommandMsg) controlMessage;
            try
            {
                long nodeId = graphController.addNewSinkTaskNode(msg.getTaskName(), msg.getAdditionalInfo());
                return new GraphBuildingTaskCreationSuccessResponseMsg(nodeId, msg.getTaskTemporaryId(), msg.getTaskName());
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingTaskCreationFailureResponseMsg(e, msg.getTaskTemporaryId());
            }
        }
        else if (controlMessage instanceof AddTaskCommandMsg)
        {
            AddTaskCommandMsg msg = (AddTaskCommandMsg) controlMessage;
            try
            {
                long nodeId = graphController.addNewTaskNode(msg.getTaskName());
                return new GraphBuildingTaskCreationSuccessResponseMsg(nodeId, msg.getTaskTemporaryId(), msg.getTaskName());
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingFailureResponseMsg(e);
            }
        }
        else if (controlMessage instanceof AddEdgeCommandMsg)
        {
            AddEdgeCommandMsg msg = (AddEdgeCommandMsg) controlMessage;
            try
            {
                long edgeId = graphController.addEdgeFromNode1ToNode2OutputToInput(msg.getNode1Id(), msg.getNode2Id(), msg.getOutputNumber(), msg.getInputNumber());
                return new GraphBuildingEdgeCreationSuccessResponseMsg(edgeId, msg.getEdgeTemporaryId());
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingFailureResponseMsg(e);
            }
        }
        else if (controlMessage instanceof RemoveTaskCommandMsg)
        {
            RemoveTaskCommandMsg msg = (RemoveTaskCommandMsg) controlMessage;
            try
            {
                graphController.removeTaskNode(msg.getTaskNodeId());
                return new GraphBuildingTaskRemoveSuccessResponseMsg();
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingTaskRemoveFailureResponseMsg(e, "");
            }
        }
        else if (controlMessage instanceof RemoveEdgeCommandMsg)
        {
            RemoveEdgeCommandMsg msg = (RemoveEdgeCommandMsg) controlMessage;
            try
            {
                graphController.removeEdge(msg.getEdgeId());
                return new GraphBuildingEdgeRemoveSuccessResponseMsg();
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingEdgeRemoveFailureResponseMsg(e, "");
            }
        }
        else if (controlMessage instanceof SetTickIntervalOfTaskCommandMsg)
        {
            SetTickIntervalOfTaskCommandMsg msg = (SetTickIntervalOfTaskCommandMsg) controlMessage;
            try
            {
                graphController.setTickIntervalOfNode(msg.getNodeId(), msg.getTickInterval());
                return new GraphBuildingSetTaskIntervalSuccessResponseMsg();
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingSetTaskIntervalFailureResponseMsg(e, "");
            }
        }
        else if (controlMessage instanceof SetBufferSizeOfTaskCommandMsg)
        {
            SetBufferSizeOfTaskCommandMsg msg = (SetBufferSizeOfTaskCommandMsg) controlMessage;
            try
            {
                graphController.setBufferSizeOfNode(msg.getNodeId(), msg.getBufferSize());
                return new GraphBuildingSetBufferSizeSuccessResponseMsg();
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingSetBufferSizeFailureResponseMsg(e, "");
            }
        }
        else if (controlMessage instanceof SetNumberOfCyclesOfTaskCommandMsg)
        {
            SetNumberOfCyclesOfTaskCommandMsg msg = (SetNumberOfCyclesOfTaskCommandMsg) controlMessage;
            try
            {
                graphController.setNumberOfCyclesOfNode(msg.getNodeId(), msg.getNumberOfCycles());
                return new GraphBuildingSetBufferSizeSuccessResponseMsg();
            } catch (GraphBuildingException e)
            {
                return new GraphBuildingSetBufferSizeFailureResponseMsg(e, "");
            }
        }
        else if (controlMessage instanceof LaunchGraphCommandMsg)
        {
            LaunchGraphCommandMsg msg = (LaunchGraphCommandMsg) controlMessage;
            if (msg.getRunDespiteErrors())
            {
                boolean thereWereErrors = !graphController.finishGraphBuildingAndLaunchGraphDespiteErrors();
                if (thereWereErrors)
                    return new InfoSecCommandResponseMsg(true, "Graph had errors and was launched still. BRACE FOR IMPACT");
                else
                    return new InfoSecCommandResponseMsg(true, "Graph was launched without errors.");
            }
            boolean thereWereErrors = !graphController.finishGraphBuildingAndLaunchGraph();
            String additionalInfo;
            if (thereWereErrors)
                additionalInfo = "Graph had errors, so it was not launched";
            else
                additionalInfo = "Graph didn't have errors. It was successfully launched";

            return new InfoSecCommandResponseMsg(!thereWereErrors, additionalInfo);
        }
        else if (controlMessage instanceof PauseGraphCommandMsg)
        {
            boolean success = graphController.pauseGraph();
            if(success)
                return new GraphRunningPauseGraphSuccessResponseMsg("");
            else
                return new GraphRunningPauseGraphFailureResponseMsg("");
        }
        else if (controlMessage instanceof ResumeGraphCommandMsg)
        {
            boolean success = graphController.resumeGraph();
            if(success)
                return new GraphRunningResumeGraphSuccessResponseMsg("");
            else
                return new GraphRunningResumeGraphFailureResponseMsg("");
        }
        else if (controlMessage instanceof StopAndResetGraphCommandMsg)
        {
            boolean success = graphController.stopAndReset();
            if(success)
                return new GraphRunningStopAndResetGraphSuccessResponseMsg("");
            else
                return new GraphRunningStopAndResetGraphFailureResponseMsg("");
        }
        else if (controlMessage instanceof EndSessionCommandMsg)
        {
            String frontEndData = ((EndSessionCommandMsg) controlMessage).getFrontEndData();
            boolean result = saveSessionDetails(frontEndData);

            if (result)
                return new InfoSecCommandResponseMsg(true, "Information saved successfully");
            else
                return new InfoSecCommandResponseMsg(true, "Error saving information");
        }

        return new UnRecognizedCommandResponseMsg("");
    }

    private GETResponseMessage parseAndDispatchGETMessage(GETMessage message)
    {
        if (message instanceof GETAvailableTasksList)
        {
            ArrayList<String> availableTaskNames = graphController.getTaskNamesList();
            return new AvailableTasksResponseMsg(availableTaskNames);
        }

        return new UnRecognizedGETRequestResponseMsg();
    }

    private boolean saveSessionDetails(String frontEndData)
    {
        SessionData sessionData = new SessionData(frontEndData, graphBuildingCommandMsgs);
        String sessionDataStr = JsonWriter.objectToJson(sessionData);
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(graphController.getSessionId().toString() + ".isc"));
            writer.write(sessionDataStr);

            writer.close();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
