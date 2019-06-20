package mainAPI.supTools;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Date;
import java.sql.Timestamp;


public class TransformToJSON extends SynchronousProcessor
{

    public TransformToJSON()
    {
        super(2, 1);
    }

    @Override
    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {

        Date date = new Date();
        long time = date.getTime();
        String ts = new Timestamp(time).toString();

        // Create new JSON Object
        JsonObject electric_recognition_JSON_object = new JsonObject();
        electric_recognition_JSON_object.addProperty("Timestamp", ts);

        ArrayList<String> matrix_result = (ArrayList) inputs[0];
        int appliance_number = matrix_result.size();

        // Create Inner JSON Object
        JsonObject appliance_states = new JsonObject();

        for(int i=0; i < appliance_number; i++) {
            String appliance_name = "Appliance " + (i+1);
            String appliance_current_state = matrix_result.get(i);
//            System.out.println(appliance_current_state);
            appliance_states.addProperty(appliance_name, appliance_current_state);
        }

        electric_recognition_JSON_object.add("Appliance states", appliance_states);

        // Create Inner JSON Object
        ArrayList<String> matrix_recommendation = (ArrayList) inputs[1];
        int appliance_recommendation_number = matrix_recommendation.size();

        JsonObject appliance_recommendation = new JsonObject();

        if(appliance_recommendation_number == 0) {

            electric_recognition_JSON_object.add("Appliance recommendation", appliance_recommendation);

        } else {

            for (int i = 0; i < appliance_recommendation_number; i++) {
                String appliance_name = "Appliance " + (i + 1);
                String appliance_recommended = matrix_recommendation.get(i);
//                System.out.println(appliance_recommended);
                appliance_recommendation.addProperty(appliance_name, appliance_recommended);
            }

            electric_recognition_JSON_object.add("Appliance recommendation", appliance_recommendation);

        }

        outputs.add(new Object[]{ electric_recognition_JSON_object.toString() });

        return true;

    }

    @Override
    public Processor duplicate(boolean with_state)
    {
        return new TransformToJSON();
    }

}
