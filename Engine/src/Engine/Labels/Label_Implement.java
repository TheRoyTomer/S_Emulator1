package Engine.Labels;

import java.util.Objects;

public class Label_Implement implements LabelInterface, Comparable<Label_Implement>
{
    private final String name;

    public Label_Implement(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        Label_Implement that = (Label_Implement) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(Label_Implement other)
    {
        int thisNum = Integer.parseInt(this.name.substring(1));
        int otherNum = Integer.parseInt(other.name.substring(1));
        return Integer.compare(thisNum, otherNum);
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public String getLabelRepresentation()
    {
        return name;
    }


}
