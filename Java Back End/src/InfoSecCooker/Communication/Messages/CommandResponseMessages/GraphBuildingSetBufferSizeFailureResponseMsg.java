package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingSetBufferSizeFailureResponseMsg extends GraphBuildingFailureResponseMsg
{

    public GraphBuildingSetBufferSizeFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(exception, additionalInfo);
    }
}
