package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class AddSourceSinkTaskCommandMsg extends AddTaskCommandMsg
{
    private String additionalInfo;

    public AddSourceSinkTaskCommandMsg(String taskName, long taskTemporaryId, String additionalInfo)
    {
        super(taskName, taskTemporaryId);
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }
}
