package InfoSecCooker.GraphLogic.GraphBuilding.Exceptions;

public class AttemptingToConnectOutputAlreadyConnected extends GraphBuildingException
{
    long nodeId;
    long outputNumber;

    public AttemptingToConnectOutputAlreadyConnected(long nodeId, long outputNumber)
    {
        this.nodeId = nodeId;
        this.outputNumber = outputNumber;
    }

    public long getNodeId()
    {
        return nodeId;
    }

    public long getOutputNumber()
    {
        return outputNumber;
    }
}
