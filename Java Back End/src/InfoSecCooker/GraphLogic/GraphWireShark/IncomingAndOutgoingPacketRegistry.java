package InfoSecCooker.GraphLogic.GraphWireShark;

public class IncomingAndOutgoingPacketRegistry
{
    PacketRegistry incomingPacketRegistry;
    PacketRegistry outgoingPacketRegistry;

    public IncomingAndOutgoingPacketRegistry(PacketRegistry incomingPacketRegistry, PacketRegistry outgoingPacketRegistry)
    {
        this.incomingPacketRegistry = incomingPacketRegistry;
        this.outgoingPacketRegistry = outgoingPacketRegistry;
    }

    public PacketRegistry getIncomingPacketRegistry()
    {
        return incomingPacketRegistry;
    }

    public PacketRegistry getOutgoingPacketRegistry()
    {
        return outgoingPacketRegistry;
    }
}
