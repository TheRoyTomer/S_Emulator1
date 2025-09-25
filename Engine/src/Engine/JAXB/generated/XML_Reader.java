package Engine.JAXB.generated;

import Engine.Programs.Convertor;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XML_Reader
{

    private SProgram sProgram;
    private File XMLfile;

    public XML_Reader(File file)
    {

        this.XMLfile = file;
        loadXML();
        Set<String> availableFunctions = new HashSet<>();
        availableFunctions.add(getName());
        for (SFunction func : getFunctions())
        {
            availableFunctions.add(func.getName());
        }

        validateLabels(getSInstructionList(), getName());
        validateFunctionReferences(getSInstructionList(), getName(), availableFunctions);

        for (SFunction func : getFunctions()) {
            validateLabels(func.getSInstructions().getSInstruction(), func.getName());
            validateFunctionReferences(func.getSInstructions().getSInstruction(), func.getName(), availableFunctions);
        }


    }

    private void loadXML()
    {
        try {
            JAXBContext context = JAXBContext.newInstance(SProgram.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            this.sProgram = (SProgram) unmarshaller.unmarshal(XMLfile);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to unmarshal XML from: " + XMLfile.getAbsolutePath() + "\n", e);
        }
    }


    public String getName()
    {
        return sProgram.getName();
    }


    public List<SInstruction> getSInstructionList()
    {
        return this.sProgram.getSInstructions().getSInstruction();
    }


    public List<SFunction> getFunctions()
    {
        return (sProgram.getSFunctions() != null)
                ? sProgram.getSFunctions().sFunction
                : new ArrayList<>();
    }



    private void validateLabels(List<SInstruction> instructions, String funcName)
    {
        Set<String> defined = instructions.stream()
                .map(SInstruction::getSLabel)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        defined.add("EXIT");

        List<String> allArgs = instructions.stream()
                .map(SInstruction::getSInstructionArguments)
                .filter(Objects::nonNull)
                .flatMap(args -> args.getSInstructionArgument().stream())
                .map(SInstructionArgument::getValue)
                .filter(Objects::nonNull)
                .toList();

        allArgs.stream()
                .filter(val -> val.startsWith("L") && !defined.contains(val))
                .findFirst()
                .ifPresent(lbl -> {
                    throw new RuntimeException("Undefined label " + lbl + " in " + funcName + "!");
                });
    }
    private void validateFunctionReferences(List<SInstruction> instructions, String funcName, Set<String> availableFunctions)
    {
        Set<String> usedFunctions = new HashSet<>();
        for (SInstruction inst : instructions)
        {
            String funcArgs = getFunctionArguments(inst);
            if (!funcArgs.isEmpty())
            {
                List<String> strBetweenCommas = Arrays.stream(funcArgs.replace("(", "")
                        .replace(")", "")
                        .split(",")).toList();
                for (String str : strBetweenCommas)
                {
                    if (!Convertor.isArgVar(str) && !availableFunctions.contains(str))
                    {
                        throw new RuntimeException(funcName + " has reference to " + str + " that doesnt exist!");
                    }
                }
            }

        }
    }

    private String getFunctionArguments(SInstruction instruction)
    {
        if (instruction.getSInstructionArguments() == null) {return "";}

        return instruction.getSInstructionArguments().getSInstructionArgument().stream()
                .filter(arg -> "functionArguments".equals(arg.getName()))
                .map(SInstructionArgument::getValue)
                .findFirst()
                .orElse("");
    }
}
