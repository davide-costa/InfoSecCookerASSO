package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ReadFromFile extends SourcesNodeTask
{
    private String filepath;


    public ReadFromFile(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations,
                        String filepath)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.filepath = filepath;
    }

    @Override
    public ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException
    {
        ArrayList<InfoSecGraphData> outputDataArray = new ArrayList<>();
        outputDataArray.add(computeLoadFileFromSystem(filepath));
        return outputDataArray;
    }

    protected abstract InfoSecGraphData computeLoadFileFromSystem(String fileName) throws ComputationException;


    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return getInputs().size() == 0;
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return getOutputs().size() == 1;
    }
}
