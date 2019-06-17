package InfoSecCooker.GraphLogic.GraphBuilding.Exceptions;

public class ReferenceToUnExistantElementInTheGraph extends GraphBuildingException
{
    protected long idReferenced;

    public ReferenceToUnExistantElementInTheGraph(long idReferenced)
    {
        this.idReferenced = idReferenced;
    }

    public long getIdReferenced()
    {
        return idReferenced;
    }
}
