package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Text;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking.DNSLookup;
import org.json.JSONObject;

import java.util.HashMap;

public class Uppercase extends TextNodeTask
{
    public Uppercase(GraphNodeInformation graphNodeInformation,
                     RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected String handleTextOperationRequestComputation(String string)
    {
        return string.toUpperCase();
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", Uppercase.class.getSimpleName())
                .put("numberInputs", getExpectedNumberInputs())
                .put("numberOutputs", getExpectedNumberOutputs())
                .put("nodeType", "Middle").toString();

        return jsonString;
    }

    protected static int getExpectedNumberInputs()
    {
        return 1;
    }

    protected static int getExpectedNumberOutputs()
    {
        return 1;
    }

    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return getInputs().size() == getExpectedNumberInputs();
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return getOutputs().size() == getExpectedNumberOutputs();
    }
}
