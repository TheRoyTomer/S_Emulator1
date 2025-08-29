package EngineObject;

import java.util.Optional;

public record InstructionDTO(
        int lineIndex,
        boolean isSynthetic,
        String label,
        VariableDTO var,
        String name,
        InstructionDTO holder,
        int cycles,
        Optional<VariableDTO> arg,
        Optional<Long> constant,
        Optional<String> labelToJump)
{
    public String getCommandBodyRepresentation()
    {
        String varRep = var.getVarRepresentation();
        String argRep = this.arg.map(VariableDTO::getVarRepresentation).orElse("");;
        String constRep = this.constant.map(Object::toString).orElse("");
        String label2JumpRep = this.labelToJump.map(Object::toString).orElse("");
        return switch (this.name)
        {

            case "INCREASE"-> String.format(" %s <- %s + 1 ", varRep, varRep);
            case "DECREASE"-> String.format(" %s <- %s - 1 ", varRep, varRep);
            case "JUMP_NOT_ZERO"-> String.format(" IF %s != 0 GOTO %s", varRep, label2JumpRep);
            case "NEUTRAL"-> String.format(" %s <- %s ", varRep, varRep);
            case "ASSIGNMENT"-> String.format(" %s <- %s ", varRep, argRep);
            case "CONSTANT_ASSIGNMENT"-> String.format(" %s <- %s ", varRep, constRep);
            case "GOTO_LABEL"-> String.format(" GOTO %s ", label2JumpRep);
            case "JUMP_ZERO"-> String.format(" IF %s = 0 GOTO %s ", varRep, label2JumpRep);
            case "JUMP_EQUAL_CONSTANT"-> String.format("IF %s = %s GOTO %s ", varRep, constRep, label2JumpRep);
            case "JUMP_EQUAL_VARIABLE"-> String.format("IF %s = %s GOTO %s ", varRep, argRep, label2JumpRep);
            case "ZERO_VARIABLE"-> String.format(" %s <- %d ", varRep, 0);
            default ->"";
        };
    }

    public String getInstructionRepresentation()
    {
        return String.format("#<%d> (%s) [%s] %s (%d)",
                this.lineIndex,
                isSynthetic ? "S" : "B",
                this.label,
                this.getCommandBodyRepresentation(),
                this.cycles);
    }

}


