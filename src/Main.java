import Instructions_Types.B_Type.Decrease;
import Instructions_Types.B_Type.Increase;
import Instructions_Types.B_Type.JNZ;
import Instructions_Types.S_Type.Assignment;
import Instructions_Types.S_Type.Constant_Assignment;
import Instructions_Types.S_Type.Goto_Label;
import Labels.FixedLabels;
import Labels.LabelInterface;
import Labels.Label_Implement;
import Programs.Program;
import Vars.Variable;
import Vars.X_Var;
import Vars.Z_Var;

import static Programs.Program.outPut;

public class Main
{
    public static void main(String[] args)
    {
        Program program = V_Gets_Twice_V2(5, 25);
        program.execute();
        System.out.println("OutPut: " + outPut.getValue());
    }

    public static Program V_Gets_Twice_V2(long v1Value, long v2Value)
    {
        Program program = new Program();
        Variable x1 = new X_Var(program, 1);
        Variable x2 = new X_Var(program, 2);
        Variable z1 = new Z_Var(program,  1);

        LabelInterface l1 = new Label_Implement("L1");
        LabelInterface l2 = new Label_Implement("L2");
        LabelInterface l3 = new Label_Implement("L3");


        program.addInstruction(new Constant_Assignment(program,x1, v1Value));
        program.addInstruction(new Constant_Assignment(program,x2, v2Value));



        program.addInstruction(new JNZ(program,x2 ,l1));
        program.addInstruction(new Goto_Label(program, FixedLabels.EXIT));
        program.addInstruction(new Decrease(program,x1, l1));
        program.addInstruction(new JNZ(program,x1 ,l1));
        program.addInstruction(new Increase(program,x1, l2));
        program.addInstruction(new Increase(program,x1));
        program.addInstruction(new Increase(program,z1));
        program.addInstruction(new Decrease(program,x2));
        program.addInstruction(new JNZ(program, x2, l2));
        program.addInstruction(new Decrease(program,z1, l3));
        program.addInstruction(new Increase(program,x2));
        program.addInstruction(new JNZ(program, z1, l3));

        program.addInstruction(new Assignment(program, Program.outPut, x1));

        return program;
    }
}