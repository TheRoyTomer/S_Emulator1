package Engine.Programs;

import Engine.Instructions_Types.B_Type.Decrease;
import Engine.Instructions_Types.B_Type.Increase;
import Engine.Instructions_Types.B_Type.JNZ;
import Engine.Instructions_Types.B_Type.Neutral;
import Engine.Instructions_Types.Instruction;
import Engine.Instructions_Types.InstructionData;
import Engine.Instructions_Types.S_Type.*;
import Engine.JAXB.generated.*;
import Engine.Labels.FixedLabels;
import Engine.Labels.LabelInterface;
import Engine.Labels.Label_Implement;
import Engine.Vars.Variable;
import Engine.Vars.VariableImplement;
import Engine.Vars.VariableType;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
//import java.util.function.Function;

public class Loader
{
    private final Program destProgram;
    private final Context context;

    public Loader(Program destProgram)
    {
        this.destProgram = destProgram;
        this.context = destProgram.getContext();
    }

    public void load(File file)
    {
        XML_Reader reader = new XML_Reader(file);
        loadFromReader(reader);

    }

    public void loadFromReader(XML_Reader reader)
    {
        context.clearMaps();
        destProgram.setName(reader.getName());
        destProgram.setFunctions(convertToFunctionsList(reader.getFunctions()));
        List<SInstruction> newInstructions = reader.getSInstructionList();
        destProgram.setInstructions(convertToInstructionList(newInstructions, context));
        destProgram.initProgram();
    }

    public List<Instruction> convertToInstructionList(List<SInstruction> fromJAXB, Context myContext)
    {
        return fromJAXB.stream().map(inst -> convertToInstruction(inst, myContext)).toList();
    }


    public LabelInterface convertToLabelInterface(String labelName)
    {
        //If label is null, means that it is the empty label.
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
                             java.util.function.Function<String, T> mapper, String opName)
    {
        return Optional.ofNullable(getArg(ins, key))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(mapper)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Missing argument '" + key + "' for " + opName));
    }

    public List<String> argumentsToStringList(String text) {
        return Arrays.stream(text.split(","))
                .map(s -> s.replaceAll("^[()]+|[()]+$", ""))
                .collect(Collectors.toList());
    }

    public Instruction convertToInstruction(SInstruction Instruction, Context myContext)
    {
        Variable var = convertToVariable(Instruction.getSVariable());
        LabelInterface label = convertToLabelInterface(Instruction.getSLabel());
        String name = Instruction.getName();
        InstructionData type = InstructionData.valueOf(name);
        LabelInterface labelToJump;
        Variable arg;
        String funcName;
        List<String> functionArguments;
        long constant;
        return switch (type) {
                case INCREASE: yield new Increase(myContext, null , var, label);
                case InstructionData.DECREASE: yield new Decrease(myContext, null, var, label);

                case JUMP_NOT_ZERO:
                    labelToJump = requireArg(Instruction, "JNZLabel",
                            this::convertToLabelInterface, "JUMP_NOT_ZERO");
                    yield new JNZ(myContext, null, var, label, labelToJump);

                case NEUTRAL: yield new Neutral(myContext, null, var, label);

                case ZERO_VARIABLE: yield new Zero_Variable(myContext, null, var, label);

                case ASSIGNMENT:
                    arg = requireArg(Instruction, "assignedVariable",
                            this::convertToVariable, "ASSIGNMENT");
                    yield new Assignment(myContext, null, var, arg, label);

                case CONSTANT_ASSIGNMENT:
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "CONSTANT_ASSIGNMENT");
                    yield new Constant_Assignment(myContext, null, var, label, constant);

                case GOTO_LABEL:
                    labelToJump = requireArg(Instruction, "gotoLabel",
                            this::convertToLabelInterface, "GOTO_LABEL");
                    yield new Goto_Label(myContext, null, var, label, labelToJump);

                case JUMP_ZERO:
                    labelToJump = requireArg(Instruction, "JZLabel",
                            this::convertToLabelInterface, "JUMP_ZERO");
                    yield new Jump_Zero(myContext, null, var, label, labelToJump);

                case JUMP_EQUAL_CONSTANT:
                    labelToJump = requireArg(Instruction, "JEConstantLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_CONSTANT");
                    constant = requireArg(Instruction, "constantValue",
                            Long::parseLong, "JUMP_EQUAL_CONSTANT");
                    yield new Jump_Equal_Constant(myContext, null, var, label, labelToJump, constant);

                case JUMP_EQUAL_VARIABLE:
                    labelToJump = requireArg(Instruction, "JEVariableLabel",
                            this::convertToLabelInterface, "JUMP_EQUAL_VARIABLE");
                    arg = requireArg(Instruction, "variableName",
                            this::convertToVariable, "JUMP_EQUAL_VARIABLE");
                    yield new Jump_Equal_Variable(myContext, null, var, label, labelToJump, arg);

            case QUOTE:
                funcName = requireArg(Instruction, "functionName", String::toString, "QUOTE");
                functionArguments = requireArg(Instruction, "functionArguments", this::argumentsToStringList, "QUOTE");
                yield new Quote(myContext, null, var, functionArguments, destProgram.getFunctionByName(funcName), label);

        };
    }

    public List<Function> convertToFunctionsList(List<SFunction> functions)
    {
        List<Function> res = functions.stream().map(this::ConverToFunction).toList();
        functions.forEach(this::setOneFunctionInstructions);
        return res;
    }

    public Function ConverToFunction(SFunction  sFunction)
    {
        Function res = new Function(sFunction.getUserString());
        res.setName(sFunction.getName());
        destProgram.setFunctionInMap(res);
        return res;
    }

    public void setOneFunctionInstructions(SFunction sFunction)
    {
        Function func = destProgram.getFunctionByName(sFunction.getName());
        List<SInstruction> newInstructions = sFunction.getSInstructions().getSInstruction();
        func.setInstructions(convertToInstructionList(newInstructions, func.getContext()));
    }

}

