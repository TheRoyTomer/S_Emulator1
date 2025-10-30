package Server_UTILS;

import Engine.Programs.Function;
import Engine.Programs.Program;
import Out.FunctionInfoDTO;

public class FunctionHolderWrapper
{
    private final Function function;
    private final String uploader;
    private final String relatedProgramName;

    private int numExecutions;
    private int sumCreditCost;

    public FunctionHolderWrapper(Function function, String uploader, String relatedProgramName)
    {
        this.function = function;
        this.uploader = uploader;
        this.relatedProgramName = relatedProgramName;
        this.numExecutions = 0;
        this.sumCreditCost = 0;
    }

    public Function getFunction()
    {
        return function;
    }

    public String getUploader()
    {
        return uploader;
    }

    public String getRelatedProgramName()
    {
        return relatedProgramName;
    }

    public int getNumExecutions()
    {
        return numExecutions;
    }

    public int getSumCreditCost()
    {
        return sumCreditCost;
    }

    public int getAvgCreditCost()
    {
        if (numExecutions == 0) {return 0;}
        return sumCreditCost / numExecutions;
    }

    public void IncreaseExecutions()
    {
        numExecutions++;
    }

    public void addTotalCreditCost(int credit)
    {
        sumCreditCost += credit;
    }

    public FunctionInfoDTO convertToProgramInfoDTO()
    {
        return new FunctionInfoDTO(function.getName(),
                this.uploader,
                function.getInstructions().size(),
                function.getMaxDegree(),
                this.getAvgCreditCost(),
                this.relatedProgramName);
    }
}