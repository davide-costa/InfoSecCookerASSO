const GraphBuildingFailureResponseMsg = require('./GraphBuildingFailureResponseMsg.js').GraphBuildingFailureResponseMsg;

class GraphBuildingEdgeCreationFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdEdgeTemporaryId = this.jsonMessage["createdNodeTemporaryId"];
    }
}

module.exports = {GraphBuildingEdgeCreationFailureResponseMsg};
