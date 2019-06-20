//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package mainAPI.supTools;

import ca.uqac.lif.cep.signal.WindowProcessor;

import java.util.Iterator;
import java.util.Queue;

public class PlateauFinder2 extends WindowProcessor
{
    protected float m_range = 5.0F;
    protected boolean m_plateauFound = false;
    protected boolean m_relative;
    protected double m_lastPlateauFound = 0.0D;

    public PlateauFinder2(int width)
    {
        super(width);
    }

    public PlateauFinder2()
    {
    }

    public PlateauFinder2 setRelative(boolean relative)
    {
        this.m_relative = relative;
        return this;
    }

    public void reset()
    {
        super.reset();
        this.m_plateauFound = false;
    }

    public PlateauFinder2 setPlateauRange(int range)
    {
        this.m_range = (float)range;
        return this;
    }

    protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
    {
        Object[] out_vector = new Object[1];
        float d = ((Number)((Number)inputs[0])).floatValue();
        this.m_values.addElement(d);

        if (this.m_values.size() < this.m_windowWidth)
        {
            return true;
        } else {

            if (this.m_values.size() > this.m_windowWidth)
            {
                this.m_values.remove(0);
            }

            Float f = this.computeOutputValue();
            if (f != null)
            {

                if (!this.m_plateauFound)
                {
                    out_vector[0] = f;
                    this.m_plateauFound = true;
                } else {
                    out_vector[0] = 0;
                }

            }
            else {
                this.m_plateauFound = false;
                out_vector[0] = 0;
            }

            if (this.m_relative && ((Number)out_vector[0]).doubleValue() != 0.0D)
            {
                Number cur_abs = (Number)out_vector[0];
                out_vector[0] = cur_abs.doubleValue() - this.m_lastPlateauFound;
                this.m_lastPlateauFound = cur_abs.doubleValue();
            }

            outputs.add(out_vector);
            return true;
        }

    }

    public Float computeOutputValue()
    {

        if (this.m_values.get(0)>0)
        {
            float sum = 0.0F;
            float min = 100000f;
            float max = -100000F;

            float f;

            for (Iterator var4 = this.m_values.iterator(); var4.hasNext(); max = Math.max(f, max))
            {
                f = (Float) var4.next();
                sum += f;
                min = Math.min(f, min);
            }

            return Math.abs(max - min) > Math.abs(this.m_range) ? null : sum / (float) this.m_values.size();

        } else {

            float sum = 0.0F;
            float min = -10000F;
            float max = 10000F;

            float f;

            for (Iterator var4 = this.m_values.iterator(); var4.hasNext(); max = Math.min(f, max))
            {
                f = (Float) var4.next();
                sum += f;
                min = Math.max(f, min);
            }

            return Math.abs(max - min) > Math.abs(this.m_range) ? null : sum / (float) this.m_values.size();
        }

    }

    public PlateauFinder2 duplicate(boolean with_state)
    {
        PlateauFinder2 out = new PlateauFinder2();
        out.m_relative = this.m_relative;
        out.m_range = this.m_range;

        if (with_state)
        {
            out.m_lastPlateauFound = this.m_lastPlateauFound;
            out.m_plateauFound = this.m_plateauFound;
        }

        return out;
    }

}
