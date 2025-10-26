package Server_UTILS;

import Engine.Programs.Function;
import Engine.Programs.Program;
import Out.FunctionInfoDTO;

public class FunctionHolderWrapper
{
    private final Function function;
    private final String uploader;
    private final String relatedProgramName;

    public FunctionHolderWrapper(Function function, String uploader, String relatedProgramName)
    {
        this.function = function;
        this.uploader = uploader;
        this.relatedProgramName = relatedProgramName;
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

    public FunctionInfoDTO convertToProgramInfoDTO()
    {
        return new FunctionInfoDTO(function.getName(),
                this.uploader,
                function.getInstructions().size(),
                function.getMaxDegree(),
                this.relatedProgramName);
    }
}
