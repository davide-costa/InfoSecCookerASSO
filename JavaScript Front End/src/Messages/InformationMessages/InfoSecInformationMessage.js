const BaseResponseMessage = require('../BaseResponseMessage.js').BaseResponseMessage;

class InfoSecInformationMessage extends BaseResponseMessage
{
    constructor(jsonMesasge)
    {
        super(jsonMesasge);
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("InfoSecInformationMessage.");
    }
}

module.exports = {InfoSecInformationMessage};
