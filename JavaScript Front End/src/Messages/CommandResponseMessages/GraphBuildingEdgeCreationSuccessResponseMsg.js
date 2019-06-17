const GraphBuildingSuccessResponseMsg = require('./GraphBuildingSuccessResponseMsg.js').GraphBuildingSuccessResponseMsg;

class GraphBuildingEdgeCreationSuccessResponseMsg extends GraphBuildingSuccessResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdEdgeId = this.jsonMessage["createdEdgeId"];
        this.createdEdgeTemporaryId = this.jsonMessage["createdEdgeTemporaryId"];
    }
}

module.exports = {GraphBuildingEdgeCreationSuccessResponseMsg};
