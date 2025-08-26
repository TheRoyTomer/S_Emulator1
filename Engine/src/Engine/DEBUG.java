/*
package Engine;

import Engine.Instructions_Types.Instruction;
import Engine.JAXB.generated.XML_Reader;
import Engine.Programs.Context;
import Engine.Programs.Viewer;
import Engine.Programs.Loader;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DEBUG
{
    Program program;
    private String name;
    private final Context context;
    private List<Instruction> instructions;*/
/* = new ArrayList<>();*//*

    private List<Instruction> ExpandedInstructions;

    private Loader loader;
    private Viewer displayer;

    public DEBUG(Program program)
    {
        this.program = program;
        this.instructions = program.getInstructions();
        this.ExpandedInstructions = program.getExpandedInstructions();
        this.context = program.getContext();
        this.name = program.getName();
        this.displayer = new Viewer(program);
        this.loader = new Loader(program);
    }


    //ToDO: just for debug, delete later.
    public static void main(String[] args)
    {

        Program program = new Program();
        DEBUG debug = new DEBUG(program);

        Loader loader = new Loader(program);
        String PATH = "C:\\Users\\beatl\\Desktop\\minus (1).xml";
        String PATH2 = "C:\\Users\\beatl\\Downloads\\successor.xml";

        File f = new File(PATH);
        XML_Reader reader = new XML_Reader(f);
        loader.loadFromReader(reader);

        //Change Here
        int degree = 0;
        List<Long> inputs = new ArrayList<>();
        inputs.add(8L);
        inputs.add(3L);


        debug.YuvalLovesRunningPrograms(degree, inputs);
        //debug.YuvalLovesDisplayingPrograms(degree);
    }



    //ToDO: just for debug, delete later.
    public void YuvalLovesRunningPrograms(int degree, List<Long> inputs)
    {

        */
/*Variable X1 = new VariableImplement(VariableType.INPUT, 1);
        Variable X2 = new VariableImplement(VariableType.INPUT, 2);
        context.setVarValue(X1, 8);
        context.setVarValue(X2, 3);*//*


        */
/*//*
/ToDo: for statistics, need in another way.
        List<PairDataStructure<Variable, Long>> inputs = new ArrayList<>();
        inputs.add(new PairDataStructure<Variable, Long>(X1, 8L));
        inputs.add(new PairDataStructure<Variable, Long>(X2, 3L));*//*

        program.initProgram();

         int TotalCycles = program.Run(degree, inputs);
         displayer.displayEndOfRun(TotalCycles, degree);

        //program.initProgram();
        //context.ddd();

        //System.out.println(instructionsStats.getStatisticsListRepresentation());

    }

    //ToDO: just for debug, delete later.
    public void YuvalLovesDisplayingPrograms(int degree)
    {
        Loader loader = new Loader(program);
        String PATH = "C:\\Users\\beatl\\Desktop\\minus (1).xml";
        //String PATH2 = "C:\\Users\\beatl\\Downloads\\successor.xml";

        File f = new File(PATH);
        XML_Reader reader = new XML_Reader(f);
        loader.loadFromReader(reader);
        Variable X1 = new VariableImplement(VariableType.INPUT, 1);
        //Variable X2 = new VariableImplement(VariableType.INPUT, 2);
        context.setVarValue(X1, 100);
        //context.setVarValue(X2, 3);

        */
/*//*
/ToDo: for statistics, need in another way.
        List<PairDataStructure<Variable, Long>> inputs = new ArrayList<>();
        inputs.add(new PairDataStructure<Variable, Long>(X1, 100L));*//*

        //inputs.add(new PairDataStructure<Variable, Long>(X2, 3L));

        if (degree > 0)
        {
            program.expand(degree);
        }

        program.initProgram();

        displayer.displayProgram(degree);
    }


}
*/
