package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphBuilding.TaskFactory;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

public class TestingTaskFactory extends TaskFactory
{
    private TaskGraphNode taskGraphNodeToReturnNext;

    public TestingTaskFactory(RunTimeConfigurations runTimeConfigurations)
    {
        super(runTimeConfigurations);
    }

    public void setTaskGraphNodeToReturnNext(TaskGraphNode taskGraphNodeToReturnNext)
    {
        this.taskGraphNodeToReturnNext = taskGraphNodeToReturnNext;
    }

    public TaskGraphNode buildTaskGraphNodeWithName(String name)
    {
        return taskGraphNodeToReturnNext;
    }
}
