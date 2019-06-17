package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingSetTaskIntervalFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    public GraphBuildingSetTaskIntervalFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(exception, additionalInfo);
    }
}
