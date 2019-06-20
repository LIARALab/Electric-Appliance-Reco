package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DisplayRecommendationV2 extends SynchronousProcessor
{

    protected List<Object> appliance_names;

    public DisplayRecommendationV2(List<Object> appliance_names)
    {
        super(2, 1);
        this.appliance_names = appliance_names;
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        int count = (int) inputs[0];

        ArrayList<String> matrix_result = (ArrayList) inputs[1];
        int length = matrix_result.size();

        int counter = 0;

        ArrayList<String> recommendation = new ArrayList<String>();

        if (count == 0)
        {

            for (int i = 0; i < length; i++)
            {
                String string_1 = String.valueOf(matrix_result.get(i));

                if (string_1.equals("ON"))
                {
                    counter = counter + 1;
                    recommendation.add((String) appliance_names.get(i));
                }

            }

            if (counter == 0)
            {
                recommendation.add( " - " );
            }

            outputs.add(new Object[]{ recommendation });

        } else {

            recommendation.add( " - " );
            outputs.add(new Object[]{ recommendation });
        }

        return true;

    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new DisplayRecommendationV2(appliance_names);
    }

}
