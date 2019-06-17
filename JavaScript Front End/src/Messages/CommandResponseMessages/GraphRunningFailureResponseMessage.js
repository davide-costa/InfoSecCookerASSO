const InfoSecCommandResponseMsg = require('./InfoSecCommandResponseMsg.js').InfoSecCommandResponseMsg;

class GraphRunningFailureResponseMessage extends InfoSecCommandResponseMsg
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.exception = this.jsonMessage["exception"];
    }
}

module.exports = {GraphRunningFailureResponseMessage};
