package mainAPI;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.*;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.io.websocket.WebSocketReader;
import ca.uqac.lif.cep.tmf.*;
import ca.uqac.lif.cep.tuples.*;
import ca.uqac.lif.cep.util.*;
import mainAPI.supTools.*;
import mainAPI.supTools.WebSocketServerProcessor;
import org.java_websocket.client.WebSocketClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import util.UtilityMethods;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import java.util.List;

import static ca.uqac.lif.cep.Connector.INPUT;

// http://appsdeveloperblog.com/java-into-json-json-into-java-all-possible-examples/
// https://tecadmin.net/get-current-timestamp-in-java/

public class AppliancesDetectionMooreMachine_V9 {

    public static enum State {OFF, ON, TURN_ON, TURN_OFF}
    public static final float POSITIVE_THRESHOLD = 100;
    public static final float NEGATIVE_THRESHOLD = -100;


    public static void start(String[] args) throws URISyntaxException, IOException {

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////// - Global Initialization nbnm

        WebSocketServerProcessor sender = new WebSocketServerProcessor(51234);

        ///// - Known Appliances
        // Define the path of directory where we can find the signature json files of the known appliances
//        Path currentRelativePath = FileSystems.getDefault().getPath(".");
//        System.out.println(currentRelativePath);
//        File f = new File("C:/Users/Julien/Desktop/Electric/JSON_Signatures");
        File f = new File(args[0]);

        // Create empty lists of ApplianceSignature objects (of the known appliances)
        List<Object> signature_list = UtilityMethods.createList();
        List<Object> interval_list = UtilityMethods.createList();
        List<Object> appliance_name_list = UtilityMethods.createList();

        // Get the path + filename of the signature json files of the known appliances
        File[] files = f.listFiles();

        // Compute the number of signature json files of the known appliances
        int file_number = files.length;

        // Create a list of ApplianceSignature objects (of the known appliances)
        for (int i = 0; i < file_number; i++) {

            JSONParser parser = new JSONParser();
            JSONObject object = null;

            try {
                FileReader r = new FileReader(files[i]);
                object = (JSONObject) parser.parse(r);
            } catch (Exception e1) {
                e1.printStackTrace();
                System.err.println(e1.getMessage());
            }

            // getting the value of the m_onEnvelope
            Map m_onEnvelope = ((Map)object.get("m_onEnvelope"));

            // getting the value of the m_onEnvelope
            Map m_offEnvelope = ((Map)object.get("m_offEnvelope"));

            Tuple on = new TupleMap();
            on.put("W1-K", m_onEnvelope.get("W1-K"));
            on.put("W1-T", m_onEnvelope.get("W1-T"));
            on.put("W2-K", m_onEnvelope.get("W2-K"));
            on.put("W2-T", m_onEnvelope.get("W2-T"));
            on.put("VA1-K", m_onEnvelope.get("VA1-K"));
            on.put("VA1-T", m_onEnvelope.get("VA1-T"));
            on.put("VA2-K", m_onEnvelope.get("VA2-K"));
            on.put("VA2-T", m_onEnvelope.get("VA2-T"));
            Tuple off = new TupleMap();
            off.put("W1-T", m_offEnvelope.get("W1-T"));
            off.put("W2-T", m_offEnvelope.get("W2-T"));
            off.put("VA1-T", m_offEnvelope.get("VA1-T"));
            off.put("VA2-T", m_offEnvelope.get("VA2-T"));
            signature_list.add(new ApplianceSignature(on, off));

            interval_list.add(object.get("m_interval"));

            appliance_name_list.add(object.get("m_applianceName"));

        }

        // Create an empty list of ApplianceMooreMachine objects (of the known appliances)
        List<Object> moore_machine_list = UtilityMethods.createList();

        // Create a list of ApplianceMooreMachine objects (of the known appliances)
        for (int i = 0; i < file_number; i++) {

            ApplianceSignature sig;
            {
                sig = (ApplianceSignature) signature_list.get(i);
                System.out.println(sig);
            }

            int interval = (int) Math.round((double) interval_list.get(i));

            moore_machine_list.add(new ApplianceMooreMachine(sig, interval));

        }

        // Create an empty array of ApplianceMooreMachine.State objects (of the known appliances)
        Class[] moore_machine_states = new Class[file_number];

        // Create a list of ApplianceMooreMachine.State objects (of the known appliances)
        for (int i = 0; i < file_number; i++) {

            moore_machine_states[i] = ApplianceMooreMachine.State.class;

        }

        ///// - Recommendation Appliances
        // Define the path of the directory where we can find the signature json files of the recommendation appliances
//        File recommendation_f = new File(".\\src\\neoelectric\\demo\\JSON_Signatures_Recommendation");
//        File recommendation_f = new File("C:/Users/Julien/Desktop/Electric/JSON_Signatures_Recommendation");
        File recommendation_f = new File(args[1]);

        // Create empty lists of RecommendationApplianceSignature objects (of the recommendation appliances)
        List<Object> recommendation_signature_list = UtilityMethods.createList();
        List<Object> recommendation_interval_list = UtilityMethods.createList();
        List<Object> recommendation_appliance_name_list = UtilityMethods.createList();

        // Get the path + filename of the signature json files of the recommendation appliances
        File[] recommendation_files = recommendation_f.listFiles();

        // Compute the number of signature json files of the recommendation appliances
        int recommendation_file_number = files.length;

        // Create a list of RecommendationApplianceSignature objects (of the recommendation appliances)
        for (int i = 0; i < recommendation_file_number; i++) {

            JSONParser parser = new JSONParser();
            JSONObject object = null;

            try {
                FileReader r = new FileReader(recommendation_files[i]);
                object = (JSONObject) parser.parse(r);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            // getting the value of the m_onEnvelope
            Map m_onEnvelope = ((Map)object.get("m_onEnvelope"));

            // getting the value of the m_onEnvelope
            Map m_offEnvelope = ((Map)object.get("m_offEnvelope"));

            Tuple on = new TupleMap();
            on.put("Wmin", m_onEnvelope.get("W-min"));
            on.put("Wmax", m_onEnvelope.get("W-max"));
            Tuple off = new TupleMap();
            off.put("Wmin", m_offEnvelope.get("W-min"));
            off.put("Wmax", m_offEnvelope.get("W-max"));
            recommendation_signature_list.add(new ApplianceSignature(on, off));

            recommendation_interval_list.add(object.get("m_interval"));

            recommendation_appliance_name_list.add(object.get("m_applianceName"));

        }

        // Create an empty list of RecommendationApplianceMooreMachine objects (of the recommendation appliances)
        List<Object> recommendation_moore_machine_list = UtilityMethods.createList();

        // Create a list of RecommendationApplianceMooreMachine objects (of the recommendation appliances)
        for (int i = 0; i < recommendation_file_number; i++) {

            ApplianceSignature sig;
            {
                sig = (ApplianceSignature) recommendation_signature_list.get(i);
                System.out.println(sig);
            }

            int interval = (int) Math.round((double) recommendation_interval_list.get(i));

            recommendation_moore_machine_list.add(new RecommendationApplianceMooreMachine(sig, interval));

        }

        // Create an empty array of ApplianceMooreMachine.State objects (of the known appliances)
        Class[] recommendation_moore_machine_states = new Class[file_number];

        // Create a list of ApplianceMooreMachine.State objects (of the known appliances)
        for (int i = 0; i < file_number; i++) {

            recommendation_moore_machine_states[i] = RecommendationApplianceMooreMachine.State.class;

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////// - Start Recognition

        ///// - Start the electric signal processes
        // Connect to the lab with web socket
        WebSocketReader reader = new WebSocketReader(new URI("ws://172.24.24.2:6095"));
        reader.start();

        WebSocketClient client = reader.getWebSocketClient();
        client.connect();

        // Extract the values for each json that we received from the web socket connection
        ReaderToString rs = new ReaderToString();
        Connector.connect(reader, 0, rs, 0);

        ///// - Get the values of the component that we need for the appliance recognition
        Fork fork_electric_signal = new Fork(4);
        Connector.connect(rs, 0, fork_electric_signal, 0);

        ApplyFunction get_WL1 = new ApplyFunction(new FetchAttribute("WL1"));
        Connector.connect(fork_electric_signal, 0, get_WL1, 0);
        ApplyFunction get_WL2 = new ApplyFunction(new FetchAttribute("WL2"));
        Connector.connect(fork_electric_signal, 1, get_WL2, 0);
        ApplyFunction get_VARL1 = new ApplyFunction(new FetchAttribute("VARL1"));
        Connector.connect(fork_electric_signal, 2, get_VARL1, 0);
        ApplyFunction get_VARL2 = new ApplyFunction(new FetchAttribute("VARL2"));
        Connector.connect(fork_electric_signal, 3, get_VARL2, 0);

        ///// - Extract peak and plateau on signal
        ProcessAllEnvelopesTuple pae = new ProcessAllEnvelopesTuple("W1", "W2", "VA1", "VA2");
        Connector.connect(get_WL1, 0, pae, 0);
        Connector.connect(get_WL2, 0, pae, 1);
        Connector.connect(get_VARL1, 0, pae, 2);
        Connector.connect(get_VARL2, 0, pae, 3);

        // Set to 0 all components related to the VAR
        SetVARPeakToZeroV2 set_var_peak_to_zeros = new SetVARPeakToZeroV2();
        Connector.connect(pae, set_var_peak_to_zeros);

        Fork fork_envelopes_tuple = new Fork((int) file_number+5);
        Connector.connect(set_var_peak_to_zeros, 0, fork_envelopes_tuple, 0);

        ///// - Recognize appliances (the known appliances)
        ApplyFunction matrix_result = new ApplyFunction(new Bags.ToList(moore_machine_states));

        for (int i = 0; i < file_number; i++) {

            Connector.connect(fork_envelopes_tuple, i, (ApplianceMooreMachine) moore_machine_list.get(i), INPUT);
            Connector.connect((ApplianceMooreMachine) moore_machine_list.get(i), 0, matrix_result, i);

        }

        Fork fork_appliance_recognition = new Fork(2);
        Connector.connect(matrix_result, fork_appliance_recognition);

        AssociateApplianceNames associate_appliance_names = new AssociateApplianceNames(appliance_name_list);
        Connector.connect(fork_appliance_recognition, 0, associate_appliance_names,0);

        TransformToJSON JSON_object = new TransformToJSON();
        Connector.connect(associate_appliance_names, 0, JSON_object, 0);

        ///// - Start the event detection - Generate condition 2
        FunctionTree event_detection_1 = new FunctionTree(Booleans.or,
                new FunctionTree(Booleans.or,
                        new FunctionTree(Booleans.and,
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("W1-T"), StreamVariable.X),new Constant(POSITIVE_THRESHOLD)),
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("W1-K"),StreamVariable.X),new Constant(POSITIVE_THRESHOLD))),
                        new FunctionTree(Booleans.and,
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("VA1-T"), StreamVariable.X),new Constant(POSITIVE_THRESHOLD)),
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("VA1-K"),StreamVariable.X),new Constant(POSITIVE_THRESHOLD)))),
                new FunctionTree(Booleans.or,
                        new FunctionTree(Booleans.and,
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("W2-T"), StreamVariable.X),new Constant(POSITIVE_THRESHOLD)),
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("W2-K"),StreamVariable.X),new Constant(POSITIVE_THRESHOLD))),
                        new FunctionTree(Booleans.and,
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("VA2-T"), StreamVariable.X),new Constant(POSITIVE_THRESHOLD)),
                                new FunctionTree(Numbers.isGreaterThan,
                                        new FunctionTree(new FetchAttribute("VA2-K"),StreamVariable.X),new Constant(POSITIVE_THRESHOLD)))));

        ApplyFunction event_detector_1 = new ApplyFunction(event_detection_1);

        FunctionTree event_detection_2 = new FunctionTree(Booleans.or,
                new FunctionTree(Booleans.or,
                        new FunctionTree(Numbers.isLessThan,
                                new FunctionTree(new FetchAttribute("W1-T"), StreamVariable.X),new Constant(NEGATIVE_THRESHOLD)),
                        new FunctionTree(Numbers.isLessThan,
                                new FunctionTree(new FetchAttribute("W2-T"),StreamVariable.X),new Constant(NEGATIVE_THRESHOLD))),
                new FunctionTree(Booleans.or,
                        new FunctionTree(Numbers.isLessThan,
                                new FunctionTree(new FetchAttribute("VA1-T"), StreamVariable.X),new Constant(NEGATIVE_THRESHOLD)),
                        new FunctionTree(Numbers.isLessThan,
                                new FunctionTree(new FetchAttribute("VA2-T"),StreamVariable.X),new Constant(NEGATIVE_THRESHOLD))));

        ApplyFunction event_detector_2 = new ApplyFunction(event_detection_2);

        Connector.connect(fork_envelopes_tuple, file_number, event_detector_1, 0);
        Connector.connect(fork_envelopes_tuple, file_number + 1, event_detector_2, 0);

        BlackHole bh0 = new BlackHole();
        Connector.connect(event_detector_1, 0, bh0, 0);
        BlackHole bh1 = new BlackHole();
        Connector.connect(event_detector_2, 0, bh1, 0);

        ///// - Generate condition 1 to display recommendation
        ApplianceNumberOn appliance_number_on = new ApplianceNumberOn();
        Connector.connect(fork_appliance_recognition, 1, appliance_number_on,0);

        BlackHole bh2 = new BlackHole();
        Connector.connect(appliance_number_on, 0, bh2, 0);

        ///// - Recognize appliances (the recommendation appliances)
        // Get the values components that we need for the recommendation appliance recognition
        ApplyFunction get_W1T = new ApplyFunction(new FetchAttribute("W1-T"));
        Connector.connect(fork_envelopes_tuple, file_number+2, get_W1T, 0);
        ApplyFunction get_W2T = new ApplyFunction(new FetchAttribute("W2-T"));
        Connector.connect(fork_envelopes_tuple, file_number+3, get_W2T, 0);

        BlackHole bh_pae = new BlackHole();
        Connector.connect(fork_envelopes_tuple,file_number + 4, bh_pae,0);

        Fork fork_W1T = new Fork(recommendation_file_number);
        Connector.connect(get_W1T, fork_W1T);

        Fork fork_W2T = new Fork(recommendation_file_number);
        Connector.connect(get_W2T, fork_W2T);

        ApplyFunction recommendation_matrix_result = new ApplyFunction(new Bags.ToList(recommendation_moore_machine_states));

        for (int i = 0; i < recommendation_file_number; i++) {

            Connector.connect(fork_W1T, i, (RecommendationApplianceMooreMachine) recommendation_moore_machine_list.get(i), 0);
            Connector.connect(fork_W2T, i, (RecommendationApplianceMooreMachine) recommendation_moore_machine_list.get(i), 1);
            Connector.connect((RecommendationApplianceMooreMachine) recommendation_moore_machine_list.get(i), 0, recommendation_matrix_result, i);

        }

        ///// - Display appliance recommendation
        DisplayRecommendationV2 display_recommendation = new DisplayRecommendationV2(recommendation_appliance_name_list);
        Connector.connect(appliance_number_on,0,display_recommendation,0);
        Connector.connect(recommendation_matrix_result,0,display_recommendation,1);

        Connector.connect(display_recommendation, 0, JSON_object,1);
        Connector.connect(JSON_object, sender);

        sender.getWebSocketServer().run();

    }

}
