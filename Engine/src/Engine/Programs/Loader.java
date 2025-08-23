package Engine.Programs;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Instruction;
import Engine.Instructions_Types.S_Type.*;
import Engine.JAXB.generated.SInstruction;
import Engine.JAXB.generated.SInstructionArgument;
import Engine.JAXB.generated.XML_Reader;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Loader
{
    private Program destProgram;
    private Context context;



    public Loader(Program destProgram)
    {
        this.destProgram = destProgram;
        this.context = destProgram.getContext();
    }

    public void loadFromReader(XML_Reader reader)
    {
        try {
            destProgram.setName(reader.getName());
            destProgram.setInstructions(convertToInstructionList(reader.getSInstructionList()));
        }  catch (RuntimeException e) {/*ToDo: handle Exception};*/}

    }

    public List<Instruction> convertToInstructionList(List<SInstruction> fromJAXB)
    {
        return fromJAXB.stream().map(this::convertToInstruction).toList();
    }


    public LabelInterface convertToLabelInterface(String labelName)
    {
        //TODO:ADD TO MAP?

        if(labelName == null) labelName = "";

        switch (labelName.toUpperCase()) {
            case "EXIT":
                return FixedLabels.EXIT;

            case "":
                return FixedLabels.EMPTY;
            default:
                return new Label_Implement(labelName);

        }

    }

    public Variable convertToVariable(String varName)
    {
        try {
            int serial = convertSerial(varName);
            switch (Character.toUpperCase(varName.charAt(0))) {
                case 'X':
                    return new VariableImplement(VariableType.INPUT, serial);
                case 'Z':
                    return new VariableImplement(VariableType.WORK, serial);
                case 'Y':
                    return Variable.OUTPUT;
                default:
                    throw new IllegalStateException("Unexpected value: " + varName.charAt(0));
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }

    }

    public int convertSerial(String varName) {
        try {
            if (varName == null) {
                throw new IllegalArgumentException("Variable name cannot be null");
            }

            if (varName.equalsIgnoreCase("Y")) {
                return 0;
            }

            if (varName.length() <= 1) {
                throw new IllegalArgumentException("Invalid variable name: " + varName);
            }

            int serial = Integer.parseInt(varName.substring(1));
            if (serial < 0) {
                throw new IllegalArgumentException("Invalid serial number: " + serial);
            }

            return serial;
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Invalid variable name format: " + varName, e);
        }
    }

    public String getArg(SInstruction instruction, String name)
    {
        List<SInstructionArgument> args =
                instruction.getSInstructionArguments().getSInstructionArgument();

        return args.stream()
                .filter(arg -> arg.getName().equals(name))
                .map(SInstructionArgument::getValue)
                .findFirst()
                .orElse(null);
    }

    private <T> T requireArg(SInstruction ins, String key,
                             Function<String, T> mapper, String opName)
    {
        return Optional.ofNullable(getArg(ins, key))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(mapper)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Missing argument '" + key + "' for " + opName));
    }

    public Instruction convertToInstruction(SInstruction Instruction)
    {
        try {

            Variable var = convertToVariable(Instruction.getSVariable());
            LabelInterface label = convertToLabelInterface(Instruction.getSLabel());
            String name = Instruction.getName();
            LabelInterface labelToJump;
            Variable arg;
            long constant;
            switch (name) {
                case "INCREASE":
                    return new Increase(context, var, label);
                case "DECREASE":
                    return new Decrease(context, var, label);

                case "JUMP_NOT_ZERO": {
                    labelToJump = requireArg(Instruction, "JNZLabel",
                            this::convertToLabelInterface, "JUMP_NOT_ZERO");
                    return new JNZ(context, var, label, labelToJump);
                }

                case "NEUTRAL":
                    return new Neutral(context, var, label);

                case "ZERO_VARIABLE":
                    return new Zero_Variable(context, var, label);

                case "ASSIGNMENT": {
                    arg = requireArg(Instruction, "assignedVariable",
                            this::convertToVariable, "ASSIGNMENT");
                    return new Assignment(context, var, arg, label);
                }

                case "CONSTANT_ASSIGNMENT":
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "CONSTANT_ASSIGNMENT");
                    return new Constant_Assignment(context, var, label, constant);

                case "GOTO_LABEL":
                    labelToJump = requireArg(Instruction, "gotoLabel",
                            this::convertToLabelInterface, "GOTO_LABEL");
                    return new Goto_Label(context, var, label, labelToJump);

                case "JUMP_ZERO":
                    labelToJump = requireArg(Instruction, "JZLabel",
                            this::convertToLabelInterface, "JUMP_ZERO");
                    return new Jump_Zero(context, var, label, labelToJump);

                case "JUMP_EQUAL_CONSTANT":
                    labelToJump = requireArg(Instruction, "JEConstantLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_CONSTANT");
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "JUMP_EQUAL_CONSTANT");
                    return new Jump_Equal_Constant(context, var, label, labelToJump, constant);

                case "JUMP_EQUAL_VARIABLE":
                    labelToJump = requireArg(Instruction, "JEVariableLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_VARIABLE");
                    arg = requireArg(Instruction, "variableName",
                            this::convertToVariable, "JUMP_EQUAL_VARIABLE");
                    return new Jump_Equal_Variable(context, var, label, labelToJump, arg);

                default:
                    throw new IllegalStateException("Unsupported_Instruction: " + name);
            }

        }
        catch (RuntimeException e) {/*ToDo: handle Exception};*/}
        System.out.println("yuval was right!!!!!!!!!!\n");
        return null;
    }

}

