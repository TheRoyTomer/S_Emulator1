package Labels;

public class Label_Implement implements LabelInterface
{
    private final String name;

    public Label_Implement(String name)
    {
        this.name = name;
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
