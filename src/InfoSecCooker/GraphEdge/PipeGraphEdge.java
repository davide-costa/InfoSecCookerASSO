package InfoSecCooker.GraphEdge;

import InfoSecCooker.GraphNodes.TaskGraphNode;
import InfoSecCooker.Outputs.InfoSecData;

/**
 * Since this is a DAG, this pipe only needs to transfer in one direction (djirigido)
 */
public abstract class PipeGraphEdge
{
    private TaskGraphNode source;
    private TaskGraphNode destination;

    public abstract void sendData(InfoSecData infoSecData) throws InterruptedException;

    public abstract InfoSecData receiveData();

    public TaskGraphNode getSource()
    {
        return source;
    }

    public TaskGraphNode getDestination()
    {
        return destination;
    }

    public void setSource(TaskGraphNode source)
    {
        this.source = source;
    }

    public void setDestination(TaskGraphNode destination)
    {
        this.destination = destination;
    }
}
