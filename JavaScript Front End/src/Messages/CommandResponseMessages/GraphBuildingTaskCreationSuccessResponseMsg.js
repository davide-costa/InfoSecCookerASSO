const InfoSecCommandResponseMsg = require('./InfoSecCommandResponseMsg.js').InfoSecCommandResponseMsg;

class GraphBuildingTaskCreationSuccessResponseMsg extends InfoSecCommandResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeId = this.jsonMessage["createdNodeId"];
        this.createdNodeTemporaryId = this.jsonMessage["createdNodeTemporaryId"];
    }
}

module.exports = {GraphBuildingTaskCreationSuccessResponseMsg};
