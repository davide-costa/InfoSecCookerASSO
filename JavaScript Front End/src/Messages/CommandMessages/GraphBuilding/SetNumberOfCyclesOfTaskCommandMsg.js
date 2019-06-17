const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class SetNumberOfCyclesOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    constructor(nodeId, numberOfCycles)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("SetNumberOfCyclesOfTaskCommandMsg");
        this.jsonMessage["nodeId"] = nodeId;
        this.jsonMessage["numberOfCycles"] = numberOfCycles;
    }
}

module.exports = {SetNumberOfCyclesOfTaskCommandMsg};
