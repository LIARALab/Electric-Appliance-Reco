package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;
import ca.uqac.lif.cep.tuples.Tuple;
import ca.uqac.lif.cep.tuples.TupleFixed;

import java.util.*;


public class SetVARPeakToZeroV2 extends SynchronousProcessor {

    public SetVARPeakToZeroV2()
    {
        super(1, 1);
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        Tuple tuples_name = (Tuple) inputs[0];

        String[] list = {"W1-K", "W1-T", "W2-K", "W2-T", "VA1-K", "VA1-T", "VA2-K", "VA2-T"};
        Float[] extracted_values = new Float[8];

        for (int i = 0; i < tuples_name.size(); i++)
        {

            if (i < 4)
            {
                extracted_values[i] = (float) tuples_name.get(list[i]);
            } else{
                extracted_values[i] = 0f;
            }

        }

        TupleFixed tuple = new TupleFixed(list, extracted_values);

        return outputs.add(new Object[] {tuple});
    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new SetVARPeakToZeroV2();
    }

    @Override
    public Class<?> getOutputType(int position)
    {
        if (position == 0)
        {
            return Float.class;
        }

        return null;
    }

}


