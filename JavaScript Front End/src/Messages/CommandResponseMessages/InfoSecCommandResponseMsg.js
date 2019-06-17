const BaseResponseMessage = require('../BaseResponseMessage.js').BaseResponseMessage;

class InfoSecCommandResponseMsg extends BaseResponseMessage
{
    constructor(jsonMessage)
    {
        super(jsonMessage);
        this.result = this.jsonMessage["result"];

        if(!this.jsonMessage["additionalInfo"])
            this.jsonMessage["additionalInfo"] = "";
        this.additionalInfo = this.jsonMessage["additionalInfo"];
    }
}

module.exports = {InfoSecCommandResponseMsg};
