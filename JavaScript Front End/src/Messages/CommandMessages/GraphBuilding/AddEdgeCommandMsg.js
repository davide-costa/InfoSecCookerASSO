const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class AddEdgeCommandMessage extends GraphBuildingCommandMsg
{
    constructor(node1Id, node2Id, outputNumber, inputNumber, edgeTemporaryId)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("AddEdgeCommandMsg");
        this.jsonMessage["node1Id"] = node1Id;
        this.jsonMessage["node2Id"] = node2Id;
        this.jsonMessage["outputNumber"] = outputNumber;
        this.jsonMessage["inputNumber"] = inputNumber;
        this.jsonMessage["edgeTemporaryId"] = edgeTemporaryId;
    }
}

module.exports = {AddEdgeCommandMsg: AddEdgeCommandMessage};

