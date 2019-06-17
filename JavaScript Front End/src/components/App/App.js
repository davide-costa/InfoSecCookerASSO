import React, { Component } from 'react';
import './App.css';
import 'jquery-confirm';
import '../../../node_modules/jquery/dist/jquery.js';
import "lodash";
import "backbone";
import "jointjs/dist/joint.css";
import {GraphCommunication} from "../../communication";
import {GETAvailableTasksList} from "../../Messages/GETMessages/GETAvailableTasksList";
import {AddSourceTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/AddSourceTaskCommandMsg";
import {AddTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/AddTaskCommandMsg";
import {AddSinkTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/AddSinkTaskCommandMsg";
import {AddEdgeCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/AddEdgeCommandMsg";
import {GraphData} from "../../graphData";
import {StartNewGraphCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/StartNewGraphCommandMsg";
import {LaunchGraphCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/LaunchGraphCommandMsg";
import {PauseGraphCommandMsg} from "../../Messages/CommandMessages/GraphRunning/PauseGraphCommandMsg";
import {StopAndResetGraphCommandMsg} from "../../Messages/CommandMessages/GraphRunning/StopAndResetGraphCommandMsg";
import {ResumeGraphCommandMsg} from "../../Messages/CommandMessages/GraphRunning/ResumeGraphCommandMsg";
import {SetBufferSizeOfTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/SetBufferSizeOfTaskCommandMsg";
import {SetTickIntervalOfTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/SetTickIntervalOfTaskCommandMsg";
import {SetNumberOfCyclesOfTaskCommandMsg} from "../../Messages/CommandMessages/GraphBuilding/SetNumberOfCyclesOfTaskCommandMsg";
import {EndSessionCommandMsg} from "../../Messages/CommandMessages/EndSessionCommandMsg";
const joint = require('jointjs');
const $ = require('jquery');


export default class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
      graph: {},
      paper: {},
      nodesList: [],
      nodes: [],
      nodeStates: [],
      edges: [],
      currTemporaryId: 0,
      infoBox: "",
    }

    this.communication = new GraphCommunication("127.0.0.1", 4550, this);
    this.graphData = new GraphData();
    this.getTasksNodes = this.getTasksNodes.bind(this);
    this.onDragDrop = this.onDragDrop.bind(this);
    this.onDragOver = this.onDragOver.bind(this);
    this.onDrag = this.onDrag.bind(this);
    this.addNodeToGraph = this.addNodeToGraph.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);
    this.addNodeToGraph = this.addNodeToGraph.bind(this);
    this.updateNodeIdBasedOnBackendResponse = this.updateNodeIdBasedOnBackendResponse.bind(this);
    this.onPlay = this.onPlay.bind(this);
    this.onPause = this.onPause.bind(this);
    this.onStop = this.onStop.bind(this);
    this.onEndSession = this.onEndSession.bind(this);
  }

  getTasksNodes()
  {
    this.communication.sendExecuteCommandRequest(new GETAvailableTasksList().toString());
  }

  addNodesToNodesList(nodes) {
    let nodesArray = [];
    nodes.forEach((n) => {
        let newTemporaryId = this.state.currTemporaryId + 1;
        nodesArray.push(
        <div key={newTemporaryId}
             information={n.name + "/" + n.numberInputs + "/" + n.numberOutputs + "/" + n.nodeType}
             id ={newTemporaryId}
            onDragStart = {(e) => this.onDrag(e)}
            draggable
            className="draggable"
        >
          {n.name}
        </div>
      );
      this.setState({
        currTemporaryId: newTemporaryId
      })
    });
    this.setState({
      nodesList: nodesArray,
    })
  }

  onDragDrop(e)
  {
    e.preventDefault();

    //create frontend node
    let taskTemporaryId = e.dataTransfer.getData("id");
    const coords = { x: e.pageX, y: e.pageY };
    var nodeInformation = e.dataTransfer.getData("information");
    let splittedNodeInformation = nodeInformation.split("/");
    let taskName = splittedNodeInformation[0];
    let nodeType = splittedNodeInformation[3];
    let newNode = this.addNodeToGraph(taskTemporaryId, nodeInformation, coords);

    //add to graph data
    this.graphData.addNode(newNode.id, taskName, coords);

    //backend request
    if(nodeType == "Source")
    {
      let additionalInfo = prompt("Please enter your additional information");
      this.communication.sendExecuteCommandRequest(new AddSourceTaskCommandMsg(taskName, taskTemporaryId, additionalInfo));
    }
    else if(nodeType == "Sink")
    {
      let additionalInfo = prompt("Please enter your additional information");
      this.communication.sendExecuteCommandRequest(new AddSinkTaskCommandMsg(taskName, taskTemporaryId, additionalInfo));
    }
    else
      this.communication.sendExecuteCommandRequest(new AddTaskCommandMsg(taskName, taskTemporaryId));


  }

  onDragOver(e) {
    e.preventDefault();
  }

  onDrag(e)
  {
    e.dataTransfer.setData("information", e.target.getAttribute("information"));
    e.dataTransfer.setData("id", e.target.id);
  }

  updateNodeIdBasedOnBackendResponse(temporaryAssignedId, newId)
  {
    this.state.nodes.forEach(function (node)
    {
      if(node.attributes.backendId == temporaryAssignedId)
      {
        node.attributes.backendId = newId;
        return;
      }
    })
  }

  updateEdgeIdBasedOnBackendResponse(temporaryAssignedId, newId)
  {
    this.state.edges.forEach(function (edge)
    {
      if(edge.attributes.backendId == temporaryAssignedId)
      {
        edge.attributes.backendId = newId;
        return;
      }
    })
  }

  addNodeToGraph(taskTemporaryId, nodeInformation, coords) {
    const nodeInfo = nodeInformation.split("/");
    const name = nodeInfo[0];
    const nrInputs = nodeInfo[1];
    const nrOutputs = nodeInfo[2];

    this.state.nodeStates.push("");
    let node = new joint.shapes.devs.Model({
      backendId: taskTemporaryId,
      position: { x: coords.x - 160, y: coords.y - 60 },
      size: { width: 120, height: 150 },
      ports: {
        groups: {
          in: {
            attrs: {
              ".port-body": {
                fill: "#16A085",
                magnet: "passive"
              }
            }
          },
          out: {
            attrs: {
              ".port-body": {
                fill: "#E74C3C"
              }
            }
          }
        }
      },
      attrs: {
        ".label": { text: name, "ref-x": 0.5, "ref-y": 0.2 },
        rect: { fill: "#2ECC71" }
      }
    });

    console.log(this.state.nodeStates);
    //node.attributes.attrs[".label"].text = "SHA\nSLEEPING\nFILENAME"
    for (let i = 0; i < nrInputs; i++) {
      node.addInPort("I" + (i + 1));
    }

    for (let i = 0; i < nrOutputs; i++) {
      node.addOutPort("O" + (i + 1));
    }

    let newNodesArray = this.state.nodes;
    newNodesArray.push(node);
    this.setState({
      nodes: newNodesArray,
    });
    node.addTo(this.state.graph);

    return node;
  }

  componentDidMount() {
    let self = this;
    this.getTasksNodes();
    // create graph
    let graphVar = new joint.dia.Graph();
    const offsetWidth = (25 * window.innerWidth) / 100;
    const offsetHeight = (window.innerHeight) / 100;
    graphVar.on("change:source change:target", function(element) {
      self.onNewEdge(element)
    });

    // create paper
    let paperVar = new joint.dia.Paper({
      el: document.getElementById("paintGrid"),
      model: graphVar,
      width: window.innerWidth - offsetWidth,
      height: window.innerHeight - offsetHeight,
      gridSize: 1,
      // disable built-in link dragging
      interactive: {
        linkMove: false
      },
      validateConnection: function(
        cellViewS,
        magnetS,
        cellViewT,
        magnetT,
        end,
        linkView
      ) {
        // Prevent linking from input ports.
        if (
          magnetS &&
          magnetS.getAttribute("port-group") === "in"
        )
          return false;
        // Prevent linking from output ports to input ports within one element.
        if (cellViewS === cellViewT) return false;
        // Prevent linking to input ports.
        return (
          magnetT && magnetT.getAttribute("port-group") === "in"
        );
      },
      validateMagnet: function(cellView, magnet) {
        // Note that this is the default behaviour. Just showing it here for reference.
        // Disable linking interaction for magnets marked as passive (see below `.inPorts circle`).
        return magnet.getAttribute("magnet") !== "passive";
      }
    });

      paperVar.on('element:pointerdblclick', function(elementView)
      {
          let bufferSize = prompt("Please enter buffer size (empty for ignore)").toString();
          let tickInterval = prompt("Please enter tick interval (empty for ignore)").toString();
          let numberOfCycles = prompt("Please enter the number of node cycles (0 for infinite)").toString();
          let backendId = elementView.model.attributes.backendId;

          if(bufferSize.length > 0)
              self.communication.sendExecuteCommandRequest(new SetBufferSizeOfTaskCommandMsg(backendId, parseInt(bufferSize)));

          if(tickInterval.length > 0)
            self.communication.sendExecuteCommandRequest(new SetTickIntervalOfTaskCommandMsg(backendId, parseInt(tickInterval)));

          if(numberOfCycles.length > 0)
            self.communication.sendExecuteCommandRequest(new SetNumberOfCyclesOfTaskCommandMsg(backendId, parseInt(numberOfCycles)));
      });

    this.setState({
      graph: graphVar,
      paper: paperVar,
    })

    $(window).resize(function() {
      paperVar.setDimensions(window.innerWidth - (25 * window.innerWidth) / 100, window.innerHeight);
    });
  }

  onNewEdge(element)
  {
      if (element.attributes.target.port == undefined)
          return;
      else
      {
          console.log(element.attributes);
          let sourceFrontendId = element.attributes.source.id;
          let destinationFrontendId = element.attributes.target.id;

          let sourceBackendId = 0;
          let destinationBackendId = 0;
          this.state.nodes.forEach(function (node)
          {
              if(node.attributes.id == sourceFrontendId)
                  sourceBackendId = node.attributes.backendId;
              else if(node.attributes.id == destinationFrontendId)
                  destinationBackendId = node.attributes.backendId;
          });

          let sourcePortString = element.attributes.source.port;
          let sourceOutputNumber = parseInt(sourcePortString.substr(1,1));
          let destinationPortString = element.attributes.target.port;
          let destinationInputNumber = parseInt(destinationPortString.substr(1,1));

          let newTemporaryId = this.state.currTemporaryId + 1;
          element.attributes.backendId = newTemporaryId;
            this.setState({
              currTemporaryId: newTemporaryId
            });

            this.communication.sendExecuteCommandRequest(new AddEdgeCommandMsg(sourceBackendId, destinationBackendId,
                sourceOutputNumber, destinationInputNumber, element.attributes.backendId));

          let newEdgesArray = this.state.edges;
          newEdgesArray.push(element);
          this.setState({
            currTemporaryId: newTemporaryId
          });

          this.graphData.connectNodes(sourceBackendId, destinationBackendId, sourceOutputNumber, destinationInputNumber, element.id)
        }
  }

  onResume() {
    this.communication.sendExecuteCommandRequest(new ResumeGraphCommandMsg());

    document.getElementById('resume').style.display = 'none';
    document.getElementById('pause').style.display = 'inline';
    document.getElementById('stop').style.display = 'inline';
  }

  onPause() {
    this.communication.sendExecuteCommandRequest(new PauseGraphCommandMsg());

    document.getElementById('resume').style.display = 'inline';
    document.getElementById('pause').style.display = 'none';
    document.getElementById('stop').style.display = 'none';
  }

  onStop()
  {
    this.communication.sendExecuteCommandRequest(new StopAndResetGraphCommandMsg());

    //update frontend
    document.getElementById('resume').style.display = 'inline';
    document.getElementById('pause').style.display = 'none';
    document.getElementById('stop').style.display = 'none';
  }

  onPlay()
  {
    if (!window.confirm("Run graph despite errors"))
      return;

    this.communication.sendExecuteCommandRequest(new LaunchGraphCommandMsg());
    this.communication.startReceivingGraphRunningInformation(500);

    //update frontend
    document.getElementById('messageBox').style.display = 'block';
    document.getElementById('pause').style.display = 'inline';
    document.getElementById('stop').style.display = 'inline';
    document.getElementById('play').style.display = 'none';
  }

  onNewGraphRunningInformation(infoSecFullReportInformationMessage)
  {
    let nodesIdsArray = infoSecFullReportInformationMessage.taskNodeStates["@keys"];
    let nodesStatesArray = infoSecFullReportInformationMessage.taskNodeStates["@items"];
    let nodesTickCountArray = infoSecFullReportInformationMessage.currentTickCountOfAllNodes["@items"];
    for(let i = 0; i < nodesIdsArray.length; i++)
    {
      let nodeId = nodesIdsArray[i];
      let newNodes = this.state.nodes;
      for(let j = 0; j < newNodes.length; j++)
      {
       if(newNodes[j].attributes.backendId == nodeId)
       {
         let labelSplitted = newNodes[j].attributes.attrs[".label"].text.toString().split("\n");
         this.state.graph.getElements()[j].attr('.label/text', labelSplitted[0] + "\n" + nodesStatesArray[i] + "\n" + nodesTickCountArray[i]);
       }
      }
    }
  }

  onEndSession()
  {
        this.communication.sendExecuteCommandRequest(new EndSessionCommandMsg(JSON.stringify(this.graphData)));
  }

  render () {
    return (
      <div className="container-drag">
        <div id="nodesList" className="wip">
          <span className="task-header">Nodes</span>
          {this.state.nodesList}
        </div>
        <div id="messageBox" className="info box">
          {this.state.infoBox}
        </div>
        <div
          id="paintGrid"
          className="fullWH"
          onDragOver= {(e) => {
            this.onDragOver(e)
          }}
          onDrop={(e) => {
            this.onDragDrop(e)
          }}
        >
        </div>
        <button id="endSession" onClick={this.onEndSession}>
          End Session
        </button>
        <div id="buttons">
          <button id="play" onClick={this.onPlay}>
            Play
          </button>
          <button id="resume" onClick={this.onResume}>
            Resume
          </button>
          <button id="pause" onClick={this.onPause}>
            Pause
          </button>
          <button id="stop" onClick={this.onStop}>
            Stop
          </button>
        </div>
      </div>
    );
  }
}
