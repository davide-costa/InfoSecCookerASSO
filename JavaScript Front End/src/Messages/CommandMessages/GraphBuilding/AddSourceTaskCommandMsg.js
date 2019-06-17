const AddSourceSinkTaskCommandMessage = require('./AddSourceSinkTaskCommandMsg.js').AddSourceSinkTaskCommandMessage;

class AddSourceTaskCommandMsg extends AddSourceSinkTaskCommandMessage
{
    constructor(taskName, taskTemporaryId, additionalInfo)
    {
        super(taskName, taskTemporaryId, additionalInfo);
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("AddSourceTaskCommandMsg");
    }
}

module.exports = {AddSourceTaskCommandMsg: AddSourceTaskCommandMsg};
