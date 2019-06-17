package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class GraphBuildingEdgeCreationSuccessResponseMsg extends GraphBuildingSuccessResponseMsg
{
    private long createdEdgeId;
    private long createdEdgeTemporaryId;

    public GraphBuildingEdgeCreationSuccessResponseMsg(long createdEdgeId, long createdEdgeTemporaryId, String additionalInfo)
    {
        super(additionalInfo);
        this.createdEdgeId = createdEdgeId;
        this.createdEdgeTemporaryId = createdEdgeTemporaryId;
    }

    public GraphBuildingEdgeCreationSuccessResponseMsg(long createdEdgeId, long createdEdgeTemporaryId)
    {
        super("");
        this.createdEdgeId = createdEdgeId;
        this.createdEdgeTemporaryId = createdEdgeTemporaryId;
    }
}
