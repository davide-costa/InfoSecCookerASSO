const GraphBuildingFailureResponseMsg = require('./GraphBuildingFailureResponseMsg.js').GraphBuildingFailureResponseMsg;

class GraphBuildingTaskRemoveFailureResponseMsg extends GraphBuildingFailureResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeId = this.jsonMessage["createdNodeId"];
        this.createdNodeTemporaryId = this.jsonMessage["createdNodeTemporaryId"];
    }
}

module.exports = {GraphBuildingTaskRemoveFailureResponseMsg};
