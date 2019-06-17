package InfoSecCooker.Communication;

import InfoSecCooker.GraphLogic.GraphController;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CommunicationStaticData
{
    private static HashMap<Long, RunTimeConfigurations> runTimeConfigurationsOfSessionId;
    private static HashMap<Long, CommandDispatcher> commandDispatcherOfSessionId;
    private static HashMap<Long, GraphController> graphControllerOfSessionId;

    private static AtomicLong currSessionId = new AtomicLong(0);

    static
    {
        runTimeConfigurationsOfSessionId = new HashMap<>();
        commandDispatcherOfSessionId = new HashMap<>();
        graphControllerOfSessionId = new HashMap<>();
    }

    public static long startNewSessionAndGetId()
    {
        long sessionId = currSessionId.incrementAndGet();
        CommandDispatcher commandDispatcher = new CommandDispatcher(sessionId);

        runTimeConfigurationsOfSessionId.put(sessionId, new RunTimeConfigurations());
        commandDispatcherOfSessionId.put(sessionId, commandDispatcher);
        graphControllerOfSessionId.put(sessionId, commandDispatcher.getGraphController());

        return sessionId;
    }

    public static RunTimeConfigurations getRunTimeConfigurationsOfSessionId(long sessionId)
    {
        return runTimeConfigurationsOfSessionId.get(sessionId);
    }

    public static CommandDispatcher getCommandDispatcherOfSessionId(long sessionId)
    {
        return commandDispatcherOfSessionId.get(sessionId);
    }

    public static GraphController getGraphControllerOfSessionId(long sessionId)
    {
        return graphControllerOfSessionId.get(sessionId);
    }
}
