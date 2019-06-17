const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class AddTaskCommandMessage extends GraphBuildingCommandMsg
{
    constructor(taskName, taskTemporaryId)
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("AddTaskCommandMsg");
        this.jsonMessage["taskName"] = taskName;
        this.jsonMessage["taskTemporaryId"] = taskTemporaryId;
    }
}

module.exports = {AddTaskCommandMsg: AddTaskCommandMessage};
