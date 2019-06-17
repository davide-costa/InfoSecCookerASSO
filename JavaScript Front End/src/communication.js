/*
In the node.js intro tutorial (http://nodejs.org/), they show a basic tcp
server, but for some reason omit a client connecting to it.  I added an
example at the bottom.
Save the following server in example.js:
*/

/*
var net = require('net');

var server = net.createServer(function (socket) {
    socket.write('Echo server\r\n');
    socket.pipe(socket);
    socket.on("data", function (data) {
        console.log(socket.bytesRead);
        console.log(data)
    })
});

server.listen(1337, '127.0.0.1');

server.addListener("connection", function () {

    console.log("connection")

});
*/
/*
And connect with a tcp client from the command line using netcat, the *nix
utility for reading and writing across tcp/udp network connections.  I've only
used it for debugging myself.
$ netcat 127.0.0.1 1337
You should see:
> Echo server
*/

/* Or use this example tcp client written in node.js.  (Originated with
example code from
http://www.hacksparrow.com/tcp-socket-programming-in-node-js.html.) */





const GraphBuildingEdgeCreationFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingEdgeCreationFailureResponseMsg").GraphBuildingEdgeCreationFailureResponseMsg;
const GraphRunningStopAndResetGraphFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningStopAndResetGraphFailureResponseMsg").GraphRunningStopAndResetGraphFailureResponseMsg;
const GraphBuildingEdgeRemoveFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingEdgeRemoveFailureResponseMsg").GraphBuildingEdgeRemoveFailureResponseMsg;
const GraphBuildingEdgeRemoveSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingEdgeRemoveSuccessResponseMsg").GraphBuildingEdgeRemoveSuccessResponseMsg;
const GraphBuildingSetBufferSizeFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingSetBufferSizeFailureResponseMsg").GraphBuildingSetBufferSizeFailureResponseMsg;
const GraphBuildingSetBufferSizeSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingSetBufferSizeSuccessResponseMsg").GraphBuildingEdgeCreationSuccessResponseMsg;
const GraphBuildingSetTaskIntervalSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingSetTaskIntervalSuccessResponseMsg").GraphBuildingSetTaskIntervalSuccessResponseMsg;
const GraphBuildingSetTaskIntervalFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingSetTaskIntervalFailureResponseMsg").GraphBuildingSetTaskIntervalFailureResponseMsg;
const GraphBuildingTaskCreationFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingTaskCreationFailureResponseMsg").GraphBuildingTaskCreationFailureResponseMsg;
const GraphBuildingTaskRemoveFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingTaskRemoveFailureResponseMsg").GraphBuildingTaskRemoveFailureResponseMsg;
const GraphBuildingTaskRemoveSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingTaskRemoveSuccessResponseMsg").GraphBuildingTaskRemoveSuccessResponseMsg;
const GraphRunningPauseGraphSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningPauseGraphSuccessResponseMsg").GraphRunningPauseGraphSuccessResponseMsg;
const StartNewGraphSuccessResponseMsg = require("./Messages/CommandResponseMessages/StartNewGraphSuccessResponseMsg").StartNewGraphSuccessResponseMsg;
const GraphRunningPauseGraphFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningPauseGraphFailureResponseMsg").GraphRunningPauseGraphFailureResponseMsg;
const GraphRunningResumeGraphFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningResumeGraphFailureResponseMsg").GraphRunningResumeGraphFailureResponseMsg;
const GraphRunningResumeGraphSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningResumeGraphSuccessResponseMsg").GraphRunningResumeGraphSuccessResponseMsg;
const GraphRunningStopAndResetGraphSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphRunningStopAndResetGraphSuccessResponseMsg").GraphRunningStopAndResetGraphSuccessResponseMsg;
const LaunchGraphFailureResponseMsg = require("./Messages/CommandResponseMessages/StartNewGraphSuccessResponseMsg").StartNewGraphSuccessResponseMsg;
const LaunchGraphSuccessResponseMsg = require("./Messages/CommandResponseMessages/LaunchGraphSuccessResponseMsg").LaunchGraphSuccessResponseMsg;
const StartNewGraphFailureResponseMsg = require("./Messages/CommandResponseMessages/StartNewGraphFailureResponseMsg").StartNewGraphFailureResponseMsg;

const InfoSecFullReportInformationMessage = require("./Messages/InformationMessages/InfoSecFullReportInformationMessage").InfoSecFullReportInformationMessage;

const  LaunchGraphCommandMsg = require("./Messages/CommandMessages/GraphBuilding/LaunchGraphCommandMsg").LaunchGraphCommandMsg;

const StartNewGraphCommandMsg = require("./Messages/CommandMessages/GraphBuilding/StartNewGraphCommandMsg").StartNewGraphCommandMsg;
const AddEdgeCommandMsg = require("./Messages/CommandMessages/GraphBuilding/AddEdgeCommandMsg").AddEdgeCommandMsg;
const AddTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/AddTaskCommandMsg").AddTaskCommandMsg;
const AddSourceTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/AddSourceTaskCommandMsg").AddSourceTaskCommandMsg;
const AddSinkTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/AddSinkTaskCommandMsg").AddSinkTaskCommandMsg;
const RemoveTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/RemoveTaskCommandMsg").RemoveTaskCommandMsg;
const SetTickIntervalOfTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/SetTickIntervalOfTaskCommandMsg").SetTickIntervalOfTaskCommandMsg;
const SetBufferSizeOfTaskCommandMsg = require("./Messages/CommandMessages/GraphBuilding/SetBufferSizeOfTaskCommandMsg").SetBufferSizeOfTaskCommandMsg;
const StopAndResetGraphCommandMsg = require("./Messages/CommandMessages/GraphRunning/StopAndResetGraphCommandMsg").StopAndResetGraphCommandMsg;
const PauseGraphCommandMsg = require("./Messages/CommandMessages/GraphRunning/PauseGraphCommandMsg").PauseGraphCommandMsg;
const ResumeGraphCommandMsg = require("./Messages/CommandMessages/GraphRunning/ResumeGraphCommandMsg").ResumeGraphCommandMsg;

const GETAvailableTasksList = require("./Messages/GETMessages/GETAvailableTasksList").GETAvailableTasksList;


const AvailableTasksResponseMsg = require("./Messages/GETResponseMessages/AvailableTasksResponseMsg").AvailableTasksResponseMsg;
const UnRecognizedGETRequestResponseMsg = require("./Messages/GETResponseMessages/UnRecognizedGETRequestResponseMsg").UnRecognizedGETRequestResponseMsg;
const InfoSecCommandResponseMsg = require("./Messages/CommandResponseMessages/InfoSecCommandResponseMsg").InfoSecCommandResponseMsg;
const GraphBuildingCommandResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingCommandResponseMsg").GraphBuildingCommandResponseMsg;
const GraphBuildingFailureResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingFailureResponseMsg").GraphBuildingFailureResponseMsg;
const GraphBuildingSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingSuccessResponseMsg").GraphBuildingSuccessResponseMsg;
const GraphBuildingTaskCreationSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingTaskCreationSuccessResponseMsg").GraphBuildingTaskCreationSuccessResponseMsg;
const GraphBuildingEdgeCreationSuccessResponseMsg = require("./Messages/CommandResponseMessages/GraphBuildingEdgeCreationSuccessResponseMsg").GraphBuildingEdgeCreationSuccessResponseMsg;
const GraphRunningCommandResponseMessage = require("./Messages/CommandResponseMessages/GraphRunningCommandResponseMessage").GraphRunningCommandResponseMessage;
const UnRecognizedCommandResponseMsg = require("./Messages/CommandResponseMessages/UnRecognizedCommandResponseMsg").UnRecognizedCommandResponseMsg;
const UnRecognizedRequestResponseMsg = require("./Messages/UnRecognizedRequestResponseMsg").UnRecognizedRequestResponseMsg;

/*
import {GraphBuildingCommandResponseMsg} from "./Messages/CommandResponseMessages/GraphBuildingCommandResponseMsg";
import {GraphBuildingFailureResponseMsg} from "./Messages/CommandResponseMessages/GraphBuildingFailureResponseMsg";
import {GraphBuildingSuccessResponseMsg} from "./Messages/CommandResponseMessages/GraphBuildingSuccessResponseMsg";
import {GraphBuildingTaskCreationSuccessResponseMsg} from "./Messages/CommandResponseMessages/GraphBuildingTaskCreationSuccessResponseMsg";
import {GraphBuildingEdgeCreationSuccessResponseMsg} from "./Messages/CommandResponseMessages/GraphBuildingEdgeCreationSuccessResponseMsg";
import {GraphRunningCommandResponseMessage} from "./Messages/CommandResponseMessages/GraphRunningCommandResponseMessage";
import {InfoSecCommandResponseMsg} from "./Messages/CommandResponseMessages/InfoSecCommandResponseMsg";
import {UnRecognizedCommandResponseMsg} from "./Messages/CommandResponseMessages/UnRecognizedCommandResponseMsg";
import {AvailableTasksResponseMsg} from "./Messages/GETResponseMessages/AvailableTasksResponseMsg";
import {UnRecognizedGETRequestResponseMsg} from "./Messages/GETResponseMessages/UnRecognizedGETRequestResponseMsg";
import {InfoSecFullReportInformationMessage} from "./Messages/InformationMessages/InfoSecFullReportInformationMessage";
import {GETAvailableTasksList} from "./Messages/GETMessages/GETAvailableTasksList";
import {StartNewGraphCommandMsg} from "./Messages/CommandMessages/GraphBuilding/StartNewGraphCommandMsg";
import {AddSourceTaskCommandMsg} from "./Messages/CommandMessages/GraphBuilding/AddSourceTaskCommandMsg";
import {AddTaskCommandMsg} from "./Messages/CommandMessages/GraphBuilding/AddTaskCommandMsg";
import {AddEdgeCommandMsg} from "./Messages/CommandMessages/GraphBuilding/AddEdgeCommandMsg";
import {LaunchGraphCommandMsg} from "./Messages/CommandMessages/GraphBuilding/LaunchGraphCommandMsg";
*/
class GraphCommunication
{
    constructor(address, port, gui)
    {
        this.baseURL = "http://" + address + ":" + port + "/InfoSecCooker/";
        this.sendStartSessionRequest();
        this.gui = gui;
        console.log(gui);
    }

    handleNewCommandMessageResponse(newMessage)
    {
        console.log("handleNewCommandMessageResponse");
        console.log(newMessage);
        console.log("\n\n\n");


        let jsonResponseType = newMessage['@type'];
        if(!jsonResponseType)
            return;

        switch (jsonResponseType)
        {
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingEdgeCreationFailureResponseMsg":
                let graphBuildingEdgeCreationFailureResponseMsg = new GraphBuildingEdgeCreationFailureResponseMsg(newMessage);
                alert(graphBuildingEdgeCreationFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingEdgeCreationSuccessResponseMsg":
                let graphBuildingEdgeCreationSuccessResponseMsg = new GraphBuildingEdgeCreationSuccessResponseMsg(newMessage);
                let createdEdgeTemporaryId = graphBuildingEdgeCreationSuccessResponseMsg.createdEdgeTemporaryId;
                let createdEdgeId = graphBuildingEdgeCreationSuccessResponseMsg.createdEdgeId;
                this.gui.updateEdgeIdBasedOnBackendResponse(createdEdgeTemporaryId, createdEdgeId);
                this.gui.graphData.updateEdgeIdBasedOnBackendResponse(createdEdgeTemporaryId, createdEdgeId);
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingEdgeRemoveFailureResponseMsg":
                let graphBuildingEdgeRemoveFailureResponseMsg = new GraphBuildingEdgeRemoveFailureResponseMsg(newMessage);
                alert(graphBuildingEdgeRemoveFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingEdgeRemoveSuccessResponseMsg":
                let graphBuildingEdgeRemoveSuccessResponseMsg = new GraphBuildingEdgeRemoveSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingSetBufferSizeFailureResponseMsg":
                let graphBuildingSetBufferSizeFailureResponseMsg = new GraphBuildingSetBufferSizeFailureResponseMsg(newMessage);
                alert(graphBuildingSetBufferSizeFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingSetBufferSizeSuccessResponseMsg":
                let graphBuildingSetBufferSizeSuccessResponseMsg = new GraphBuildingSetBufferSizeSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingSetTaskIntervalFailureResponseMsg":
                let graphBuildingSetTaskIntervalFailureResponseMsg = new GraphBuildingSetTaskIntervalFailureResponseMsg(newMessage);
                alert(graphBuildingSetTaskIntervalFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingSetTaskIntervalSuccessResponseMsg":
                let graphBuildingSetTaskIntervalSuccessResponseMsg = new GraphBuildingSetTaskIntervalSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingTaskCreationFailureResponseMsg":
                let graphBuildingTaskCreationFailureResponseMsg = new GraphBuildingTaskCreationFailureResponseMsg(newMessage);
                alert(graphBuildingTaskCreationFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingTaskCreationSuccessResponseMsg":
                let graphBuildingTaskCreationSuccessResponseMsg = new GraphBuildingTaskCreationSuccessResponseMsg(newMessage);
                let createdNodeTemporaryId = graphBuildingTaskCreationSuccessResponseMsg.createdNodeTemporaryId;
                let createdNodeId = graphBuildingTaskCreationSuccessResponseMsg.createdNodeId;
                this.gui.updateNodeIdBasedOnBackendResponse(createdNodeTemporaryId, createdNodeId);
                this.gui.graphData.updateNodeIdBasedOnBackendResponse(createdNodeTemporaryId, createdNodeId);
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingTaskRemoveFailureResponseMsg":
                let graphBuildingTaskRemoveFailureResponseMsg = new GraphBuildingTaskRemoveFailureResponseMsg(newMessage);
                alert(graphBuildingTaskRemoveFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphBuildingTaskRemoveSuccessResponseMsg":
                let GraphBuildingTaskRemoveSuccessResponseMsg = new GraphBuildingTaskRemoveSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningPauseGraphFailureResponseMsg":
                let graphRunningPauseGraphFailureResponseMsg = new GraphRunningPauseGraphFailureResponseMsg(newMessage);
                alert(graphRunningPauseGraphFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningPauseGraphSuccessResponseMsg":
                let graphRunningPauseGraphSuccessResponseMsg = new GraphRunningPauseGraphSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningResumeGraphFailureResponseMsg":
                let graphRunningResumeGraphFailureResponseMessage = new GraphRunningResumeGraphFailureResponseMsg(newMessage);
                alert(graphRunningResumeGraphFailureResponseMessage.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningResumeGraphSuccessResponseMsg":
                let graphRunningResumeGraphSuccessResponseMsg = new GraphRunningResumeGraphSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningStopAndResetGraphFailureResponseMsg":
                let graphRunningStopAndResetGraphFailureResponseMsg = new GraphRunningStopAndResetGraphFailureResponseMsg(newMessage);
                alert(graphRunningStopAndResetGraphFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.GraphRunningStopAndResetGraphSuccessResponseMsg":
                let graphRunningStopAndResetGraphSuccessResponseMsg = new GraphRunningStopAndResetGraphSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.LaunchGraphFailureResponseMsg":
                let launchGraphFailureResponseMsg = new LaunchGraphFailureResponseMsg(newMessage);
                alert(launchGraphFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.LaunchGraphSuccessResponseMsg":
                let launchGraphSuccessResponseMsg = new LaunchGraphSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.StartNewGraphFailureResponseMsg":
                let startNewGraphFailureResponseMsg = new StartNewGraphFailureResponseMsg(newMessage);
                alert(startNewGraphFailureResponseMsg.exception);
                break;
            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.StartNewGraphSuccessResponseMsg":
                let gtartNewGraphSuccessResponseMsg = new StartNewGraphSuccessResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;

            case "InfoSecCooker.Communication.Messages.CommandResponseMessages.UnRecognizedCommandResponseMsg":
                let unRecognizedCommandResponseMsg = new UnRecognizedCommandResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;
            case "InfoSecCooker.Communication.Messages.GETResponseMessages.AvailableTasksResponseMsg":
                let availableTasksResponseMsg = new AvailableTasksResponseMsg(newMessage);
                this.gui.addNodesToNodesList(availableTasksResponseMsg.availableTasksInfoJson);
                break;
            case "InfoSecCooker.Communication.Messages.GETResponseMessages.UnRecognizedGETRequestResponseMsg":
                let unRecognizedGETRequestResponseMsg = new UnRecognizedGETRequestResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;
            case "InfoSecCooker.Communication.Messages.UnrecognizedRequestResponseMessage":
                let unRecognizedRequestResponseMsg = new UnRecognizedRequestResponseMsg(newMessage);
                //TODO STUFF na GUI
                break;
            default:
                break;
        }
    }

    sendMessage(url, message, handler)
    {
        let XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function()
        {
            if (this.readyState == 4 && this.status == 200)
            {
                handler(JSON.parse(this.responseText))
            }
        };
        xhttp.open("POST", url, true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("sessionId=" + this.sessionId + "&message=" + message);
    }

    sendStartSessionRequest()
    {
        let self = this;
        let XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function()
        {
            if (this.readyState == 4 && this.status == 200)
            {
                self.handleNewSessionId(JSON.parse(this.responseText))
            }
        };
        xhttp.open("POST", this.baseURL + "startSession", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("");
    }

    sendExecuteCommandRequest(message)
    {
        let self = this;
        if(this.sessionId==undefined)
        {
            setTimeout(function() {
                self.sendExecuteCommandRequest(message)
            }, 100);
            return;
        }

        let XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function()
        {
            if (this.readyState == 4 && this.status == 200)
            {
                self.handleNewCommandMessageResponse(JSON.parse(this.responseText))
            }
        };
        xhttp.open("POST", this.baseURL + "executeCommand", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("sessionId=" + this.sessionId + "&message=" + message);
    }

    sendGetInformationRequest()
    {
        let self = this;
        let XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function()
        {
            if (this.readyState == 4 && this.status == 200)
            {
                self.handleNewInformationMessage(this.responseText);
            }
        };
        xhttp.open("POST", this.baseURL + "getInformation", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("sessionId=" + this.sessionId);
    }



    handleNewInformationMessage(data)
    {
        let newMessageJson = JSON.parse(data.toString());
        let jsonResponseType = newMessageJson['@type'];
        if(!jsonResponseType)
            return;

        switch (jsonResponseType)
        {
            case "InfoSecCooker.Communication.Messages.InformationMessages.InfoSecFullReportInformationMessage":
                let infoSecFullReportInformationMessage = new InfoSecFullReportInformationMessage(newMessageJson);
                this.gui.onNewGraphRunningInformation(infoSecFullReportInformationMessage);
                break;
            default:
                break;
        }
    }

    handleNewSessionId(sessionId)
    {
        this.sessionId = sessionId;
        this.sendExecuteCommandRequest(new StartNewGraphCommandMsg());
    }

    startReceivingGraphRunningInformation(updateIntervalMilliseconds)
    {
        let self = this;
        this.graphRunningInformationTimer = setInterval(function () {GraphCommunication.fireSendGetInformationRequest(self);}, updateIntervalMilliseconds);
    }

    static fireSendGetInformationRequest(self)
    {
        self.sendGetInformationRequest();
    }

    stopReceivingGraphRunningInformation()
    {
        clearInterval(this.graphRunningInformationTimer);
    }
}

module.exports = {GraphCommunication};

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function demo() {

    let graphComm = new GraphCommunication("127.0.0.1", 4550);
    await sleep(150);
    graphComm.sendExecuteCommandRequest(new GETAvailableTasksList().toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new StartNewGraphCommandMsg().toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new AddSourceTaskCommandMsg("ReadTextFile", 10, "filename").toString());
  //  graphComm.sendExecuteCommandRequest(new SetTickIntervalOfTaskCommandMsg(1, 5000).toString());
    await sleep(50);
    //await sleep(1000);
    graphComm.sendExecuteCommandRequest(new AddTaskCommandMsg("SHA1", 20).toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new AddEdgeCommandMsg(1,2,1,1, 5).toString());
    await sleep(50);// it should be with 3
    graphComm.sendExecuteCommandRequest(new AddTaskCommandMsg("UpperCase", 30).toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new AddEdgeCommandMsg(2,3,1,1, 15).toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new AddSinkTaskCommandMsg("WriteTextToFile", 40, "file").toString());
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new AddEdgeCommandMsg(3,4,1,1, 25).toString());
    //await sleep(2000);
    await sleep(50);
    graphComm.sendExecuteCommandRequest(new LaunchGraphCommandMsg(false).toString());
    await sleep(50);
    graphComm.startReceivingGraphRunningInformation(100);
    await sleep(5000);
    graphComm.stopReceivingGraphRunningInformation();

    await sleep(5000);
    /*
    graphComm.sendExecuteCommandRequest(new SetTickIntervalOfTaskCommandMsg(1, 10).toString());
    graphComm.sendExecuteCommandRequest(new SetTickIntervalOfTaskCommandMsg(2, 20).toString());
    graphComm.sendExecuteCommandRequest(new SetBufferSizeOfTaskCommandMsg(2, 5).toString());

    graphComm.sendExecuteCommandRequest(new RemoveTaskCommandMsg(2).toString());
    graphComm.sendExecuteCommandRequest(new AddTaskCommandMsg("SHA1").toString());
    graphComm.sendExecuteCommandRequest(new AddEdgeCommandMsg(1,3,1,1).toString()); // it should be with 3

    graphComm.sendExecuteCommandRequest(new LaunchGraphCommandMsg(true).toString());
    /*
    graphComm.sendExecuteCommandRequest(new StopAndResetGraphCommandMsg().toString());
    graphComm.sendExecuteCommandRequest(new ResumeGraphCommandMsg().toString());
    graphComm.sendExecuteCommandRequest(new PauseGraphCommandMsg().toString());
    */
}

//demo();
