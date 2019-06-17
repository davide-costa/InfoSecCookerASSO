package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class AddSourceTaskCommandMsg extends AddSourceSinkTaskCommandMsg
{
    public AddSourceTaskCommandMsg(String taskName, long taskTemporaryId, String additionalInfo)
    {
        super(taskName, taskTemporaryId, additionalInfo);
    }
}
