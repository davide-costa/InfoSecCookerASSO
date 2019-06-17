package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class SetNumberOfCyclesOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    Long nodeId;
    Integer numberOfCycles;

    public SetNumberOfCyclesOfTaskCommandMsg(Long nodeId, Integer numberOfCycles)
    {
        this.nodeId = nodeId;
        this.numberOfCycles = numberOfCycles;
    }

    public Long getNodeId()
    {
        return nodeId;
    }

    public Integer getNumberOfCycles()
    {
        return numberOfCycles;
    }
}
