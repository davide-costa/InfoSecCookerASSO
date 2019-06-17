class GraphData
{
    // defining vertex array and
    // adjacent list
    constructor()
    {
        this.nodesInfo = new Array();

        //associates edges and nodes connected
        this.edgeBackendIds = new Array();
        this.edgeFrontendIds = new Array();
        this.node1OfEdge = new Array();
        this.node2OfEdge = new Array();
        this.node1OutputNumber = new Array();
        this.node2InputNumber = new Array();
    }

    addNode(frontendId, taskName, coords)
    {
        let nodeInfo = new NodeInfo(frontendId, taskName, coords);
        this.nodesInfo.push(nodeInfo);
    }


    updateNodeIdBasedOnBackendResponse(frontendId, backendId)
    {
        this.nodesInfo.forEach(function (node)
        {
            if(node.frontendId == frontendId)
            {
                node.id = backendId;
                return;
            }
        })
    }

    updateEdgeIdBasedOnBackendResponse(frontendId, backendId)
    {
        for(let i = 0; i < this.edgeFrontendIds.length; i++)
        {
            if(this.edgeFrontendIds[i] == frontendId)
            {
                this.edgeBackendIds[i] = backendId;
                return;
            }
        }

        for(let i = 0; i < this.nodesInfo.length; i++)
        {
            let inputs = this.nodesInfo[i].inputs;
            for(let j = 0; j < inputs.length; j++)
            {
                if(inputs.edgeId == frontendId)
                    inputs.edge = backendId;
            }
        }
    }

    connectNodes(node1Id, node2Id,
                    outputNumber, inputNumber, edgeFrontendId)
    {
        this.nodesInfo.forEach(function (nodeInfo, i)
        {
            if (nodeInfo.id === node2Id)
            {
                nodeInfo.connectInput(inputNumber, node1Id, edgeFrontendId, outputNumber);
                this.node2OfEdge.push(nodeInfo);
            }

            if (nodeInfo.id === node1Id)
                this.node1OfEdge.push(nodeInfo);
        });

        this.edgeBackendIds.push(0);
        this.edgeFrontendIds.push(edgeFrontendId);
        this.node1OutputNumber.push(outputNumber);
        this.node2InputNumber.push(inputNumber);
    }

    removeNode(id)
    {
        this.nodesInfo.forEach(function (nodeInfo, i)
        {
            if (nodeInfo.id === id)
            {
                this.nodesInfo.splice(i, 1);
            }
        });
    }

    removeEdge(id)
    {
        this.edgeBackendIds.forEach(function (edge, i)
        {
            if (edge.id === id)
            {
                this.node1OfEdge.removeOutput(this.node1OutputNumber[i]);
                this.node2OfEdge.removeInput();

                this.edgeBackendIds.splice(i, 1);
                this.edgeFrontendIds.splice(i, 1);
                this.node1OfEdge.splice(i, 1);
                this.node2OfEdge.splice(i, 1);
                this.node1OutputNumber.splice(i, 1);
                this.node2InputNumber.splice(i, 1);
            }
        });
    }

    // functions to be implemented

    // addVertex(v)
    // addEdge(v, w)
    // printGraph()

    // bfs(v)
    // dfs(v)
}

module.exports = {GraphData};

class NodeInfo
{
    constructor(frontendId, taskName, coords)
    {
        this.id = 0;
        this.frontendId = frontendId;
        this.taskName = taskName;
        this.coords = coords;
        this.numberOfInputs = 0;
        this.inputs = new Array();
        this.numberOfOutputs = 0;
    }

    addInput()
    {
        this.numberOfInputs++;
        this.inputs.push(new NodeInputInfo());
    }

    addOutput()
    {
        this.numberOfOutputs++;
    }

    /**
     * counting starts at 1
     * @param {*} number
     */
    removeInput(number)
    {
        if (this.numberOfInputs <= number)
            return;

        this.numberOfInputs--;
        this.inputs.splice(number, 1);
    }

    /**
     * counting starts at 1
     * @param {*} number
     */
    removeOutput(number)
    {
        if (this.numberOfOutputs <= number)
            return;

        this.numberOfOutputs--;
    }

    connectInput(inputNumber, nodeIdThatIsConnectedTo, edgeId, outputNumberOfNodeThatIsConnectedTo)
    {
        this.inputs[inputNumber].connect(nodeIdThatIsConnectedTo, edgeId, outputNumberOfNodeThatIsConnectedTo);
    }
}

class NodeInputInfo
{
    constructor()
    {
        this.isConnected = false;
        this.nodeIdThatIsConnectedTo = null;
        this.edgeId = null;
    }

    connect(nodeIdThatIsConnectedTo, edgeId, outputNumberOfNodeThatIsConnectedTo)
    {
        this.isConnected = true;
        this.nodeIdThatIsConnectedTo = nodeIdThatIsConnectedTo;
        this.edgeId = edgeId;
        this.outputNumberOfNodeThatIsConnectedTo = outputNumberOfNodeThatIsConnectedTo;
    }

    setEdgeId(edgeId)
    {
        this.edgeId = edgeId;
    }
}

class Coords
{
    constructor(x, y)
    {
        this.x = x;
        this.y = y;
    }
}
