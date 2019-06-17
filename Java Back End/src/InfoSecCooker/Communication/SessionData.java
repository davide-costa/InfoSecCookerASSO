package InfoSecCooker.Communication;

import InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding.GraphBuildingCommandMsg;

import java.util.ArrayList;

public class SessionData
{
    private String frontEndData;
    private ArrayList<GraphBuildingCommandMsg> graphBuildingCommandMsgs; //command pattern

    public SessionData(String frontEndData, ArrayList<GraphBuildingCommandMsg> graphBuildingCommandMsgs)
    {
        this.frontEndData = frontEndData;
        this.graphBuildingCommandMsgs = graphBuildingCommandMsgs;
    }
}
