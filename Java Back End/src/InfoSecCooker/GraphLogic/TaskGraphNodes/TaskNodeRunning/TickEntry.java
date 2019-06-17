package InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning;

import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

public class TickEntry
{
    GraphNodeInformation graphNodeInformation;
    long tickTimeNanoSecs;
    long tickTimeMilliSecs;

    public TickEntry(GraphNodeInformation graphNodeInformation, long tickTimeNanoSecs, long tickTimeMilliSecs)
    {
        this.graphNodeInformation = graphNodeInformation;
        this.tickTimeNanoSecs = tickTimeNanoSecs;
        this.tickTimeMilliSecs = tickTimeMilliSecs;
    }
}