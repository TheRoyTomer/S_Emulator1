package Engine.Instructions_Types;


import Engine.ArchitectureTypes.ArchitectureInterface;
import Engine.ArchitectureTypes.ArchitectureTypeEnum;

public enum InstructionData
{
    INCREASE("INCREASE", 1, ArchitectureTypeEnum.ONE),
    DECREASE("DECREASE", 1, ArchitectureTypeEnum.ONE),
    NEUTRAL("NEUTRAL", 0, ArchitectureTypeEnum.ONE),
    JUMP_NOT_ZERO("JUMP_NOT_ZERO", 2, ArchitectureTypeEnum.ONE),
    ZERO_VARIABLE("ZERO_VARIABLE", 1, ArchitectureTypeEnum.TWO),
    GOTO_LABEL("GOTO_LABEL", 1, ArchitectureTypeEnum.TWO),
    ASSIGNMENT("ASSIGNMENT", 4, ArchitectureTypeEnum.THREE),
    CONSTANT_ASSIGNMENT("CONSTANT_ASSIGNMENT", 2, ArchitectureTypeEnum.TWO),
    JUMP_ZERO("JUMP_ZERO", 2, ArchitectureTypeEnum.THREE),
    JUMP_EQUAL_CONSTANT("JUMP_EQUAL_CONSTANT", 2, ArchitectureTypeEnum.THREE),
    JUMP_EQUAL_VARIABLE("JUMP_EQUAL_VARIABLE", 2, ArchitectureTypeEnum.THREE),
    QUOTE("QUOTE", 5, ArchitectureTypeEnum.FOUR),
    JUMP_EQUAL_FUNCTION("JUMP_EQUAL_FUNCTION", 6, ArchitectureTypeEnum.FOUR);


    private final String name;
    private final int cycles;

    private final ArchitectureTypeEnum architectureType;

    InstructionData(String name, int cycles,  ArchitectureTypeEnum architectureType)
    {
        this.name = name;
        this.cycles = cycles;
        this.architectureType = architectureType;
    }


    public String getName()
    {
        return name;
    }

    public int getCycles()
    {
        return cycles;
    }

    public ArchitectureTypeEnum getArchitectureType()
    {
        return architectureType;
    }




}
