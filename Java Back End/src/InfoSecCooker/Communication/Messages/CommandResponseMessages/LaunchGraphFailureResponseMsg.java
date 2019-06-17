package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class LaunchGraphFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    public LaunchGraphFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(exception, additionalInfo);
    }
}
