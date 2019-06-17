const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class RemoveTaskCommandMsg extends GraphBuildingCommandMsg
{
    constructor(taskNodeId)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("RemoveTaskCommandMsg");
        this.jsonMessage["taskNodeId"] = taskNodeId;
    }
}

module.exports = {RemoveTaskCommandMsg};
