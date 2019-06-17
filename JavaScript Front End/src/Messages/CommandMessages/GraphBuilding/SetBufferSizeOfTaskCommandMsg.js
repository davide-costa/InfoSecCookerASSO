const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class SetBufferSizeOfTaskCommandMsg extends GraphBuildingCommandMsg
{
    constructor(nodeId, bufferSize)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("SetBufferSizeOfTaskCommandMsg");
        this.jsonMessage["nodeId"] = nodeId;
        this.jsonMessage["bufferSize"] = bufferSize;
    }
}

module.exports = {SetBufferSizeOfTaskCommandMsg};

