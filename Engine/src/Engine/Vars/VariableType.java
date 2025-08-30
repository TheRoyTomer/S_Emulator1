package Engine.Vars;

public enum VariableType
{
    OUTPUT {
        @Override
        public String getVariableRepresentation(int serial) {return "Y";}
    },
    INPUT {
        @Override
        public String getVariableRepresentation(int serial) {
            return "X" + serial;
        }
    },
    WORK {
        @Override
        public String getVariableRepresentation(int serial) {
            return "Z" + serial;
        }
    };

    public abstract String getVariableRepresentation(int serial);
}

