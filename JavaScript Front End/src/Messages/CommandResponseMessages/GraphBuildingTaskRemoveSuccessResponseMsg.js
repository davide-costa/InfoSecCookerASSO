const GraphBuildingSuccessResponseMsg = require('./GraphBuildingSuccessResponseMsg.js').GraphBuildingSuccessResponseMsg;

class GraphBuildingTaskRemoveSuccessResponseMsg extends GraphBuildingSuccessResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeTemporaryId = this.jsonMessage["createdNodeTemporaryId"];
    }
}

module.exports = {GraphBuildingTaskRemoveSuccessResponseMsg};
