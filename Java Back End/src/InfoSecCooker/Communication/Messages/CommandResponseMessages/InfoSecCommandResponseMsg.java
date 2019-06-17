package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.Communication.Messages.InfoSecResponseMessage;

public class InfoSecCommandResponseMsg extends InfoSecResponseMessage
{
    boolean result;
    String additionalInfo;

    public InfoSecCommandResponseMsg(boolean result, String additionalInfo)
    {
        this.result = result;
        this.additionalInfo = additionalInfo;
    }
}
