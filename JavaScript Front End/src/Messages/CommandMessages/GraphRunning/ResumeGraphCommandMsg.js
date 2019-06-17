const GraphRunningCommandMsg = require('./GraphRunningCommandMsg.js').GraphRunningCommandMsg;

class ResumeGraphCommandMsg extends GraphRunningCommandMsg
{
    constructor()
    {
        super();
        this.jsonMessage["@type"] = this.jsonMessage["@type"].concat("ResumeGraphCommandMsg")
    }
}

module.exports = {ResumeGraphCommandMsg};
