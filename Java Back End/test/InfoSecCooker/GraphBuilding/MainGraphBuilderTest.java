package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.*;
import InfoSecCooker.GraphLogic.GraphBuilding.MainGraphBuilder;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MainGraphBuilderTest
{
    TestingTaskFactory taskFactory;
    MainGraphBuilder mainGraphBuilder;

    @BeforeEach
    void setUp()
    {
        taskFactory = new TestingTaskFactory(new RunTimeConfigurations());
        mainGraphBuilder = new TestingMainGraphBuilder(taskFactory);
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void areAllNodesFullyConnected()
    {
    }

    @Test
    void addNewTaskNode()
    {
        taskFactory.setTaskGraphNodeToReturnNext(null);
        assertThrows(AttemptToCreateUnknownNode.class, () ->
        {
            mainGraphBuilder.addNewTaskNode("");
        });

        Pair<TestingTaskGraphNode, Long> nodeAdditionReturn;
        TestingTaskGraphNode taskGraphNodeToAdd;
        Long nodeId;

        try
        {
            nodeAdditionReturn = addNewNode("test", "test");
            taskGraphNodeToAdd = nodeAdditionReturn.getKey();
            nodeId = nodeAdditionReturn.getValue();

        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(1, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        TaskGraphNode taskGraphNodeAdded = mainGraphBuilder.unconnectedGraphNodes.get(new Long(nodeId));
        assertNotEquals(null, taskGraphNodeAdded);
        assertEquals(taskGraphNodeToAdd, taskGraphNodeAdded);
    }

    private Pair<TestingTaskGraphNode, Long> addNewNode(String type, String name) throws GraphBuildingException
    {
        TestingTaskGraphNode taskGraphNodeToAdd = new TestingTaskGraphNode(new GraphNodeInformation(type, name));
        taskFactory.setTaskGraphNodeToReturnNext(taskGraphNodeToAdd);
        Long nodeId = mainGraphBuilder.addNewTaskNode("");
        return new Pair(taskGraphNodeToAdd, nodeId);
    }

    @Test
    void addEdgeFromNode1ToNode2OutputToInput()
    {
        Pair<TestingTaskGraphNode, Long> node1AdditionReturn;
        Pair<TestingTaskGraphNode, Long> node2AdditionReturn;
        TestingTaskGraphNode taskGraphNode1;
        TestingTaskGraphNode taskGraphNode2;
        try
        {
            node1AdditionReturn = addNewNode("test1", "test1");
            node2AdditionReturn = addNewNode("test2", "test2");
            taskGraphNode1 = node1AdditionReturn.getKey();
            taskGraphNode2 = node2AdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        Long node1Id = taskGraphNode1.getGraphNodeInformation().getId();
        Long node2Id = taskGraphNode2.getGraphNodeInformation().getId();
        assertThrows(ReferenceToUnExistantNodeInTheGraph.class, () ->
        {
            mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(3, node2Id, 0, 1);
        });
        assertThrows(ReferenceToUnExistantNodeInTheGraph.class, () ->
        {
            mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, 3, 0, 1);
        });
        assertThrows(ReferenceToUnExistantNodeInTheGraph.class, () ->
        {
            mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(3, 3, 0, 1);
        });

        //add and edge node1 output 0 -> node2 input 1
        Long edge1Id;
        try
        {
            edge1Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 1);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }

        assertThrows(AttemptingToConnectOutputAlreadyConnected.class, () ->
        {
            mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 2);
        });
        assertThrows(AttemptingToConnectInputAlreadyConnected.class, () ->
        {
            mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 1, 1);
        });

        //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
        assertEquals(0, taskGraphNode1.getInputs().size());
        assertEquals(1, taskGraphNode1.getOutputs().size());
        assertEquals(1, taskGraphNode2.getInputs().size());
        assertEquals(0, taskGraphNode2.getOutputs().size());
        PipeGraphEdge edgeConnectedToNode1 = taskGraphNode1.getOutputWithNumber(0);
        PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

        assertNotEquals(null, edgeConnectedToNode1);
        assertNotEquals(null, edgeConnectedToNode2);
        //assert that the output edge of one node is the same as the input edge of the other
        assertEquals(edgeConnectedToNode1, edgeConnectedToNode2);

        //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
        PipeGraphEdge edgeCreated = edgeConnectedToNode1;

        //Check MainGraphBuilder's HashMaps
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(1, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        //Check HashMap contents
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge1Id);
            assertEquals(edgeCreated, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge1Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge1Id);
            assertEquals(0, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge1Id);
            assertEquals(taskGraphNode2, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge1Id);
            assertEquals(1, downstreamOutputNumber);
        }

        //Add another node: node3

        Pair<TestingTaskGraphNode, Long> nodeAdditionReturn;
        TaskGraphNode taskGraphNode3;
        try
        {
            nodeAdditionReturn = addNewNode("test3", "test3");
            taskGraphNode3 = nodeAdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        Long node3Id = taskGraphNode3.getGraphNodeInformation().getId();

        //Add another edge
        //node 1 output 2 -> node 3 input 4
        Long edge2Id;
        try
        {
            edge2Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node3Id, 2, 4);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }


        //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
        assertEquals(0, taskGraphNode1.getInputs().size());
        assertEquals(2, taskGraphNode1.getOutputs().size());
        assertEquals(1, taskGraphNode2.getInputs().size());
        assertEquals(0, taskGraphNode2.getOutputs().size());
        assertEquals(1, taskGraphNode3.getInputs().size());
        assertEquals(0, taskGraphNode3.getOutputs().size());
        PipeGraphEdge edge2ConnectedToNode1 = taskGraphNode1.getOutputWithNumber(2);
        PipeGraphEdge edgeConnectedToNode3 = taskGraphNode3.getInputWithNumber(4);

        assertNotEquals(null, edge2ConnectedToNode1);
        assertNotEquals(null, edgeConnectedToNode3);
        //assert that the output edge of one node is the same as the input edge of the other
        assertEquals(edge2ConnectedToNode1, edgeConnectedToNode3);

        //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
        PipeGraphEdge edge2Created = edge2ConnectedToNode1;

        //Check MainGraphBuilder's HashMaps
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(3, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(2, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(2, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(2, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(2, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(2, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        //Check HashMap contents regarding edge 1 remain correct
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge1Id);
            assertEquals(edgeCreated, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge1Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge1Id);
            assertEquals(0, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge1Id);
            assertEquals(taskGraphNode2, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge1Id);
            assertEquals(1, downstreamOutputNumber);
        }

        //Check HashMap contents regarding new edge 2 are correct
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge2Id);
            assertEquals(edge2Created, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge2Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge2Id);
            assertEquals(2, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge2Id);
            assertEquals(taskGraphNode3, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge2Id);
            assertEquals(4, downstreamOutputNumber);
        }

        //Check functionality of adding a node to the fullyConnectedGraphNodes HashMap
        taskGraphNode1.setAreNumberOfInputsInACorrectState(true);
        taskGraphNode1.setAreNumberOfOutputsInACorrectState(true);
        //Add another edge
        //node 1 output 2 -> node 3 input 4
        Long edge3Id;
        try
        {
            edge3Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node3Id, 3, 5);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }


        //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
        assertEquals(0, taskGraphNode1.getInputs().size());
        assertEquals(3, taskGraphNode1.getOutputs().size());
        assertEquals(1, taskGraphNode2.getInputs().size());
        assertEquals(0, taskGraphNode2.getOutputs().size());
        assertEquals(2, taskGraphNode3.getInputs().size());
        assertEquals(0, taskGraphNode3.getOutputs().size());
        PipeGraphEdge edge3ConnectedToNode1 = taskGraphNode1.getOutputWithNumber(3);
        PipeGraphEdge edge3ConnectedToNode3 = taskGraphNode3.getInputWithNumber(5);

        assertNotEquals(null, edge3ConnectedToNode3);
        assertNotEquals(null, edge3ConnectedToNode3);
        //assert that the output edge of one node is the same as the input edge of the other
        assertEquals(edge3ConnectedToNode1, edge3ConnectedToNode3);

        //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
        PipeGraphEdge edge3Created = edge3ConnectedToNode1;

        //Check MainGraphBuilder's HashMaps
        assertEquals(1, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(3, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(3, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(3, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(3, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(3, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        //Check HashMap contents regarding edge 1 remain correct
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge1Id);
            assertEquals(edgeCreated, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge1Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge1Id);
            assertEquals(0, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge1Id);
            assertEquals(taskGraphNode2, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge1Id);
            assertEquals(1, downstreamOutputNumber);
        }

        //Check HashMap contents regarding new edge 2 remain correct
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge2Id);
            assertEquals(edge2Created, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge2Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge2Id);
            assertEquals(2, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge2Id);
            assertEquals(taskGraphNode3, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge2Id);
            assertEquals(4, downstreamOutputNumber);
        }

        //Check HashMap contents regarding new edge 3 are correct
        {
            PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge3Id);
            assertEquals(edge3Created, edgeOnMap);
            TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge3Id);
            assertEquals(taskGraphNode1, upstreamNode);
            Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge3Id);
            assertEquals(3, upstreamOutputNumber);
            TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge3Id);
            assertEquals(taskGraphNode3, downstreamNode);
            Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge3Id);
            assertEquals(5, downstreamOutputNumber);
        }

    }

    @Test
    void removeTaskNode()
    {
        Pair<TestingTaskGraphNode, Long> node1AdditionReturn;
        Pair<TestingTaskGraphNode, Long> node2AdditionReturn;
        TestingTaskGraphNode taskGraphNode1;
        TestingTaskGraphNode taskGraphNode2;
        try
        {
            node1AdditionReturn = addNewNode("test1", "test1");
            taskGraphNode1 = node1AdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }

        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(1, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        Long node1Id = node1AdditionReturn.getValue();
        TaskGraphNode taskGraphNodeAdded = mainGraphBuilder.unconnectedGraphNodes.get(new Long(node1Id));
        assertNotEquals(null, taskGraphNodeAdded);
        assertEquals(taskGraphNode1, taskGraphNodeAdded);

        try
        {
            mainGraphBuilder.removeTaskNode(taskGraphNode1.getGraphNodeInformation().getId());
        } catch (ReferenceToUnExistantNodeInTheGraph referenceToUnExistantNodeInTheGraph)
        {
            fail("Exception thrown when removing a task node that is supposed to be able to be removed.");
            return;
        }

        //Assert that the node has been removed from the class' internal information
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        //Now re-add node removed and add another one
        try
        {
            node1AdditionReturn = addNewNode("test1", "test1");
            node2AdditionReturn = addNewNode("test2", "test2");
            taskGraphNode1 = node1AdditionReturn.getKey();
            taskGraphNode2 = node2AdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }

        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        Long secondAddedNode1Id = node1AdditionReturn.getValue();
        Long secondAddedNode2Id = node2AdditionReturn.getValue();
        taskGraphNodeAdded = mainGraphBuilder.unconnectedGraphNodes.get(new Long(secondAddedNode1Id));
        assertNotEquals(null, taskGraphNodeAdded);
        assertEquals(taskGraphNode1, taskGraphNodeAdded);

        taskGraphNode1.resetDestructorCalledFlag();
        try
        {
            mainGraphBuilder.removeTaskNode(taskGraphNode1.getGraphNodeInformation().getId());
        } catch (ReferenceToUnExistantNodeInTheGraph referenceToUnExistantNodeInTheGraph)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        assertTrue(taskGraphNode1.isDestructorCalled());

        //Assert that the node has been removed from the class' internal information
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(1, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());


        TaskGraphNode taskGraphNodeAdded2 = mainGraphBuilder.unconnectedGraphNodes.get(secondAddedNode2Id);
        assertNotEquals(null, taskGraphNodeAdded2);
        assertEquals(taskGraphNode2, taskGraphNodeAdded2);

        taskGraphNode2.resetDestructorCalledFlag();
        try
        {
            mainGraphBuilder.removeTaskNode(taskGraphNode2.getGraphNodeInformation().getId());
        } catch (ReferenceToUnExistantNodeInTheGraph referenceToUnExistantNodeInTheGraph)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        assertTrue(taskGraphNode2.isDestructorCalled());

        //Assert that the node has been removed from the class' internal information
        assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.unconnectedGraphNodes.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
        assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

        Long node2Id = taskGraphNode2.getGraphNodeInformation().getId();

        //Add edge to test if the node removal causes the edges to also be removed from the HashMap's
        //add and edge node1 output 0 -> node2 input 1

        try
        {
            node1AdditionReturn = addNewNode("test1", "test1");
            node2AdditionReturn = addNewNode("test2", "test2");
            taskGraphNode1 = node1AdditionReturn.getKey();
            taskGraphNode2 = node2AdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }
        node1Id = taskGraphNode1.getGraphNodeInformation().getId();
        node2Id = taskGraphNode2.getGraphNodeInformation().getId();


        Long edge1Id;
        try
        {
            edge1Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 1);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }

        try
        {
            mainGraphBuilder.removeTaskNode(taskGraphNode1.getGraphNodeInformation().getId());
        } catch (ReferenceToUnExistantNodeInTheGraph referenceToUnExistantNodeInTheGraph)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }

        {
            //assert hash maps of the nodes are set properly and that the they no longer contain the edge
            assertEquals(0, taskGraphNode1.getInputs().size());
            assertEquals(0, taskGraphNode2.getInputs().size());
            assertEquals(0, taskGraphNode2.getOutputs().size());
            PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

            assertEquals(null, edgeConnectedToNode2);

            //Check MainGraphBuilder's HashMaps
            assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
            assertEquals(1, mainGraphBuilder.unconnectedGraphNodes.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());
        }
    }

    @Test
    void removeEdge()
    {
        assertThrows(ReferenceToUnExistantEdgeInTheGraph.class, () ->
        {
            mainGraphBuilder.removeEdge(1);
        });

        Pair<TestingTaskGraphNode, Long> node1AdditionReturn;
        Pair<TestingTaskGraphNode, Long> node2AdditionReturn;
        TestingTaskGraphNode taskGraphNode1;
        TestingTaskGraphNode taskGraphNode2;
        try
        {
            node1AdditionReturn = addNewNode("test1", "test1");
            node2AdditionReturn = addNewNode("test2", "test2");
            taskGraphNode1 = node1AdditionReturn.getKey();
            taskGraphNode2 = node2AdditionReturn.getKey();
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new task node that is supposed to be able to be added.");
            return;
        }

        assertThrows(ReferenceToUnExistantEdgeInTheGraph.class, () ->
        {
            mainGraphBuilder.removeEdge(1);
        });

        Long node1Id = taskGraphNode1.getGraphNodeInformation().getId();
        Long node2Id = taskGraphNode2.getGraphNodeInformation().getId();

        //add and edge node1 output 0 -> node2 input 1
        Long edge1Id;
        try
        {
            edge1Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 1);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }

        {
            //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
            assertEquals(0, taskGraphNode1.getInputs().size());
            assertEquals(1, taskGraphNode1.getOutputs().size());
            assertEquals(1, taskGraphNode2.getInputs().size());
            assertEquals(0, taskGraphNode2.getOutputs().size());
            PipeGraphEdge edgeConnectedToNode1 = taskGraphNode1.getOutputWithNumber(0);
            PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

            assertNotEquals(null, edgeConnectedToNode1);
            assertNotEquals(null, edgeConnectedToNode2);
            //assert that the output edge of one node is the same as the input edge of the other
            assertEquals(edgeConnectedToNode1, edgeConnectedToNode2);

            //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
            PipeGraphEdge edgeCreated = edgeConnectedToNode1;

            //Check MainGraphBuilder's HashMaps
            assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
            assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeHashMap.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

            //Check HashMap contents
            {
                PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge1Id);
                assertEquals(edgeCreated, edgeOnMap);
                TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge1Id);
                assertEquals(taskGraphNode1, upstreamNode);
                Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge1Id);
                assertEquals(0, upstreamOutputNumber);
                TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge1Id);
                assertEquals(taskGraphNode2, downstreamNode);
                Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge1Id);
                assertEquals(1, downstreamOutputNumber);
            }
        }


        assertThrows(ReferenceToUnExistantEdgeInTheGraph.class, () ->
        {
            mainGraphBuilder.removeEdge(5);
        });

        try
        {
            mainGraphBuilder.removeEdge(edge1Id);
        } catch (InconsistentInternalInformationOnGraphBuilder inconsistentInternalInformationOnGraphBuilder)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that the internal information is inconsistent.");
        } catch (ReferenceToUnExistantEdgeInTheGraph referenceToUnExistantEdgeInTheGraph)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that there edge does not exist.");
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that there edge does not exist.");
        }

        {
            //assert hash maps of the nodes are set properly and that the they no longer contain the edge
            assertEquals(0, taskGraphNode1.getInputs().size());
            assertEquals(0, taskGraphNode1.getOutputs().size());
            assertEquals(0, taskGraphNode2.getInputs().size());
            assertEquals(0, taskGraphNode2.getOutputs().size());
            PipeGraphEdge edgeConnectedToNode1 = taskGraphNode1.getOutputWithNumber(0);
            PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

            assertEquals(null, edgeConnectedToNode1);
            assertEquals(null, edgeConnectedToNode2);

            //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
            PipeGraphEdge edgeCreated = edgeConnectedToNode1;

            //Check MainGraphBuilder's HashMaps
            assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
            assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());
        }




        //Re-add edge and check everything works as expected

        //add and edge node1 output 0 -> node2 input 1
        try
        {
            edge1Id = mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, 0, 1);
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when adding new edge that is supposed to be able to be added.");
            return;
        }

        {
            //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
            assertEquals(0, taskGraphNode1.getInputs().size());
            assertEquals(1, taskGraphNode1.getOutputs().size());
            assertEquals(1, taskGraphNode2.getInputs().size());
            assertEquals(0, taskGraphNode2.getOutputs().size());
            PipeGraphEdge edgeConnectedToNode1 = taskGraphNode1.getOutputWithNumber(0);
            PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

            assertNotEquals(null, edgeConnectedToNode1);
            assertNotEquals(null, edgeConnectedToNode2);
            //assert that the output edge of one node is the same as the input edge of the other
            assertEquals(edgeConnectedToNode1, edgeConnectedToNode2);

            //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
            PipeGraphEdge edgeCreated = edgeConnectedToNode1;

            //Check MainGraphBuilder's HashMaps
            assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
            assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeHashMap.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
            assertEquals(1, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());

            //Check HashMap contents
            {
                PipeGraphEdge edgeOnMap = mainGraphBuilder.pipeGraphEdgeHashMap.get(edge1Id);
                assertEquals(edgeCreated, edgeOnMap);
                TaskGraphNode upstreamNode = mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.get(edge1Id);
                assertEquals(taskGraphNode1, upstreamNode);
                Integer upstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.get(edge1Id);
                assertEquals(0, upstreamOutputNumber);
                TaskGraphNode downstreamNode = mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.get(edge1Id);
                assertEquals(taskGraphNode2, downstreamNode);
                Integer downstreamOutputNumber = mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.get(edge1Id);
                assertEquals(1, downstreamOutputNumber);
            }
        }

        //Remove edge again to check everything works as expected
        try
        {
            mainGraphBuilder.removeEdge(edge1Id);
        } catch (InconsistentInternalInformationOnGraphBuilder inconsistentInternalInformationOnGraphBuilder)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that the internal information is inconsistent.");
        } catch (ReferenceToUnExistantEdgeInTheGraph referenceToUnExistantEdgeInTheGraph)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that there edge does not exist.");
        } catch (GraphBuildingException e)
        {
            fail("Exception thrown when attempting to remove existing edge in the graph. Method removeEdge has thrown exception indicating that there edge does not exist.");
        }

        {
            //assert hash maps of the nodes are set properly and that the they contain a new edge on the correct input number
            assertEquals(0, taskGraphNode1.getInputs().size());
            assertEquals(0, taskGraphNode1.getOutputs().size());
            assertEquals(0, taskGraphNode2.getInputs().size());
            assertEquals(0, taskGraphNode2.getOutputs().size());
            PipeGraphEdge edgeConnectedToNode1 = taskGraphNode1.getOutputWithNumber(0);
            PipeGraphEdge edgeConnectedToNode2 = taskGraphNode2.getInputWithNumber(1);

            assertEquals(null, edgeConnectedToNode1);
            assertEquals(null, edgeConnectedToNode2);

            //no matter which one we pick, because they are the same object, let's pick the edgeConnectedToNode1
            PipeGraphEdge edgeCreated = edgeConnectedToNode1;

            //Check MainGraphBuilder's HashMaps
            assertEquals(0, mainGraphBuilder.fullyConnectedGraphNodes.size());
            assertEquals(2, mainGraphBuilder.unconnectedGraphNodes.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeHashMap.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToUpStreamOutputNumber.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamNode.size());
            assertEquals(0, mainGraphBuilder.pipeGraphEdgeIdToDownStreamInputNumber.size());
        }

    }
}