package InfoSecCooker.GraphNodes;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class GraphNodeInformation
{
    static AtomicLong curr_id = new AtomicLong(0);

    long id;
    String type;
    String name;

    public GraphNodeInformation(String type, String name)
    {
        this.id = curr_id.incrementAndGet();
        this.type = type;
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNodeInformation that = (GraphNodeInformation) o;
        return this.id == that.id;
    }

    @Override
    public String toString()
    {
        return "GraphNodeInformation{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
