const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class LaunchGraphCommandMsg extends GraphBuildingCommandMsg
{
    constructor(runDespiteErrors)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("LaunchGraphCommandMsg");
        this.jsonMessage["runDespiteErrors"] = runDespiteErrors;
    }
}

module.exports = {LaunchGraphCommandMsg};

