package InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions;

public class ExpectedEdgeOnNodeOutputButNotFound extends ExpectedEdgeOnNodeButNotFound
{
    public ExpectedEdgeOnNodeOutputButNotFound(String message, long nodeId, int edgeNumber)
    {
        super(message, nodeId, edgeNumber);
    }
}
