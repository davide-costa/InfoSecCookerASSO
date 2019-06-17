package InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions;

import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

public class ComputationException extends InfoSecCookerRuntimeException
{
    GraphNodeInformation nodeThatOuputedTheData;
    GraphNodeInformation nodeThatReceivedTheData;

    public ComputationException(String message)
    {
        super(message);
    }
}
