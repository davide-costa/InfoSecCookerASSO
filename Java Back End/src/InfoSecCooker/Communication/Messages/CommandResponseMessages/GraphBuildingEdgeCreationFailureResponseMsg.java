package InfoSecCooker.Communication.Messages.CommandResponseMessages;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;

public class GraphBuildingEdgeCreationFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    private long createdEdgeId;
    private long createdEdgeTemporaryId;

    public GraphBuildingEdgeCreationFailureResponseMsg(long createdEdgeId, GraphBuildingException exception, long createdEdgeTemporaryId, String additionalInfo)
    {
        super(exception, additionalInfo);
        this.createdEdgeId = createdEdgeId;
        this.createdEdgeTemporaryId = createdEdgeTemporaryId;
    }

    public GraphBuildingEdgeCreationFailureResponseMsg(long createdEdgeId, GraphBuildingException exception, long createdEdgeTemporaryId)
    {
        super(exception, "");
        this.createdEdgeId = createdEdgeId;
        this.createdEdgeTemporaryId = createdEdgeTemporaryId;
    }
}
