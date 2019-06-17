class BaseMessage
{
    constructor()
    {
        this.jsonMessage = {"@type": "InfoSecCooker.Communication.Messages."};
    }

    toString()
    {
        return JSON.stringify(this.jsonMessage);
    }
}

module.exports = {BaseMessage};
