const GraphRunningCommandMsg = require('./GraphRunningCommandMsg.js').GraphRunningCommandMsg;

class PauseGraphCommandMsg extends GraphRunningCommandMsg
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("PauseGraphCommandMsg")
    }
}

module.exports = {PauseGraphCommandMsg};
