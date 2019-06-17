package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingEdgeRemoveFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    public GraphBuildingEdgeRemoveFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(exception, additionalInfo);
    }
}
