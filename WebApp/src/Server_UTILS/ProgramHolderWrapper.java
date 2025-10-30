package Server_UTILS;

import Engine.Programs.Program;
import Out.ProgramInfoDTO;

public class ProgramHolderWrapper
{
    private final Program program;
    private final String uploader;
    private int numExecutions;
    private int sumCreditCost;

    public ProgramHolderWrapper(Program program, String uploader)
    {
        this.program = program;
        this.uploader = uploader;
        this.numExecutions = 0;
        this.sumCreditCost = 0;
    }

    public Program getProgram()
    {
        return program;
    }

    public String getUploader()
    {
        return uploader;
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
        if (this.numExecutions == 0) {return 0;}
        return this.sumCreditCost / this.numExecutions;
    }

    public void IncreaseExecutions()
    {
        numExecutions++;
    }

    public void addTotalCreditCost(int credit)
    {
        sumCreditCost += credit;
    }

    public ProgramInfoDTO convertToProgramInfoDTO()
    {
        return new ProgramInfoDTO(
                program.getName(),
                this.uploader,
                program.getInstructions().size(),
                program.getMaxDegree(),
                this.getAvgCreditCost(),
                this.numExecutions);
    }
}
