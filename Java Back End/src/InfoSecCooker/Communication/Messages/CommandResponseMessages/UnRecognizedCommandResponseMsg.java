package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class UnRecognizedCommandResponseMsg extends InfoSecCommandResponseMsg
{
    public UnRecognizedCommandResponseMsg(String additionalInfo)
    {
        super(false, additionalInfo);
    }
}
