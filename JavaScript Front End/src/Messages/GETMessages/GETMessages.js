const BaseMessage = require('../BaseMessage.js').BaseMessage;

class GETMessages extends BaseMessage
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("GETMessages.");
    }
}

module.exports = {GETMessages: GETMessages};
