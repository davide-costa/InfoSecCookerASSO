package InfoSecCooker.RuntimeExceptions;

import InfoSecCooker.GraphNodes.GraphNodeInformation;

public class NullDataReceivedFromGraphNodeAsInput extends InfoSecCookerRuntimeException
{
    GraphNodeInformation nodeThatOuputedTheData;
    GraphNodeInformation nodeThatReceivedTheData;

    public NullDataReceivedFromGraphNodeAsInput(String message, GraphNodeInformation nodeThatOuputedTheData, GraphNodeInformation nodeThatReceivedTheData)
    {
        super(message);
        this.nodeThatOuputedTheData = nodeThatOuputedTheData;
        this.nodeThatReceivedTheData = nodeThatReceivedTheData;
    }

    public GraphNodeInformation getNodeThatOuputedTheData()
    {
        return nodeThatOuputedTheData;
    }

    public GraphNodeInformation getNodeThatReceivedTheData()
    {
        return nodeThatReceivedTheData;
    }
}
