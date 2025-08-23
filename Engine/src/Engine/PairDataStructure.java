package Engine;

public class PairDataStructure<F, S>
{
    private F first;
    private S second;

    public PairDataStructure(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    public F getFirst()
    {
        return first;
    }

    public void setFirst(F first)
    {
        this.first = first;
    }

    public S getSecond()
    {
        return second;
    }

    public void setSecond(S second)
    {
        this.second = second;
    }

    @Override
    public String toString()
    {
        return first + ": " + second;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof PairDataStructure<?, ?> pair)) return false;
        return (first == null ? pair.first == null : first.equals(pair.first)) &&
                (second == null ? pair.second == null : second.equals(pair.second));
    }

    @Override
    public int hashCode()
    {
        return (first == null ? 0 : first.hashCode()) ^
                (second == null ? 0 : second.hashCode());
    }
}
