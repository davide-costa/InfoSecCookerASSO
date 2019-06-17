const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class StartNewGraphCommandMsg extends GraphBuildingCommandMsg
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("StartNewGraphCommandMsg");
    }
}

module.exports = {StartNewGraphCommandMsg};

