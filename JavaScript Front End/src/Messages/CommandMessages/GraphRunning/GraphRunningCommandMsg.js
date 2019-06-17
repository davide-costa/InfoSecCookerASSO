const BaseMessage = require('../../BaseMessage.js').BaseMessage;

class GraphRunningCommandMsg extends BaseMessage
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("CommandMessages.GraphRunning.")
    }
}

module.exports = {GraphRunningCommandMsg};

