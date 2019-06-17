const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class SetTickIntervalOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    constructor(nodeId, tickInterval)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("SetTickIntervalOfTaskCommandMsg");
        this.jsonMessage["nodeId"] = nodeId;
        this.jsonMessage["tickInterval"] = tickInterval;
    }
}

module.exports = {SetTickIntervalOfTaskCommandMsg};
