package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class AddSinkTaskCommandMsg extends AddSourceSinkTaskCommandMsg
{
    public AddSinkTaskCommandMsg(String taskName, long taskTemporaryId, String additionalInfo)
    {
        super(taskName, taskTemporaryId, additionalInfo);
    }
}
