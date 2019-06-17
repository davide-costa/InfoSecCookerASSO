package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class SetTickIntervalOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    Long nodeId;
    Integer tickInterval;

    public SetTickIntervalOfTaskCommandMsg(Long nodeId, Integer tickInterval)
    {
        this.nodeId = nodeId;
        this.tickInterval = tickInterval;
    }

    public Long getNodeId()
    {
        return nodeId;
    }

    public Integer getTickInterval()
    {
        return tickInterval;
    }
}
