package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingTaskCreationFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    private long createdNodeTemporaryId;

    public GraphBuildingTaskCreationFailureResponseMsg(GraphBuildingException exception, long createdNodeTemporaryId, String additionalInfo)
    {
        super(exception, additionalInfo);
        this.createdNodeTemporaryId = createdNodeTemporaryId;
    }

    public GraphBuildingTaskCreationFailureResponseMsg(GraphBuildingException exception, long createdNodeTemporaryId)
    {
        super(exception, "");
        this.createdNodeTemporaryId = createdNodeTemporaryId;
    }
}
