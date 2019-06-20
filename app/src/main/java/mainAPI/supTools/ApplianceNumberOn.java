package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;

import java.util.ArrayList;
import java.util.Queue;

public class ApplianceNumberOn extends SynchronousProcessor
{

    public ApplianceNumberOn()
    {
        super(1, 1);
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {

        ArrayList<String> array = (ArrayList) inputs[0];
        int length = array.size();

        int count = 0;

        for (int i = 0; i < length; i++)
        {

            String string_1 = String.valueOf(array.get(i));

            if (string_1.equals("TURN_ON"))
            {
                count = count + 1;
            }
            else if (string_1.equals("ON"))
            {
                count = count + 1;
            }
            else if (string_1.equals("TURN_OFF"))
            {
                count = count + 1;
            }

        }

        outputs.add(new Object[]{ count });
        return true;

    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new ApplianceNumberOn();
    }

}
