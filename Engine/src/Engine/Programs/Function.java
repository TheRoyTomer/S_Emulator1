package Engine.Programs;

import Engine.Vars.Variable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Function extends Program
{
    String userString;

    public Function(String userString)
    {
        super();
        this.userString = userString;
    }

    public Function duplicateFunction()
    {
        Function newF = new Function(this.userString);
        newF.setName(this.getName());
        newF.setMaxDegree(this.getMaxDegree());

        newF.setInstructions(this.getInstructions().stream()
                .map(instruction -> duplicateInstruction(instruction, newF.getContext()))
                .collect(Collectors.toList()));

        newF.setFunctions(this.getFunctions());


        return newF;
    }

    public String getUserString()
    {
        return this.userString;
    }

    public String changeOneArgNames(String str, Map<Variable, Variable> varChanges)
    {
        String newStr = String.valueOf(str);
        List<String> vars = Convertor.extractVariables(newStr);
        for(String var : vars)
        {
            Variable varToCheck = Convertor.convertStringToVar(var);
            if (varChanges.containsKey(varToCheck))
            {
                newStr = newStr.replaceAll(var, varChanges.get(varToCheck).getVariableRepresentation());
            }
        }
        return newStr;
    }

    //For getFuncArgsToDisplayIfExist()
    public String changeFuncArgsToPrint(String args)
    {
        String res = String.valueOf(args);
        String removepara = args.replace("(", "").replace(")", "");
        List<String> simpleArg = Arrays.stream(removepara.split(",")).toList();

        List<String> sortedByLength = simpleArg.stream()
                .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                .toList();

        for (String a : sortedByLength) {
            if (this.isNameFuncExistInMap(a))
            {
                res = res.replace(a,this.getFunctionByName(a).getUserString());
            }
        }
        return res;
    }

}
