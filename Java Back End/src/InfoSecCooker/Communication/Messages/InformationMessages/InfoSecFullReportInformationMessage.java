package InfoSecCooker.Communication.Messages.InformationMessages;

import InfoSecCooker.GraphLogic.GraphWireShark.IncomingAndOutgoingPacketRegistry;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TickEntry;

import java.util.HashMap;
import java.util.List;

public class InfoSecFullReportInformationMessage
{
    List<TickEntry> tickRegistrySinceLastUpdate;
    List<TickEntry> tickRegistrySinceEver;
    HashMap<Long, String> taskNodeStates;
    HashMap<Long, IncomingAndOutgoingPacketRegistry> taskNodesIncomingAndOutgoingPacketRegistry;
    HashMap<Long, Long> currentTickCountOfAllNodes;

    public InfoSecFullReportInformationMessage(List<TickEntry> tickRegistrySinceLastUpdate, List<TickEntry> tickRegistrySinceEver, HashMap<Long, String> taskNodeStates, HashMap<Long, IncomingAndOutgoingPacketRegistry> taskNodesIncomingAndOutgoingPacketRegistry, HashMap<Long, Long> currentTickCountOfAllNodes)
    {
        this.tickRegistrySinceLastUpdate = tickRegistrySinceLastUpdate;
        this.tickRegistrySinceEver = tickRegistrySinceEver;
        this.taskNodeStates = taskNodeStates;
        this.taskNodesIncomingAndOutgoingPacketRegistry = taskNodesIncomingAndOutgoingPacketRegistry;
        this.currentTickCountOfAllNodes = currentTickCountOfAllNodes;
    }
}
