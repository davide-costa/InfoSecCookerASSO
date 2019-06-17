package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class RemoveEdgeCommandMsg extends GraphBuildingCommandMsg
{
    Long edgeId;

    public RemoveEdgeCommandMsg(Long edgeId)
    {
        this.edgeId = edgeId;
    }

    public Long getEdgeId()
    {
        return edgeId;
    }
}
