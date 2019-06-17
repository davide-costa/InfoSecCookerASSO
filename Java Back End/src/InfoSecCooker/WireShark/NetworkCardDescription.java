package InfoSecCooker.WireShark;

import InfoSecCooker.GraphNodes.GraphNodeInformation;

public class NetworkCardDescription
{
    public enum Type {INPUT, OUTPUT};
    Type type;
    int number;
    GraphNodeInformation information;

    public NetworkCardDescription(Type type, int number, GraphNodeInformation information)
    {
        this.type = type;
        this.number = number;
        this.information = information;
    }
}
