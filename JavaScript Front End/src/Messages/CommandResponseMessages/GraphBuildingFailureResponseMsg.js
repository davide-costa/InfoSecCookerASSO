const InfoSecCommandResponseMsg = require('./InfoSecCommandResponseMsg.js').InfoSecCommandResponseMsg;

class GraphBuildingFailureResponseMsg extends InfoSecCommandResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.exception = this.jsonMessage["exception"];
    }
}

module.exports = {GraphBuildingFailureResponseMsg};
