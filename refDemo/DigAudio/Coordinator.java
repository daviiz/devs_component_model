package refDemo.DigAudio;

import GenCol.doubleEnt;
import GenCol.entity;
import GenCol.intEnt;
import model.modeling.content;
import model.modeling.message;
import refDemo.realDevs;



public class Coordinator extends realDevs
{
  protected static final int NUM_FILTERS = 4;
  protected static final int L = 256;

  protected double fs; //sampling frequency

  protected double[] fc1, fc1_1, fc1_2, fc2, fc2_1, fc2_2; //cut-off frequency and transtion frequencies for filters
  protected double[] wc1, wc2; //angular cut-off frequency for filter 1
  protected int[] M, filter_type, window_type; //number of filter taps
  protected double[] h;
  //protected double[][] f1_coeff;//filter1 co-efficients
  protected int counter; //for clock sync

  //filter type
  //LPF - 1
  //HPF - 2
  //BPF - 3
  //BSF - 4

  //window type
  //Hamming Window - 1

  public Coordinator()
  {
    this("Coordinator");
  }

  public Coordinator(String name)
  {
    super(name);

    addInport("start");
    addInport("load");
    addInport("filter_type");
    addInport("window_type");
    addInport("fc1");
    addInport("fc1_1");
    addInport("fc1_2");
    addInport("fc2");
    addInport("fc2_1");
    addInport("fc2_2");
    addInport("fs");

    addInport("fft_done");

    addOutport("dataR");
    addOutport("dataI");
    addOutport("row");
    addOutport("rd");
    addOutport("wr");
    addOutport("out");
    addOutport("N");
    addOutport("ostart");
    addOutport("filter1_en");
    addOutport("odone");

    addTestInput("start", new entity("start"));
    addTestInput("load", new entity("load"));

    filter_type = new int[NUM_FILTERS];
    window_type = new int[NUM_FILTERS];
    M = new int[NUM_FILTERS];
    fc1 = new double[NUM_FILTERS];
    fc1_1 = new double[NUM_FILTERS];
    fc1_2 = new double[NUM_FILTERS];
    fc2 = new double[NUM_FILTERS];
    fc2_1 = new double[NUM_FILTERS];
    fc2_2 = new double[NUM_FILTERS];
    wc1 = new double[NUM_FILTERS];
    wc2 = new double[NUM_FILTERS];
  }

  public void initialize()
  {
    super.initialize();
    passivateIn("idle");
  }

  public void deltext(double e, message x)
  {
    Continue(e);
    if (phaseIs("idle"))
    {
      if (somethingOnPort(x, "start"))
      {
        counter = 0;
        holdIn("load", INFINITY);
      }
    }
    else if (phaseIs("load"))
    {
      if (somethingOnPort(x, "load"))
      {
        //System.out.println(counter);


        if (counter == 0)
          fs = getRealValueOnPort(x, "fs");


        filter_type[counter] = getIntValueOnPort(x, "filter_type");
        window_type[counter] = getIntValueOnPort(x, "window_type");
        fc1[counter] = getRealValueOnPort(x, "fc1");
        fc1_1[counter] = getRealValueOnPort(x, "fc1_1");
        fc1_2[counter] = getRealValueOnPort(x, "fc1_2");
        fc2[counter] = getRealValueOnPort(x, "fc2");
        fc2_1[counter] = getRealValueOnPort(x, "fc2_1");
        fc2_2[counter] = getRealValueOnPort(x, "fc2_2");

        holdIn("load", 0);
      }
    }
    else if (phaseIs("wait_fft_filter1"))
    {
      if (somethingOnPort(x, "fft_done"))
      {
        holdIn("disable_filter1", 0);
      }
    }
  }

  public void deltint()
  {
    if (phaseIs("load"))
    {
      counter++;

      if (counter < NUM_FILTERS)
        holdIn("load", INFINITY);
      else
        holdIn("filter_1_calc", 0);
    }
    else  if (phaseIs("filter_1_calc"))
    {
      M[0] = CalculateM(fs, fc1_1[0], fc1_2[0]);
      wc1[0] = 2*Math.PI*fc1[0]/fs;
      wc2[0] = 2*Math.PI*fc2[0]/fs;
      h = CalculateCoeffs(L, M[0], wc1[0], wc2[0], filter_type[0], window_type[0]);

      //DisplayArray(h);
      counter = 0;
      holdIn("load_filter_1", 1);
    }
    else if (phaseIs("load_filter_1"))
    {
      counter++;

      if (counter < M[0])
        holdIn("load_filter_1", 1);
      else
        holdIn("do_fft", 0);
    }
    else if (phaseIs("do_fft"))
    {
      passivateIn("wait_fft_filter1");
    }
    else if (phaseIs("disable_filter1"))
    {
      holdIn("ready", 0);
    }
    else if (phaseIs("ready"))
    {
      //holdIn("inputs", 1);
      passivateIn("idle");
    }
  }


  public message out()
  {
    content con_0 = new content("", new entity(""));
    content con_1 = new content("", new entity(""));
    content con_2 = new content("", new entity(""));
    content con_3 = new content("", new entity(""));

    message op = new message();
    if (phaseIs("load_filter_1"))
    {
      //System.out.println(h[counter] + " + j0.0");
      if (counter == 0)
      {
        content con_4 = new content("N", new intEnt(L));
        op.add(con_4);
      }
      con_0=makeContent("dataR", new doubleEnt(h[counter]));
      con_1=makeContent("dataI", new doubleEnt(0));
      con_2=makeContent("wr", new entity("write"));
      con_3=makeContent("row", new intEnt(counter));
    }
    else if (phaseIs("do_fft"))
    {
      con_0=makeContent("ostart", new entity("start"));
      con_1=makeContent("filter1_en", new intEnt(1));
    }
    else if (phaseIs("disable_filter1"))
    {
      con_0=makeContent("filter1_en", new intEnt(0));
    }
    else if (phaseIs("ready"))
    {
      con_0=makeContent("filter1_en", new intEnt(1));
      con_1=makeContent("odone", new entity("done"));
      //con_2=makeContent("N", new intEnt(L + M[0]));
    }


    op.add(con_0);
    op.add(con_1);
    op.add(con_2);
    op.add(con_3);

    return op;
  }

  protected int CalculateM(double fs, double fc1, double fc2)
  {
    int _temp = (int)(Math.floor(3.3*fs/(fc2-fc1)));
    return ((_temp%2)==0)?_temp-1:_temp;
  }

  protected double HammingWindow(double param)
  {
    return (0.54 - 0.46*Math.cos(param));
  }

  protected double LPF(double wc, int param)
  {
    return (wc/Math.PI)*((param==0)?1:(Math.sin(wc*param)/(wc*param)));
  }


  protected double[] CalculateCoeffs(int N, int M, double wc1, double wc2, int filter_type, int window_type)
  {
    //int N = L;

    //N = L + M -1;
    double[] coeffs = new double[N];
    double[] window = new double[M];
    double[] filt = new double[M];
    int i;

    for(i=0; i<M; i++)
    {
      switch (window_type)
      {
        case 1:
          window[i] = HammingWindow(2*Math.PI*i/(M-1));
          break;
      }


      switch (filter_type)
      {
        case 1:
          filt[i] = LPF(wc1, i - Math.round(M/2));
          break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            break;
      }
      coeffs[i] = filt[i]*window[i];
    }
    for(i=M; i<N; i++)
      coeffs[i] = 0;

    return coeffs;
  }

  protected void DisplayArray(double[] data)
  {
    for(int i=0; i<data.length; i++)
      System.out.println("val " + i + " = " + data[i]);
  }
  protected void DisplayArray(int[] data)
  {
    for(int i=0; i<data.length; i++)
      System.out.println("val " + i + " = " + data[i]);
  }

  protected void DisplayStateRegs()
  {
    System.out.println("-------------fs------------");
      System.out.println(fs);
      System.out.println("---------------------------");
      System.out.println("--------filter type--------");
      DisplayArray(filter_type);
      System.out.println("---------------------------");
      System.out.println("--------window type--------");
      DisplayArray(window_type);
      System.out.println("---------------------------");
      System.out.println("-----------fc1-------------");
      DisplayArray(fc1);
      System.out.println("---------------------------");
      System.out.println("-----------fc1_1-----------");
      DisplayArray(fc1_1);
      System.out.println("---------------------------");
      System.out.println("-----------fc1_2-----------");
      DisplayArray(fc1_2);
      System.out.println("---------------------------");
      System.out.println("------------fc2------------");
      DisplayArray(fc2);
      System.out.println("---------------------------");
      System.out.println("------------fc2_1----------");
      DisplayArray(fc2_1);
      System.out.println("---------------------------");
      System.out.println("-----------fc2_2-----------");
      DisplayArray(fc2_2);
      System.out.println("---------------------------");
  }
}