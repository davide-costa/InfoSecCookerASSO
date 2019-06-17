const GraphBuildingCommandMsg = require('./GraphBuildingCommandMsg.js').GraphBuildingCommandMsg;

class AddSourceSinkTaskCommandMsg extends GraphBuildingCommandMsg
{
    constructor(taskName, taskTemporaryId, additionalInfo)
    {
        super();
        this.jsonMessage["taskName"] = taskName;
        this.jsonMessage["taskTemporaryId"] = taskTemporaryId;
        this.jsonMessage["additionalInfo"] = additionalInfo;
    }
}

module.exports = {AddSourceSinkTaskCommandMessage: AddSourceSinkTaskCommandMsg};
