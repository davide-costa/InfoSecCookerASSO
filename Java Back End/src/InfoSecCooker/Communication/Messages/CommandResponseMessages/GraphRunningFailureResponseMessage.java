package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class GraphRunningFailureResponseMessage extends InfoSecCommandResponseMsg
{
    public GraphRunningFailureResponseMessage(String additionalInfo)
    {
        super(false, additionalInfo);
    }
}
