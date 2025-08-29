package Engine.JAXB.generated;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class XML_Reader
{

    private SProgram sProgram;
    private File XMLfile;

    public XML_Reader(String path)
    {

        File file = new File(path);
        this.XMLfile = Objects.requireNonNull(file, "XMLfile is null");

        if (!XMLfile.exists() || !XMLfile.isFile()) {
            throw new IllegalArgumentException("XML file not found: " + XMLfile.getAbsolutePath());
        }

        String name = XMLfile.getName().toLowerCase(Locale.ROOT);
        if (!name.endsWith(".xml")) {
            throw new IllegalArgumentException("File must end with .xml: " + XMLfile.getAbsolutePath());
        }


        loadXML();
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

    public SProgram getsProgram()
    {
        return sProgram;
    }

    public String getName()
    {
        return sProgram.getName();
    }


    public List<SInstruction> getSInstructionList()
    {
        return this.sProgram.getSInstructions().getSInstruction();
    }



    public String checkLabelValidity()
    {
        List<SInstruction> list = getSInstructionList();

        Set<String> defined = list.stream()
                .map(SInstruction::getSLabel)      // extract the label of each instruction
                .filter(Objects::nonNull)          // skip null labels
                .collect(Collectors.toSet());      // put into a Set for fast lookup
        defined.add("EXIT"); //Adding EXIT because it is the only LABEL that doesn't appears but can be referenced

        List<String> allArgs = list.stream()
                .map(SInstruction::getSInstructionArguments)             // get the arguments block
                .filter(Objects::nonNull)                                // skip if no arguments
                .flatMap(args -> args.getSInstructionArgument().stream())// flatten the list of arguments
                .map(SInstructionArgument::getValue)                     // extract the string value of each argument
                .filter(Objects::nonNull)                                // skip null values
                .toList();                           // gather into a list

        return allArgs.stream()
                .filter(val -> val.startsWith("L"))        // must start with 'L' to be considered a label
                .filter(lbl -> !defined.contains(lbl))     // keep only labels not defined
                .findFirst()                               // return the first invalid label found
                .orElse("");                               // if none found → return empty string
    }
}
