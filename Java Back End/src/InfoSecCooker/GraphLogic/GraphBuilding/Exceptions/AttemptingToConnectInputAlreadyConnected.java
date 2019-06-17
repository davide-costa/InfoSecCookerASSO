package InfoSecCooker.GraphLogic.GraphBuilding.Exceptions;

public class AttemptingToConnectInputAlreadyConnected extends GraphBuildingException
{
    long nodeId;
    long inputNumber;

    public AttemptingToConnectInputAlreadyConnected(long nodeId, long inputNumber)
    {
        this.nodeId = nodeId;
        this.inputNumber = inputNumber;
    }

    public long getNodeId()
    {
        return nodeId;
    }

    public long getInputNumber()
    {
        return inputNumber;
    }
}
