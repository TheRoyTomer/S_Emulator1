package Engine.Vars;

import java.util.Objects;

public class VariableImplement implements Variable
{
    private final VariableType type;
    private final int serial;

    public VariableImplement(VariableType type, int serial)
    {
        this.type = type;
        this.serial = serial;
    }

    @Override
    public String toString()
    {
        return type.getVariableRepresentation(serial);
    }

    @Override
    public int compareTo(Variable o)
    {
        int r1 = rank(this.type);
        int r2 = rank(o.getVariableType());
        int cmp = Integer.compare(r1, r2);
        if (cmp != 0) return cmp;
        return Integer.compare(this.serial, o.getSerial());
    }

    private static int rank(VariableType t)
    {
        return switch (t) {
            case OUTPUT -> 0; // Y
            case INPUT -> 1; // X
            case WORK -> 2; // Z
            default -> Integer.MAX_VALUE; // others last
        };
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof Variable)) return false;
        Variable other = (Variable) obj;
        return this.serial == other.getSerial() && this.type == other.getVariableType();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, serial);
    }


    @Override
    public String getVariableRepresentation()
    {
        return type.getVariableRepresentation(serial);
    }

    @Override
    public int getSerial()
    {
        return serial;
    }

    @Override
    public VariableType getVariableType()
    {
        return type;
    }
}
