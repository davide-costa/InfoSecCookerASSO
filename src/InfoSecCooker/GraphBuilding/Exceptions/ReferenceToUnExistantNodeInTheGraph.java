package InfoSecCooker.GraphBuilding.Exceptions;

public class ReferenceToUnExistantNodeInTheGraph extends GraphBuildingException
{
    private long idReferenced;

    public ReferenceToUnExistantNodeInTheGraph(long idReferenced)
    {
        this.idReferenced = idReferenced;
    }

    public long getIdReferenced()
    {
        return idReferenced;
    }
}
