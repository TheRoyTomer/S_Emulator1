package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Labels.LabelInterface;
import Engine.Vars.Variable;
import EngineObject.InstructionDTO;
import EngineObject.VariableDTO;
import Out.ViewResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


        List<VariableDTO> Xlist = context.getAll_X_InList(currInstructions)
                .stream()
                .map(Convertor::VariableToDTO)
                .toList();

        List<String> Llist = context.getAll_L_InList(currInstructions)
                .stream()
                .map(LabelInterface::getLabelRepresentation)
                .toList();



        return new ViewResultDTO(
                program.getName(),
                Convertor.convertInstructionsListToDTO(currInstructions),
                Xlist,
                Llist);
    }



/*


    //***ToDO: Transfer To UI****
    public void displayProgram(int degree)
    {
        List<Instruction> instructions = program.getProperListByDegree(degree);
        System.out.println(program.getName() + "\n");
        System.out.println(context.getAll_X_InList(instructions) + "\n");
        System.out.println(context.getAll_L_InList(instructions) + "\n");
        System.out.println(this.getProgramRepresentation(instructions) + "\n");
    }

    public void displayEndOfRun(int cycles, int degree)
    {
        List<Instruction> instructions = program.getProperListByDegree(degree);
        System.out.println(program.getName());
        System.out.println(this.getProgramRepresentation(instructions));
        PrintVarWithValue(Variable.OUTPUT);
        context.getAllVarsInList(instructions)
                .forEach(this::PrintVarWithValue);
        System.out.println("Total cycles: " + cycles);
    }

    public void PrintVarWithValue(Variable variable)
    {
        System.out.println(variable.getVariableRepresentation()
                            + " = "
                            + context.getVarValue(variable));
    }

    public String getProgramRepresentation(List <Instruction> instructions)
    {

        StringBuilder res = new StringBuilder();
        for (Instruction c : instructions)
        {
            Instruction inst = c;
            while(inst != null)
            {
                res.append(inst.getInstructionRepresentation());
                inst = inst.getHolder();
                if (inst != null)
                {
                    res.append(" >> ");
                }
            }
            res.append("\n");
        }
        return res.toString();
    }
*/
}
