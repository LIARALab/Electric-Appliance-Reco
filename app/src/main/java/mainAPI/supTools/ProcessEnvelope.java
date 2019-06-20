package mainAPI.supTools;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.signal.Limit;
import ca.uqac.lif.cep.signal.Persist;
import ca.uqac.lif.cep.signal.Threshold;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Trim;

public class ProcessEnvelope extends GroupProcessor
{
  public static final int THRESHOLD = 100;
  
  protected static final int RANGE = 80;

  public ProcessEnvelope()
  {
    super(1, 2);
    Fork f = new Fork(2);
    Processor peak_finder = new PeakFinderLocalMaximum2(5);
    Connector.connect(f, 0, peak_finder, 0);

    // Threshold to avoid finding peaks due to noise
    Threshold peak_th = new Threshold(THRESHOLD);
    Connector.connect(peak_finder, peak_th);

    // Dampen to avoid double peaks
    Processor peak_damper = new Limit(10);
    Connector.connect(peak_th, peak_damper);
    Persist peak_persist = new Persist(10);
    Connector.connect(peak_damper, peak_persist);

    Trim discard_peak = new Trim(11); // a ete ajoute
    Connector.connect(peak_persist, discard_peak); // a ete ajoute
    
    // Second branch: plateau detection
    Processor plateau_finder = new PlateauFinder2().setPlateauRange(RANGE).setRelative(true);
    Connector.connect(f, 1, plateau_finder, 0);

    // Threshold to avoid finding plateaus due to noise
    Threshold plateau_th = new Threshold(THRESHOLD);
    Connector.connect(plateau_finder, plateau_th);
    Processor plateau_damper = new Limit(10);
    Connector.connect(plateau_th, plateau_damper);
    Persist plateau_persist = new Persist(10);
    Connector.connect(plateau_damper, plateau_persist);

    Trim discard_plateau = new Trim(11); // a ete ajoute
    Connector.connect(plateau_persist, discard_plateau); // a ete ajoute
    
    // Bundle everything into a group
    addProcessors(f, peak_finder, peak_th, peak_damper, peak_persist, discard_peak, plateau_finder, plateau_th, plateau_damper, plateau_persist, discard_plateau);
    associateInput(0, f, 0);
    associateOutput(0, discard_peak, 0);
    associateOutput(1, discard_plateau, 0);
  }

}
