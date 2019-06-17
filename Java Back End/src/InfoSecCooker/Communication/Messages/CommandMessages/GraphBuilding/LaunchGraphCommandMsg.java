package InfoSecCooker.Communication.Messages.CommandMessages.GraphBuilding;

public class LaunchGraphCommandMsg extends GraphBuildingCommandMsg
{
    Boolean runDespiteErrors;

    public LaunchGraphCommandMsg(Boolean runDespiteErrors)
    {
        this.runDespiteErrors = runDespiteErrors;
    }

    public Boolean getRunDespiteErrors()
    {
        return runDespiteErrors;
    }
}
