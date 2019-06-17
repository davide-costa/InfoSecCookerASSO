const BaseMessage = require('../../BaseMessage').BaseMessage;

class GraphBuildingCommandMsg extends BaseMessage
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("CommandMessages.GraphBuilding.")
    }
}

module.exports = {GraphBuildingCommandMsg};
