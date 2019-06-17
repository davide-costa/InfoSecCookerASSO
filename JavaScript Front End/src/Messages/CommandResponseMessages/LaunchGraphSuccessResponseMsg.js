const InfoSecCommandResponseMsg = require('./InfoSecCommandResponseMsg.js').InfoSecCommandResponseMsg;

class LaunchGraphSuccessResponseMsg extends InfoSecCommandResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.createdNodeId = this.jsonMessage["createdNodeId"];
        this.createdNodeTemporaryId = this.jsonMessage["createdEdgeTemporaryId"];
    }
}

module.exports = {LaunchGraphSuccessResponseMsg};
