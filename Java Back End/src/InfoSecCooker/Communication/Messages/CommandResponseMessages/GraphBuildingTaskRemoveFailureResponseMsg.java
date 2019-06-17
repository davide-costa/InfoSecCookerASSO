package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingTaskRemoveFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    public GraphBuildingTaskRemoveFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(exception, additionalInfo);
    }
}
