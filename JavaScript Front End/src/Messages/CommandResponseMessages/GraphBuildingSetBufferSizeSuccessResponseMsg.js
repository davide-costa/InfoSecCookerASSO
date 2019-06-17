const GraphBuildingSuccessResponseMsg = require('./GraphBuildingSuccessResponseMsg.js').GraphBuildingSuccessResponseMsg;

class GraphBuildingEdgeCreationSuccessResponseMsg extends GraphBuildingSuccessResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeId = this.jsonMessage["createdNodeId"];
        this.createdEdgeTemporaryId = this.jsonMessage["createdNodeTemporaryId"];
    }
}

module.exports = {GraphBuildingEdgeCreationSuccessResponseMsg};
