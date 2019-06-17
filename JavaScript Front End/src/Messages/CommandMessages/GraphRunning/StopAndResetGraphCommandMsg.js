const GraphRunningCommandMsg = require('./GraphRunningCommandMsg.js').GraphRunningCommandMsg;

class StopAndResetGraphCommandMsg extends GraphRunningCommandMsg
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("StopAndResetGraphCommandMsg")
    }
}

module.exports = {StopAndResetGraphCommandMsg};
