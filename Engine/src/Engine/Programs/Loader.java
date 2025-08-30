package Engine.Programs;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
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

import java.util.*;
import java.util.function.Function;

public class Loader
{
    private final Program destProgram;
    private final Context context;

    public Loader(Program destProgram)
    {
        this.destProgram = destProgram;
        this.context = destProgram.getContext();
    }

    public void load(String path)
    {
        XML_Reader reader = new XML_Reader(path);
        loadFromReader(reader);
    }

    public void loadFromReader(XML_Reader reader)
    {
        context.clearMaps();

        destProgram.setName(reader.getName());
        List<SInstruction> newInstructions = reader.getSInstructionList();
        destProgram.setInstructions(convertToInstructionList(newInstructions));
        destProgram.initProgram();
    }

    public List<Instruction> convertToInstructionList(List<SInstruction> fromJAXB)
    {
        return fromJAXB.stream().map(this::convertToInstruction).toList();
    }


    public LabelInterface convertToLabelInterface(String labelName)
    {
        if (labelName == null) {
            labelName = "";
        }

        LabelInterface label = switch (labelName.toUpperCase()) {
            case "EXIT" -> FixedLabels.EXIT;
            case "" -> FixedLabels.EMPTY;
            default -> new Label_Implement(labelName);
        };

        if (label instanceof Label_Implement) {
            context.setInMapL((Label_Implement) label, 0L);
        }
        return label;
    }

    public Variable convertToVariable(String varName)
    {
        char varSign = Character.toUpperCase(varName.charAt(0));
        int serial = convertSerial(varName);
        Variable var = switch (varSign) {

            case 'X' -> new VariableImplement(VariableType.INPUT, serial);
            case 'Z' -> new VariableImplement(VariableType.WORK, serial);
            case 'Y' -> Variable.OUTPUT;
            default -> throw new RuntimeException();
        };

        context.setVarValue(var, 0);
        return var;

    }


    public int convertSerial(String varName) {
        if (varName.equalsIgnoreCase("Y"))
        {
            return 0;
        }
        return Integer.parseInt(varName.substring(1));
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
        Variable var = convertToVariable(Instruction.getSVariable());
        LabelInterface label = convertToLabelInterface(Instruction.getSLabel());
        String name = Instruction.getName();
        InstructionData type = InstructionData.valueOf(name);
        LabelInterface labelToJump;
        Variable arg;
        long constant;
        return switch (type) {
                case INCREASE: yield new Increase(context, null , var, label);
                case InstructionData.DECREASE: yield new Decrease(context, null, var, label);

                case JUMP_NOT_ZERO:
                    labelToJump = requireArg(Instruction, "JNZLabel",
                            this::convertToLabelInterface, "JUMP_NOT_ZERO");
                    yield new JNZ(context, null, var, label, labelToJump);

                case NEUTRAL: yield new Neutral(context, null, var, label);

                case ZERO_VARIABLE: yield new Zero_Variable(context, null, var, label);

                case ASSIGNMENT:
                    arg = requireArg(Instruction, "assignedVariable",
                            this::convertToVariable, "ASSIGNMENT");
                    yield new Assignment(context, null, var, arg, label);

                case CONSTANT_ASSIGNMENT:
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "CONSTANT_ASSIGNMENT");
                    yield new Constant_Assignment(context, null, var, label, constant);

                case GOTO_LABEL:
                    labelToJump = requireArg(Instruction, "gotoLabel",
                            this::convertToLabelInterface, "GOTO_LABEL");
                    yield new Goto_Label(context, null, var, label, labelToJump);

                case JUMP_ZERO:
                    labelToJump = requireArg(Instruction, "JZLabel",
                            this::convertToLabelInterface, "JUMP_ZERO");
                    yield new Jump_Zero(context, null, var, label, labelToJump);

                case JUMP_EQUAL_CONSTANT:
                    labelToJump = requireArg(Instruction, "JEConstantLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_CONSTANT");
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "JUMP_EQUAL_CONSTANT");
                    yield new Jump_Equal_Constant(context, null, var, label, labelToJump, constant);

                case JUMP_EQUAL_VARIABLE:
                    labelToJump = requireArg(Instruction, "JEVariableLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_VARIABLE");
                    arg = requireArg(Instruction, "variableName",
                            this::convertToVariable, "JUMP_EQUAL_VARIABLE");
                    yield new Jump_Equal_Variable(context, null, var, label, labelToJump, arg);
            };
    }

}

