package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Labels.LabelInterface;
import EngineObject.VariableDTO;
import Out.ViewResultDTO;
import java.util.List;

public class Viewer
{
    private final Program program;
    private final Context context;

    public Viewer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }



    public ViewResultDTO viewProgram(int degree)
    {

        List<Instruction> currInstructions = program.getProperListByDegree(degree);


        List<VariableDTO> Varlist = context.getAllVarsInList(currInstructions)
                .stream()
                .map(var -> Convertor.VariableToDTO(var, context))
                .toList();

        List<String> Llist = context.getAll_L_InList(currInstructions)
                .stream()
                .map(LabelInterface::getLabelRepresentation)
                .toList();



        return new ViewResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(currInstructions,  context),
                Varlist,
                Llist);
    }

}
