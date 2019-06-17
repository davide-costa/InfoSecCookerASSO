package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class GraphRunningSuccessResponseMsg extends InfoSecCommandResponseMsg
{
    public GraphRunningSuccessResponseMsg(String additionalInfo)
    {
        super(true, additionalInfo);
    }
}
