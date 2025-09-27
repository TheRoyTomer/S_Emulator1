package EngineObject;


public record VariableDTO(
        String name,
        long value)
{
    public String getVarRepresentation()
    {
        return this.name();
    }

    public String getVarAndValueRepresentation()
    {
        return String.format("%s = %d", this.name, this.value);
    }

    public int getSerial()
    {
        if (name.length() <= 1) return 0;
        return Integer.parseInt(name.substring(1));
    }


}
