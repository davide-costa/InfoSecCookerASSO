package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class GraphBuildingSuccessResponseMsg extends GraphBuildingCommandResponseMsg
{
    public GraphBuildingSuccessResponseMsg(String additionalInfo)
    {
        super(true, additionalInfo);
    }

    public GraphBuildingSuccessResponseMsg()
    {
        super(true, "");
    }
}
