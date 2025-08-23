package Engine.Programs;

import Engine.Instructions_Types.Calculable;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Instruction;
import Engine.JAXB.generated.XML_Reader;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.*;

import java.io.File;
import java.util.*;

public class Program /*implements Calculable*/
{
    private String name;
    private final Context context;
    private int maxDegree;
    private int cycles;
    private List<Instruction> instructions;/* = new ArrayList<>();*/
    private List<Instruction> ExpandedInstructions;

    public Program()
    {
        //ToDO: what else?
        this.context = new Context();
    }

    public Context getContext()
    {
        return context;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void InitInstructionsExpensions()
    {
        instructions.stream()
                .filter(instruction -> instruction instanceof S_Instruction)
                .map(instruction -> (S_Instruction) instruction)
                .forEach(S_Instruction::getSingleExpansion);
    }

    public void initProgram()
    {
        InitInstructionsExpensions();
        this.maxDegree = this.calcMaxDegree();
        this.cycles = this.calcCycles();
    }

    //ToDO: just for debug, delete later.
    public void YuvalLoveDebug()
    {
        Loader loader = new Loader(this);
        String PATH = "C:\\Users\\beatl\\Desktop\\minus (1).xml";
        //String PATH = "C:\\Users\\beatl\\Desktop\\my_minus (1).xml";

        File f = new File(PATH);
        XML_Reader reader = new XML_Reader(f);
        loader.loadFromReader(reader);
        int counter =1;

        context.setVarValue(new VariableImplement(VariableType.INPUT, 1), 8);
        context.setVarValue(new VariableImplement(VariableType.INPUT, 2), 3);
        this.initProgram();
        Run(1);

       /* String res = "";
        for (Instruction c : this.instructions)
        {
            if(c == null) {res += "NULL\n";}
            else
            {
            res += String.format("#<%d> %s \n", counter++, c.getInstructionRepresentation());
            }
        }

        System.out.println(res);*/
    }

    //ToDO: just for debug, delete later.
    public static void main(String[] args)
    {
        Program program = new Program();
        program.YuvalLoveDebug();
    }

    //ToDo:NEED?
    public void addInstruction(Instruction instruction)
    {
        instructions.add(instruction);
    }

    @Override
    public String toString()
    {
        return "Program{" +
                "maxDegree=" + maxDegree +
                ", cycles=" + cycles + ", " +
                context.toString() +
                '}';
    }

    public String getProgramRepresentation()
    {
        int counter =1;
        String res = "";
        for (Instruction c : this.ExpandedInstructions)
        {
            res += String.format("#<%d> %s \n", counter++, c.getInstructionRepresentation());
        }
        return res;
    }

    public void setInstructions(List<Instruction> instructions)
    {
        this.instructions = instructions;
    }
    //@Override
    public int calcMaxDegree()
    {
        return instructions.stream()
                .mapToInt(Instruction::calcMaxDegree)
                .max()
                .orElse(0);
    }

    public int calcCycles()
    {
        return instructions.stream()
                .mapToInt(Instruction::getCycles)
                .sum();
    }


    //@Override
    public LabelInterface execute()
    {
        //context.collectVarsAndIndexLabels(this.ExpandedInstructions);
        LabelInterface label = null;
        for (long PC = 0; PC < ExpandedInstructions.size(); ) {
            label = ExpandedInstructions.get((int) PC).execute();
            if (label == FixedLabels.EMPTY) {
                PC++;
            } else if (label == FixedLabels.EXIT || !context.isExistInMapL(label)) {
                break;
            } else {
                PC = context.getFromMapL((Label_Implement) label);
            }
        }
        return FixedLabels.EXIT;
    }


    public List<Instruction> expand(int degree)
    {
        List<Instruction> res = new ArrayList<>();
        if (degree == 0)
        {
            return this.instructions;
        }
        for (Instruction instruction : instructions)
        {
            res.addAll(instruction.expand(degree));
        }
        return res;
    }

    //ToDo: is needed?
    /*public void initVarMap(Variable... vars)
    {
        for (Variable var : vars) {context.setVarValue(var,0);}
    }*/

    //ToDo: is needed?
    /*public void initLabelMap(LabelInterface... labels)
    {
        for (LabelInterface label : labels) {
            if (label instanceof Label_Implement)
            {
                context.setInMapL((Label_Implement) label, 0);
            }
        }
    }*/

    public void Run(int degree)
    {
        this.ExpandedInstructions = expand(degree);
        context.collectVarsAndIndexLabels(this.ExpandedInstructions);
        execute();
        displayEndOfRun();

    }

    public void displayEndOfRun()
    {
        System.out.println(this.getProgramRepresentation());

        System.out.println(Variable.OUTPUT.getVariableRepresentation()
                + " = " + context.getVarValue(Variable.OUTPUT));

        List<Variable> usedVarsInOrder = context.getUsedVarsInOrder();
        usedVarsInOrder.forEach(var ->
                System.out.println(
                        var.getVariableRepresentation()
                        + " = "
                        + context.getVarValue(var)));

        System.out.println("Total Cycles: " +
                ExpandedInstructions.stream()
                .mapToInt(Instruction::getCycles)
                .sum());
    }


}





