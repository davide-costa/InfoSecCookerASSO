package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class AddEdgeCommandMsg extends GraphBuildingCommandMsg
{
    Long node1Id;
    Long node2Id;
    Integer outputNumber;
    Integer inputNumber;
    long edgeTemporaryId;

    public AddEdgeCommandMsg(Long node1Id, Long node2Id, Integer outputNumber, Integer inputNumber, long edgeTemporaryId)
    {
        this.node1Id = node1Id;
        this.node2Id = node2Id;
        this.outputNumber = outputNumber;
        this.inputNumber = inputNumber;
        this.edgeTemporaryId = edgeTemporaryId;
    }

    public Long getNode1Id()
    {
        return node1Id;
    }

    public Long getNode2Id()
    {
        return node2Id;
    }

    public Integer getOutputNumber()
    {
        return outputNumber;
    }

    public Integer getInputNumber()
    {
        return inputNumber;
    }

    public long getEdgeTemporaryId()
    {
        return edgeTemporaryId;
    }

}
