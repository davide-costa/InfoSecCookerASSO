package InfoSecCooker.GraphLogic.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections.SortNodeTask;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Compression.Zip;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Hashing.SHA1;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking.DNSLookup;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking.Ping;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks.WriteTextToFile;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks.WriteToFile;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks.WriteToSocket;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources.FetchURL;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources.ReadBinaryFile;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources.ReadTextFile;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Text.Lowercase;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Text.TabsToSpaces;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Text.Uppercase;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;

public class TaskFactory
{
    RunTimeConfigurations runTimeConfigurations;

    public TaskFactory(RunTimeConfigurations runTimeConfigurations)
    {
        this.runTimeConfigurations = runTimeConfigurations;
    }

    public TaskGraphNode buildTaskGraphNodeWithName(String name)
    {
        switch(name)
        {
            case "SortNodeTask":
                return new SortNodeTask(new GraphNodeInformation("Collections", "SortNodeTask"), runTimeConfigurations);
            case "Zip":
                return new Zip(new GraphNodeInformation("Compression", "Zip"), runTimeConfigurations);
            case "SHA1":
                return new SHA1(new GraphNodeInformation("Hashing", "SHA1"), runTimeConfigurations);
            case "DNSLookup":
                return new DNSLookup(new GraphNodeInformation("Networking", "DNSLookup"), runTimeConfigurations);
            case "Ping":
                return new Ping(new GraphNodeInformation("Networking", "Ping"), runTimeConfigurations);
            case "Lowercase":
                return new Lowercase(new GraphNodeInformation("Text", "Lowercase"), runTimeConfigurations);
            case "UpperCase":
                return new Uppercase(new GraphNodeInformation("Text", "Uppercase"), runTimeConfigurations);
            case "TabsToSpaces":
                return new TabsToSpaces(new GraphNodeInformation("Text", "TabsToSpaces"), runTimeConfigurations);
            default:
                return null;
        }
    }

    public TaskGraphNode buildTaskSourceGraphNodeWithName(String taskName, String additionalInfo)
    {
        switch(taskName)
        {
            case "FetchURL":
                return new FetchURL(new GraphNodeInformation("Source", "FetchURL"), runTimeConfigurations, additionalInfo);
            case "ReadBinaryFile":
                return new ReadBinaryFile(new GraphNodeInformation("Source", "ReadBinaryFile"), runTimeConfigurations, additionalInfo);
            case "ReadTextFile":
                return new ReadTextFile(new GraphNodeInformation("Source", "ReadTextFile"), runTimeConfigurations, additionalInfo);
            default:
                return null;
        }
    }

    public TaskGraphNode buildTaskSinkGraphNodeWithName(String taskName, String additionalInfo)
    {
        switch(taskName)
        {
            case "WriteTextToFile":
                return new WriteTextToFile(new GraphNodeInformation("Sink", "WriteTextToFile"), runTimeConfigurations, additionalInfo);
            case "WriteToFile":
                return new WriteToFile(new GraphNodeInformation("Sink", "WriteToFile"), runTimeConfigurations, additionalInfo);
            case "WriteToSocket":
                String address = additionalInfo.substring(0, additionalInfo.indexOf(':'));
                int port = Integer.parseInt(additionalInfo.substring(additionalInfo.indexOf(':') + 1));
                return new WriteToSocket(new GraphNodeInformation("Sink", "WriteToSocket"), runTimeConfigurations, address, port);
            default:
                return null;
        }
    }

    public ArrayList<String> getTaskNamesList()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add(SortNodeTask.getTaskInformation());
        list.add(Zip.getTaskInformation());
        list.add(SHA1.getTaskInformation());
        list.add(DNSLookup.getTaskInformation());
        list.add(Ping.getTaskInformation());
        list.add(WriteTextToFile.getTaskInformation());
        list.add(WriteToFile.getTaskInformation());
        list.add(WriteToSocket.getTaskInformation());
        list.add(FetchURL.getTaskInformation());
        list.add(ReadBinaryFile.getTaskInformation());
        list.add(ReadTextFile.getTaskInformation());
        list.add(Lowercase.getTaskInformation());
        list.add(Uppercase.getTaskInformation());
        list.add(TabsToSpaces.getTaskInformation());
        return list;
    }
}
