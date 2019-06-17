const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class RemoveEdgeCommandMsg extends GraphBuildingCommandMsg
{
    constructor(edgeId)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("RemoveEdgeCommandMsg");
        this.jsonMessage["edgeId"] = edgeId;
    }
}

module.exports = {RemoveEdgeCommandMsg};
