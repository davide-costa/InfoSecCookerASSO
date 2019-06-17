const AddSourceSinkTaskCommandMessage = require('./AddSourceSinkTaskCommandMsg.js').AddSourceSinkTaskCommandMessage;

class AddSinkTaskCommandMsg extends AddSourceSinkTaskCommandMessage
{
    constructor(taskName, taskTemporaryId, additionalInfo)
    {
        super(taskName, taskTemporaryId, additionalInfo);
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("AddSinkTaskCommandMsg");
    }
}

module.exports = {AddSinkTaskCommandMsg: AddSinkTaskCommandMsg};
