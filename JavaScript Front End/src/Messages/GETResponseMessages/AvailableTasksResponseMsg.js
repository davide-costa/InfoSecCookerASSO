const InfoSecGETResponseMessages = require('./InfoSecGETResponseMessages.js').InfoSecGETResponseMessages;

class AvailableTasksResponseMsg extends InfoSecGETResponseMessages
{
    constructor(jsonMessage)
    {
        super();
        this.availableTasksInfoJson = [];
        jsonMessage["availableTasksNames"].forEach((availableTask) =>
        {
            this.availableTasksInfoJson.push(JSON.parse(availableTask));
        });
    }
}

module.exports = {AvailableTasksResponseMsg};
