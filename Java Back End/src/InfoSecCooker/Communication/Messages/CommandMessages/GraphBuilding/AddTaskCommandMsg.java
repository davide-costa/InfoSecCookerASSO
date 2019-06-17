package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class AddTaskCommandMsg extends GraphBuildingCommandMsg
{
    protected String taskName;
    protected long taskTemporaryId;

    public AddTaskCommandMsg(String taskName, long taskTemporaryId)
    {
        this.taskName = taskName;
        this.taskTemporaryId = taskTemporaryId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public long getTaskTemporaryId()
    {
        return taskTemporaryId;
    }

}
