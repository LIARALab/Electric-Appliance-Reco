package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AssociateApplianceNames extends SynchronousProcessor
{

    protected List<Object> appliance_names;

    public AssociateApplianceNames(List<Object> appliance_names)
    {
        super(1, 1);
        this.appliance_names = appliance_names;
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {

        ArrayList<String> array = (ArrayList) inputs[0];
        int length = array.size();

        ArrayList<String> associate_appliance_names = new ArrayList<String>();

        for (int i = 0; i < length; i++)
        {
            String string_1 = String.valueOf(array.get(i));

            String final_string = appliance_names.get(i) + " - " + string_1;
            associate_appliance_names.add(final_string);
        }

        outputs.add(new Object[]{ associate_appliance_names });
        // System.out.println(associate_appliance_names); // Fix Large Logs
        return true;

    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new AssociateApplianceNames(appliance_names);
    }

}
