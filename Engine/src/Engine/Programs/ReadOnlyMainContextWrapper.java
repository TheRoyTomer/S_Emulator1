package Engine.Programs;

import Engine.Vars.Variable;

public class ReadOnlyMainContextWrapper
{
    Context mainContext;

    public ReadOnlyMainContextWrapper(Context mainContext)
    {
        this.mainContext = mainContext;
    }

    public long getVarValueFromMainContext(Variable var)
    {
        return mainContext.getVarValue(var);
    }

    public boolean isVarExistInMainContext(Variable var)
    {
        return mainContext.isVariableExistInMap(var);
    }
}
