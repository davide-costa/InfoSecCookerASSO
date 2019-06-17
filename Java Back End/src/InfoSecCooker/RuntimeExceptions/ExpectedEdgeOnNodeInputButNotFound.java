package InfoSecCooker.RuntimeExceptions;

public class ExpectedEdgeOnNodeInputButNotFound extends ExpectedEdgeOnNodeButNotFound
{
    public ExpectedEdgeOnNodeInputButNotFound(String message, long nodeId, int edgeNumber)
    {
        super(message, nodeId, edgeNumber);
    }
}
