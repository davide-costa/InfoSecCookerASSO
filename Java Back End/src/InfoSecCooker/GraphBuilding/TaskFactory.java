package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphNodes.SpecificNodeImplementations.Files.ToFile;
import InfoSecCooker.GraphNodes.TaskGraphNode;

public class TaskFactory
{
    public static TaskGraphNode buildTaskGraphNodeWithName(String name)
    {
        //TODO i think this is impossible to apply the conversion "code array to data array"
        //(without implementing a copy method in every task graph node derived class)
        switch(name)
        {
            case "ToFile":
                return new ToFile(null, null, null, null, null, null);
            default:
                return null;
        }
    }
}
