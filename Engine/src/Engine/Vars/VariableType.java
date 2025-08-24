package Engine.Vars;

public enum VariableType
{
    OUTPUT {
        @Override
        public String getVariableRepresentation(int serial) {return "Y";}

        @Override
        public VariableType charToVarType(char sign) {return VariableType.OUTPUT;}
    },
    INPUT {
        @Override
        public String getVariableRepresentation(int serial) {
            return "X" + serial;
        }

        @Override
        public VariableType charToVarType(char sign) {return  VariableType.INPUT;}
    },
    WORK {
        @Override
        public String getVariableRepresentation(int serial) {
            return "Z" + serial;
        }

        @Override
        public VariableType charToVarType(char sign) {return VariableType.WORK;}
    };

    public abstract String getVariableRepresentation(int serial);
    public abstract VariableType charToVarType(char sign);
}

