//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package mainAPI.supTools;

import ca.uqac.lif.cep.signal.WindowProcessor;
import ca.uqac.lif.cep.util.Numbers.NumberCast;
import java.util.Iterator;
import java.util.Queue;

public class PeakFinderLocalMaximum2 extends WindowProcessor
{

    protected int m_peakPosition;
    protected int m_numSincePeak;

    public PeakFinderLocalMaximum2()
    {
        this(5);
    }

    public PeakFinderLocalMaximum2(int width)
    {
        super(width);
        this.m_peakPosition = -1;
        this.m_numSincePeak = 0;
    }

    public PeakFinderLocalMaximum2 findDrops(boolean b)
    {
        return this;
    }

    public void reset()
    {
        super.reset();
        this.m_peakPosition = -1;
        this.m_numSincePeak = 0;
    }

    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        float d = NumberCast.getNumber(inputs[0]).floatValue();

        if (this.m_values.size() < this.m_windowWidth) {
            this.m_values.addElement(d);
            if (this.m_values.size() == 1) {
                this.m_maxValue = d;
                this.m_minValue = d;
            } else {
                if ((d*this.m_maxValue > 0) && (Math.abs(d) > Math.abs(this.m_maxValue))) {
                    this.m_maxValue = d;
                    this.m_peakPosition = this.m_values.size() - 1;
                }

                if ((d*this.m_minValue > 0) && (Math.abs(d) < Math.abs(this.m_minValue))) {
                    this.m_minValue = d;
                }

                if ((d*this.m_maxValue < 0) && (this.m_maxValue > 0) && (d < this.m_maxValue)) {
                    this.m_maxValue = d;
                    this.m_peakPosition = this.m_values.size() - 1;
                }

                if ((d*this.m_minValue < 0) && (this.m_minValue > 0) && (d > this.m_minValue)) {
                    this.m_minValue = d;
                }

                if ((d*this.m_minValue < 0) && (this.m_maxValue < 0) && (d > this.m_minValue)) {
                    this.m_maxValue = d;
                    this.m_peakPosition = this.m_values.size() - 1;
                }

                if ((d*this.m_minValue < 0) && (this.m_minValue < 0) && (d < this.m_minValue)) {
                    this.m_minValue = d;
                }

            }

            return true;
        } else {
            this.m_values.remove(0);
            this.m_values.addElement(d);
            --this.m_peakPosition;
            ++this.m_numSincePeak;
            if ((d*this.m_maxValue > 0) && (Math.abs(d) > Math.abs(this.m_maxValue))) {
                this.m_maxValue = d;
                this.m_peakPosition = this.m_windowWidth - 1;
            } else if ((d*this.m_maxValue < 0) && (this.m_maxValue > 0) && (d < this.m_maxValue)) {
                this.m_maxValue = d;
                this.m_peakPosition = this.m_values.size() - 1;
            } else if (this.m_peakPosition < 0) {
                Object[] out_vector;
                for (int i = 0; i < this.m_numSincePeak - 1; ++i) {
                    out_vector = new Object[]{0};
                    outputs.add(out_vector);
                }

                if ((this.m_maxValue < 0) && (this.m_minValue < 0)) {

                    float peak_height = this.m_maxValue - this.m_minValue;
//                    System.out.println(peak_height);

                    out_vector = new Object[]{peak_height};
                    outputs.add(new Object[]{peak_height});
                    this.m_maxValue = this.getMaxValue();
                    this.m_minValue = this.getMinValue();
                    this.m_peakPosition = this.getPeakPosition();
                    this.m_numSincePeak = 0;
                    return true;

                } else if ((this.m_maxValue > 0) && (this.m_minValue < 0)) {

                    float peak_height = this.m_maxValue - this.m_minValue;

                    out_vector = new Object[]{peak_height};
                    outputs.add(new Object[]{peak_height});
                    this.m_maxValue = this.getMaxValue();
                    this.m_minValue = this.getMinValue();
                    this.m_peakPosition = this.getPeakPosition();
                    this.m_numSincePeak = 0;
                    return true;

                } else if ((this.m_maxValue < 0) && (this.m_minValue > 0)) {

                    float peak_height = this.m_maxValue - this.m_minValue;

                    out_vector = new Object[]{peak_height};
                    outputs.add(new Object[]{peak_height});
                    this.m_maxValue = this.getMaxValue();
                    this.m_minValue = this.getMinValue();
                    this.m_peakPosition = this.getPeakPosition();
                    this.m_numSincePeak = 0;
                    return true;

                } else {

                    float peak_height = this.m_maxValue - this.m_minValue;

                    out_vector = new Object[]{peak_height};
                    outputs.add(new Object[]{peak_height});
                    this.m_maxValue = this.getMaxValue();
                    this.m_minValue = this.getMinValue();
                    this.m_peakPosition = this.getPeakPosition();
                    this.m_numSincePeak = 0;
                    return true;

                }

            }

            return true;
        }
    }

    public int getPeakPosition() {
        double value = 0.0D;
        int cur_pos = 0;
        int peak_pos = 0;

        for(Iterator var5 = this.m_values.iterator(); var5.hasNext(); ++cur_pos) {
            double d = (double)(Float)var5.next();
            if (cur_pos == 0) {
                value = d;
            }

            if (d > value) {
                value = d;
                peak_pos = cur_pos;
            }
        }

        return peak_pos;
    }

    public PeakFinderLocalMaximum2 duplicate(boolean with_state) {
        PeakFinderLocalMaximum2 pflm = new PeakFinderLocalMaximum2(this.m_windowWidth);
        if (with_state) {
            pflm.m_numSincePeak = this.m_numSincePeak;
            pflm.m_peakPosition = this.m_peakPosition;
        }

        return pflm;
    }

    public Float computeOutputValue() {
        return null;
    }
}
