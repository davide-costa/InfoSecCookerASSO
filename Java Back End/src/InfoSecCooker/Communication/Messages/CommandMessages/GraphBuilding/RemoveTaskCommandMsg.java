package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class RemoveTaskCommandMsg extends GraphBuildingCommandMsg
{
    Long taskNodeId;

    public RemoveTaskCommandMsg(Long taskNodeId)
    {
        this.taskNodeId = taskNodeId;
    }

    public Long getTaskNodeId()
    {
        return taskNodeId;
    }
}
