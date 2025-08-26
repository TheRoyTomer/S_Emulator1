package Engine.Programs;

import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.Vars.Variable;
import EngineObject.InstructionDTO;
import EngineObject.VariableDTO;
import Out.ViewResultDTO;

import java.util.ArrayList;
import java.util.List;

public class Viewer
{
    private Program program;
    private Context context;

    public Viewer(Program program)
    {
        this.program = program;
        this.context = program.getContext();
    }

    public ViewResultDTO viewOriginalProgram()
    {

    }

    public InstructionDTO InstructionToDTO(Instruction inst)
    {
        if (inst == null) {return null;}
        return new InstructionDTO(
                inst.getLineIndex(),
                inst instanceof S_Instruction,
                inst.getLabel().getLabelRepresentation(),
                inst.getCommandRep(),
                InstructionToDTO(inst.getHolder()),
                inst.getCycles());
    }

    public VariableDTO VariableToDTO(Variable v)
    {
        return new VariableDTO(
                v.getVariableRepresentation(),
                context.getVarValue(v));
    }



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
}
