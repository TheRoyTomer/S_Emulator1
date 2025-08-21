import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.S_Type.Assignment;
import Engine.Instructions_Types.S_Type.Constant_Assignment;
import Engine.Instructions_Types.S_Type.Goto_Label;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Programs.Context;
import Engine.Programs.Program;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        /*//V_Gets_Twice_V2
        Program program = V_Gets_Twice_V2(5, 25);
        program.execute();
        System.out.println("OutPut: " + Variable.OUTPUT.getValue());*/


        //FirstExpansionTest
        FirstExpansionTest(2);

    }

    public static Program V_Gets_Twice_V2(long v1Value, long v2Value)
    {

        Program program = new Program("V_Gets_Twice_V2");

        Variable x1 = new VariableImplement(VariableType.INPUT, 1);
        Variable x2 = new VariableImplement(VariableType.INPUT, 2);
        Variable z1 = new VariableImplement(VariableType.WORK,  1);
        Variable z_FAKE = new VariableImplement(VariableType.WORK,  1);

        LabelInterface l1 = new Label_Implement("L1");
        LabelInterface l2 = new Label_Implement("L2");
        LabelInterface l3 = new Label_Implement("L3");


        program.addInstruction(new Constant_Assignment(program.getContext(),x1, v1Value));
        program.addInstruction(new Constant_Assignment(program.getContext(),x2, v2Value));



        program.addInstruction(new JNZ(program.getContext(),x2 ,l1));
        program.addInstruction(new Goto_Label(program.getContext(), z_FAKE, FixedLabels.EXIT));
        program.addInstruction(new Decrease(program.getContext(),x1, l1));
        program.addInstruction(new JNZ(program.getContext(),x1 ,l1));
        program.addInstruction(new Increase(program.getContext(),x1, l2));
        program.addInstruction(new Increase(program.getContext(),x1));
        program.addInstruction(new Increase(program.getContext(),z1));
        program.addInstruction(new Decrease(program.getContext(),x2));
        program.addInstruction(new JNZ(program.getContext(), x2, l2));
        program.addInstruction(new Decrease(program.getContext(),z1, l3));
        program.addInstruction(new Increase(program.getContext(),x2));
        program.addInstruction(new JNZ(program.getContext(), z1, l3));

        program.addInstruction(new Assignment(program.getContext(), Variable.OUTPUT, x1));

        return program;
    }

    public static void FirstExpansionTest(int degree)
    {
        Program program = new Program("FirstExpansionTest");
        Variable x2 = new VariableImplement(VariableType.INPUT, 2);
        Variable z1 = new VariableImplement(VariableType.WORK,  1);
        Variable z_FAKE = new VariableImplement(VariableType.WORK,  12);


        LabelInterface l6 = new Label_Implement("L6");
        LabelInterface l7 = new Label_Implement("L7");


        program.initVarMap(x2, z1, z_FAKE);
        program.initLabelMap(l6, l7);


        List<Instruction> instructions = new ArrayList<Instruction>(List.of(
        new Assignment(program.getContext(), z1, x2, l6),
        new Increase(program.getContext(), z1),
        new Constant_Assignment(program.getContext(), Variable.OUTPUT, l7, 3)
        ));

        program.initProgram(instructions);




        //List<Instruction> res = program.expand(degree);
        //program.getNewExpandInstructionList(degree);
        program.expand(degree);
        System.out.println(program);
        System.out.println(program.getProgramRepresentation());

    }


}