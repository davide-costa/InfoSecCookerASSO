const GETMessages = require('./GETMessages').GETMessages;

class GETAvailableTasksList extends GETMessages
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("GETAvailableTasksList");
    }
}

module.exports = {GETAvailableTasksList: GETAvailableTasksList};
