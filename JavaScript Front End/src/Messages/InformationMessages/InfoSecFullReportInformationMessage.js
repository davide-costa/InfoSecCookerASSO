const InfoSecInformationMessage = require('./InfoSecInformationMessage.js').InfoSecInformationMessage;

class InfoSecFullReportInformationMessage extends InfoSecInformationMessage
{
    constructor(jsonMesasge)
    {
        super(jsonMesasge);
        this.tickRegistrySinceLastUpdate = this.jsonMessage["tickRegistrySinceLastUpdate"];
        this.tickRegistrySinceEver = this.jsonMessage["tickRegistrySinceEver"];
        this.taskNodeStates = this.jsonMessage["taskNodeStates"];
        this.taskNodesIncomingAndOutgoingPacketRegistry = this.jsonMessage["taskNodesIncomingAndOutgoingPacketRegistry"];
    }
}

module.exports = {InfoSecFullReportInformationMessage};
