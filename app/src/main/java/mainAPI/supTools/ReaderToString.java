package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;
import ca.uqac.lif.cep.tuples.TupleFixed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class ReaderToString extends SynchronousProcessor
{
    public ReaderToString()
    {
        super(1, 1);
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        // We first extract the data that we need in order to respect the order of storage values
        Number[] extracted_values = new Number[8];

        try {
            JSONArray arr = (JSONArray) new JSONParser().parse((String) inputs[0]);

            Iterator itr1 = arr.iterator();

            while (itr1.hasNext())
            {
                JSONObject obj1 = (JSONObject) itr1.next();
                String name = (String) obj1.get("Name");

                if (name.equals("W"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[0] = value;
                }

                if (name.equals("W L1"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[1] = value;
                }

                if (name.equals("W L2"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[2] = value;
                }

                if (name.equals("W L3"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[3] = value;
                }

                if (name.equals("VAR "))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[4] = value;
                }

                if (name.equals("VAR L1"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[5] = value;
                }

                if (name.equals("VAR L2"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[6] = value;
                }

                if (name.equals("VAR L3"))
                {
                    Number value = (Number) obj1.get("Value");
                    extracted_values[7] = value;
                }

            }

            int counter = 0;

            for (int i = 0; i < 8; i++)
            {
                if (extracted_values[i] == null)
                {
                    counter = counter + 1;
                }
            }

            // If it is null the processor returns an empty object
            if (counter == 0)
            {
                String[] list = {"W", "WL1", "WL2", "WL3", "VAR", "VARL1", "VARL2", "VARL3"};

                TupleFixed tuple = new TupleFixed(list, extracted_values);

                outputs.add(new Object[]{ tuple });
                return true;
            } else { // Else it returns a tuple
                outputs.add(new Object[]{});
                return true;
            }

        } catch (Exception exc)
        {
            System.out.println(exc);
            outputs.add(new Object[]{});
            return true;
        }

    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new ReaderToString();
    }

}
