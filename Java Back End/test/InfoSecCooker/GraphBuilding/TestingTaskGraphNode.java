package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

public class TestingTaskGraphNode extends TaskGraphNode
{
    boolean areNumberOfInputsInACorrectState;
    boolean areNumberOfOutputsInACorrectState;
    boolean destructorCalled = false;

    public TestingTaskGraphNode()
    {
        super(new GraphNodeInformation("test", "test"), null);
    }

    public TestingTaskGraphNode(GraphNodeInformation graphNodeInformation)
    {
        super(graphNodeInformation, new RunTimeConfigurations());
    }

    @Override
    public void tick() throws CollectionsException, NullDataReceivedFromGraphNodeAsInput, InterruptedException, ExpectedEdgeOnNodeInputButNotFound, InfoSecCookerRuntimeException
    {

    }

    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return areNumberOfInputsInACorrectState;
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return areNumberOfOutputsInACorrectState;
    }

    public void setAreNumberOfInputsInACorrectState(boolean areNumberOfInputsInACorrectState)
    {
        this.areNumberOfInputsInACorrectState = areNumberOfInputsInACorrectState;
    }

    public void setAreNumberOfOutputsInACorrectState(boolean areNumberOfOutputsInACorrectState)
    {
        this.areNumberOfOutputsInACorrectState = areNumberOfOutputsInACorrectState;
    }

    public boolean isDestructorCalled()
    {
        return destructorCalled;
    }

    public void resetDestructorCalledFlag()
    {
        this.destructorCalled = false;
    }

    @Override
    public void destructor()
    {
        super.destructor();
        destructorCalled = true;
    }
}
