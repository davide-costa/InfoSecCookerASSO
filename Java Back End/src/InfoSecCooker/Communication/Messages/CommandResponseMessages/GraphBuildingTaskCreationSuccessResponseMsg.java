package InfoSecCooker.Communication.Messages.CommandResponseMessages;

public class GraphBuildingTaskCreationSuccessResponseMsg extends GraphBuildingSuccessResponseMsg
{
    private long createdNodeId;
    private long createdNodeTemporaryId;

    public GraphBuildingTaskCreationSuccessResponseMsg(long createdNodeId, long createdNodeTemporaryId, String additionalInfo)
    {
        super(additionalInfo);
        this.createdNodeId = createdNodeId;
        this.createdNodeTemporaryId = createdNodeTemporaryId;
    }

    public GraphBuildingTaskCreationSuccessResponseMsg(long createdNodeId, long createdNodeTemporaryId)
    {
        super("");
        this.createdNodeId = createdNodeId;
        this.createdNodeTemporaryId = createdNodeTemporaryId;
    }
}
