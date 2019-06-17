package InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class TaskRunningRegistry
{
    List<TickEntry> registry = Collections.synchronizedList(new ArrayList<TickEntry>());
    Semaphore semaphore = new Semaphore(1);

    public void registerTickEntry(TickEntry tickEntry) throws InterruptedException
    {
        semaphore.acquire();
        registry.add(tickEntry);
        semaphore.release();
    }

    public void clearTickEntryRegistry() throws InterruptedException
    {
        semaphore.acquire();
        registry.clear();
        semaphore.release();
    }

    public List<TickEntry> getRegistry() throws InterruptedException
    {
        semaphore.acquire();
        List<TickEntry> copy = new ArrayList<TickEntry>(registry);
        semaphore.release();

        return copy;
    }

    public List<TickEntry> getAndClearRegistry() throws InterruptedException
    {
        semaphore.acquire();
        List<TickEntry> copy = new ArrayList<TickEntry>(registry);
        registry.clear();
        semaphore.release();

        return copy;
    }
}
