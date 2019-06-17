const GraphBuildingFailureResponseMsg = require('./GraphBuildingFailureResponseMsg.js').GraphBuildingFailureResponseMsg;

class GraphBuildingTaskCreationFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeTemporaryId = this.jsonMessage["createdEdgeTemporaryId"];
    }
}

module.exports = {GraphBuildingTaskCreationFailureResponseMsg};
