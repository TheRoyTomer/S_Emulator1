package Engine.Labels;

import java.util.Comparator;
import java.util.TreeSet;
public class LabelComperator
{


    Comparator<LabelInterface> labelComparator = (a, b) -> {
        boolean aImpl = a instanceof Label_Implement;
        boolean bImpl = b instanceof Label_Implement;

        if (aImpl && !bImpl) return -1;
        if (!aImpl && bImpl) return 1;

        if (aImpl && bImpl) {
            String sa = a.getLabelRepresentation();
            String sb = b.getLabelRepresentation();
            int na = parseNumberAfterL(sa);
            int nb = parseNumberAfterL(sb);
            int cmp = Integer.compare(na, nb);
            return (cmp != 0) ? cmp : sa.compareTo(sb);
        }

        if (a == FixedLabels.EMPTY && b == FixedLabels.EXIT) return -1;
        if (a == FixedLabels.EXIT && b == FixedLabels.EMPTY) return 1;

        return 0; //My Doing, delete
    };

    private static int parseNumberAfterL(String s) {
        return Integer.parseInt(s.substring(1));

    }

    LabelComperator() {}
}
