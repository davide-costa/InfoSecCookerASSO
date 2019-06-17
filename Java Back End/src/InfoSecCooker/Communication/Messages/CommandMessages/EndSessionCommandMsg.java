package InfoSecCooker.Communication.Messages.CommandMessages;

public class EndSessionCommandMsg extends InfoSecCommandMessage
{
    String frontEndData;

    public EndSessionCommandMsg(String frontEndData)
    {
        this.frontEndData = frontEndData;
    }

    public String getFrontEndData()
    {
        return frontEndData;
    }
}
