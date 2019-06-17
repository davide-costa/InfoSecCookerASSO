package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;
import InfoSecCooker.GraphLogic.GraphBuilding.FunctionExtractor;
import InfoSecCooker.GraphLogic.GraphBuilding.MainGraphBuilder;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.ComplexTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.SwitchNodeTask;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FunctionExtractorTest
{

    @BeforeEach
    void setUp()
    {
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void getComplexGraphNode() throws GraphBuildingException
    {
        TestingTaskFactory taskFactory;
        MainGraphBuilder mainGraphBuilder;

        taskFactory = new TestingTaskFactory(new RunTimeConfigurations());
        mainGraphBuilder = new TestingMainGraphBuilder(taskFactory);
        TestingTaskGraphNode taskGraphNodeToAdd1 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));
        TestingTaskGraphNode taskGraphNodeToAdd2 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));
        TestingTaskGraphNode taskGraphNodeToAdd3 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));
        TestingTaskGraphNode taskGraphNodeToAdd4 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));
        TestingTaskGraphNode taskGraphNodeToAdd5 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));
        TestingTaskGraphNode taskGraphNodeToAdd6 = new TestingTaskGraphNode(new GraphNodeInformation("test", "test"));

        taskGraphNodeToAdd1.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd1.setAreNumberOfOutputsInACorrectState(true);
        taskGraphNodeToAdd2.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd2.setAreNumberOfOutputsInACorrectState(true);
        taskGraphNodeToAdd3.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd3.setAreNumberOfOutputsInACorrectState(true);
        taskGraphNodeToAdd4.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd4.setAreNumberOfOutputsInACorrectState(true);
        taskGraphNodeToAdd5.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd5.setAreNumberOfOutputsInACorrectState(true);
        taskGraphNodeToAdd6.setAreNumberOfInputsInACorrectState(true);
        taskGraphNodeToAdd6.setAreNumberOfOutputsInACorrectState(true);

        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd1);
        Long node1Id = mainGraphBuilder.addNewTaskNode("node");
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd2);
        Long node2Id = mainGraphBuilder.addNewTaskNode("node");
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd3);
        Long node3Id = mainGraphBuilder.addNewTaskNode("node");
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd4);
        Long node4Id = mainGraphBuilder.addNewTaskNode("node");
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd5);
        Long node5Id = mainGraphBuilder.addNewTaskNode("node");
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd6);
        Long node6Id = mainGraphBuilder.addNewTaskNode("node");

        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 0);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node2Id, node4Id, 0, 0);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node2Id, node3Id, 1, 0);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node2Id, node3Id, 2, 1);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node3Id, node4Id, 0, 1);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node3Id, node6Id, 1, 0);
        mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node4Id, node5Id, 0, 0);

        ArrayList<TaskGraphNode> nodesToExtract = new ArrayList<>();
        nodesToExtract.add(taskGraphNodeToAdd3);
        nodesToExtract.add(taskGraphNodeToAdd4);

        FunctionExtractor functionExtractor = new FunctionExtractor(nodesToExtract);
        ComplexTaskGraphNode complexTaskGraphNode = functionExtractor.getComplexGraphNode(new RunTimeConfigurations());
        ArrayList<TaskGraphNode> sourceNodes = complexTaskGraphNode.getSourceNodes();
        ArrayList<TaskGraphNode> sinkNodes = complexTaskGraphNode.getSinkNodes();
        ArrayList<TaskGraphNode> taskGraphNodes = complexTaskGraphNode.getTaskGraphNodes();
        ArrayList<SwitchNodeTask> entranceNodes = complexTaskGraphNode.getEntranceNodes();
        ArrayList<SwitchNodeTask> exitNodes = complexTaskGraphNode.getExitNodes();

        ArrayList<TaskGraphNode> correctSourceNodes = new ArrayList<>();
        correctSourceNodes.add(taskGraphNodeToAdd3);
        correctSourceNodes.add(taskGraphNodeToAdd4);


        ArrayList<TaskGraphNode> correctSinkNodes = new ArrayList<>();
        correctSinkNodes.add(taskGraphNodeToAdd3);
        correctSinkNodes.add(taskGraphNodeToAdd4);


        ArrayList<TaskGraphNode> correctTaskGraphNodes = new ArrayList<>();
        correctTaskGraphNodes.add(taskGraphNodeToAdd3);
        correctTaskGraphNodes.add(taskGraphNodeToAdd4);

        Collections.sort(sourceNodes);
        Collections.sort(sinkNodes);
        Collections.sort(taskGraphNodes);
        Collections.sort(entranceNodes);
        Collections.sort(exitNodes);

        Collections.sort(correctSourceNodes);
        Collections.sort(correctSinkNodes);
        Collections.sort(correctTaskGraphNodes);


        assertEquals(correctSourceNodes, sourceNodes);
        assertEquals(correctSinkNodes, sinkNodes);
        assertEquals(correctTaskGraphNodes, taskGraphNodes);

        //Check entrance
        for (TaskGraphNode entranceNode : entranceNodes)
        {
            HashMap<Integer, PipeGraphEdge> entranceNodeInputs = entranceNode.getInputs();
            assertEquals(1, entranceNodeInputs.size());
            PipeGraphEdge entranceNodeInput = entranceNodeInputs.get(0);
            assertNotEquals(null, entranceNodeInput);
            HashMap<Integer, PipeGraphEdge> entranceNodeOutputs = entranceNode.getOutputs();
            assertEquals(1, entranceNodeOutputs.size());
            PipeGraphEdge entranceNodeOutput = entranceNodeOutputs.get(0);
            assertNotEquals(null, entranceNodeOutput);
        }

        int taskGraphNode2OutputsSize = taskGraphNodeToAdd2.getOutputs().size();
        assertEquals(3, taskGraphNode2OutputsSize);
        ArrayList<TaskGraphNode> nodesToWhichTaskGraphNode2Outputs = new ArrayList<>();
        nodesToWhichTaskGraphNode2Outputs.add(taskGraphNodeToAdd4);
        nodesToWhichTaskGraphNode2Outputs.add(taskGraphNodeToAdd3);
        nodesToWhichTaskGraphNode2Outputs.add(taskGraphNodeToAdd3);
        ArrayList<Integer> destinationPortsToWhichTaskGraphNode2Outputs = new ArrayList<>();
        destinationPortsToWhichTaskGraphNode2Outputs.add(0);
        destinationPortsToWhichTaskGraphNode2Outputs.add(0);
        destinationPortsToWhichTaskGraphNode2Outputs.add(1);
        for (int i = 0; i < taskGraphNode2OutputsSize; i++)
        {
            //Check outside edge
            PipeGraphEdge outsideEdge = taskGraphNodeToAdd2.getOutputWithNumber(i);
            assertEquals(i, outsideEdge.getSourcePort());
            assertEquals(0, outsideEdge.getDestinationPort()); //port of the end that connects to the outside node
            assertEquals(taskGraphNodeToAdd2, outsideEdge.getSource()); //port of the end that connects to the short circuit node
            //Check ShortCircuitNode
            TaskGraphNode node = outsideEdge.getDestination();
            assertTrue(node instanceof SwitchNodeTask);
            SwitchNodeTask switchNodeTask = (SwitchNodeTask) node;
            assertEquals(1, switchNodeTask.getInputs().size());
            assertEquals(outsideEdge, switchNodeTask.getInputWithNumber(0));
            assertEquals(1, switchNodeTask.getOutputs().size());
            //Check inside edge
            PipeGraphEdge insideEdge = switchNodeTask.getOutputWithNumber(0);
            assertEquals(switchNodeTask, insideEdge.getSource());
            assertEquals(0, insideEdge.getSourcePort());
            //Check inside edge connects to correct element from nodesToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(nodesToWhichTaskGraphNode2Outputs.get(i), insideEdge.getDestination());
            //Check inside edge connects to correct port from destinationPortsToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(destinationPortsToWhichTaskGraphNode2Outputs.get(i), insideEdge.getDestinationPort());
        }


        //Check exit
        for (TaskGraphNode exitNode : exitNodes)
        {
            HashMap<Integer, PipeGraphEdge> entranceNodeInputs = exitNode.getInputs();
            assertEquals(1, entranceNodeInputs.size());
            PipeGraphEdge entranceNodeInput = entranceNodeInputs.get(0);
            assertNotEquals(null, entranceNodeInput);
            HashMap<Integer, PipeGraphEdge> entranceNodeOutputs = exitNode.getOutputs();
            assertEquals(1, entranceNodeOutputs.size());
            PipeGraphEdge entranceNodeOutput = entranceNodeOutputs.get(0);
            assertNotEquals(null, entranceNodeOutput);
        }

        //Check outside node 5
        int taskGraphNode5InputsSize = taskGraphNodeToAdd5.getInputs().size();
        assertEquals(1, taskGraphNode5InputsSize);
        ArrayList<TaskGraphNode> nodesFromWhichTaskGraphNode5Inputs = new ArrayList<>();
        nodesFromWhichTaskGraphNode5Inputs.add(taskGraphNodeToAdd4);
        ArrayList<Integer> sourcePortsFromWhichTaskGraphNode5Inputs = new ArrayList<>();
        sourcePortsFromWhichTaskGraphNode5Inputs.add(0);
        for (int i = 0; i < taskGraphNode5InputsSize; i++)
        {
            //Check outside edge
            PipeGraphEdge outsideEdge = taskGraphNodeToAdd5.getInputWithNumber(i);
            assertEquals(i, outsideEdge.getDestinationPort()); //port of the end that connects to the outside node
            assertEquals(0, outsideEdge.getSourcePort()); //port of the end that connects to the short circuit node
            assertEquals(taskGraphNodeToAdd5, outsideEdge.getDestination());
            //Check ShortCircuitNode
            TaskGraphNode node = outsideEdge.getSource();
            assertTrue(node instanceof SwitchNodeTask);
            SwitchNodeTask switchNodeTask = (SwitchNodeTask) node;
            assertEquals(1, switchNodeTask.getInputs().size());
            assertEquals(outsideEdge, switchNodeTask.getOutputWithNumber(0));
            assertEquals(1, switchNodeTask.getOutputs().size());
            //Check inside edge
            PipeGraphEdge insideEdge = switchNodeTask.getInputWithNumber(0);
            assertEquals(switchNodeTask, insideEdge.getDestination());
            assertEquals(0, insideEdge.getDestinationPort());
            //Check inside edge connects to correct element from nodesToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(nodesFromWhichTaskGraphNode5Inputs.get(i), insideEdge.getSource());
            //Check inside edge connects to correct port from destinationPortsToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(sourcePortsFromWhichTaskGraphNode5Inputs.get(i), insideEdge.getSourcePort());
        }

        //Check outside node 6
        int taskGraphNode6InputsSize = taskGraphNodeToAdd6.getInputs().size();
        assertEquals(1, taskGraphNode6InputsSize);
        ArrayList<TaskGraphNode> nodesFromWhichTaskGraphNode6Inputs = new ArrayList<>();
        nodesFromWhichTaskGraphNode6Inputs.add(taskGraphNodeToAdd3);
        ArrayList<Integer> sourcePortsFromWhichTaskGraphNode6Inputs = new ArrayList<>();
        sourcePortsFromWhichTaskGraphNode6Inputs.add(1);
        for (int i = 0; i < taskGraphNode6InputsSize; i++)
        {
            //Check outside edge
            PipeGraphEdge outsideEdge = taskGraphNodeToAdd6.getInputWithNumber(i);
            assertEquals(i, outsideEdge.getDestinationPort()); //port of the end that connects to the outside node
            assertEquals(0, outsideEdge.getSourcePort()); //port of the end that connects to the short circuit node
            assertEquals(taskGraphNodeToAdd6, outsideEdge.getDestination());
            //Check ShortCircuitNode
            TaskGraphNode node = outsideEdge.getSource();
            assertTrue(node instanceof SwitchNodeTask);
            SwitchNodeTask switchNodeTask = (SwitchNodeTask) node;
            assertEquals(1, switchNodeTask.getInputs().size());
            assertEquals(outsideEdge, switchNodeTask.getOutputWithNumber(0));
            assertEquals(1, switchNodeTask.getOutputs().size());
            //Check inside edge
            PipeGraphEdge insideEdge = switchNodeTask.getInputWithNumber(0);
            assertEquals(switchNodeTask, insideEdge.getDestination());
            assertEquals(0, insideEdge.getDestinationPort());
            //Check inside edge connects to correct element from nodesToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(nodesFromWhichTaskGraphNode6Inputs.get(i), insideEdge.getSource());
            //Check inside edge connects to correct port from destinationPortsToWhichTaskGraphNode2Outputs ArrayList
            assertEquals(sourcePortsFromWhichTaskGraphNode6Inputs.get(i), insideEdge.getSourcePort());
        }

    }
}