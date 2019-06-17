package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingFailureResponseMsg extends GraphBuildingCommandResponseMsg
{
    GraphBuildingException exception;

    public GraphBuildingFailureResponseMsg(GraphBuildingException exception, String additionalInfo)
    {
        super(false, additionalInfo);
        this.exception = exception;
    }

    public GraphBuildingFailureResponseMsg(GraphBuildingException exception)
    {
        super(false, "");
        this.exception = exception;
    }
}
