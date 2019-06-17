package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class SetBufferSizeOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    Long nodeId;
    Integer bufferSize;

    public SetBufferSizeOfTaskCommandMsg(Long nodeId, Integer bufferSize)
    {
        this.nodeId = nodeId;
        this.bufferSize = bufferSize;
    }

    public Long getNodeId()
    {
        return nodeId;
    }

    public Integer getBufferSize()
    {
        return bufferSize;
    }
}
