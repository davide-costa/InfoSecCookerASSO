const BaseMessage = require('../BaseMessage.js').BaseMessage;

class EndSessionCommandMsg extends BaseMessage
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("CommandMessages.EndSessionCommandMsg");
        this.jsonMessage["frontEndData"] = jsonMessage;
    }
}

module.exports = {EndSessionCommandMsg};
