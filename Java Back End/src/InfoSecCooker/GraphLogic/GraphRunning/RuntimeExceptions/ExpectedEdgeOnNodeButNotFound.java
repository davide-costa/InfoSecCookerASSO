package InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions;

public class ExpectedEdgeOnNodeButNotFound extends InfoSecCookerRuntimeException
{
    private long nodeId;
    private int edgeNumber;

    public ExpectedEdgeOnNodeButNotFound(String message, long nodeId, int edgeNumber)
    {
        super(message);
        this.nodeId = nodeId;
        this.edgeNumber = edgeNumber;
    }
}
