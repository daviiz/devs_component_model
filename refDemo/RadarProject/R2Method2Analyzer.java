package refDemo.RadarProject;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class R2Method2Analyzer extends R2Analyzer{

  public R2Method2Analyzer() {
    this("R2Method2Analyzer");
  }

  public R2Method2Analyzer(String name) {
    super(name);
  }

  public boolean HasR2(){
//    System.out.println("my:" + m_myRadar.getTQ() + " ext:" + m_extRadar.getTQ());

    hasR2 = (m_myRadar.getTQ() >= m_extRadar.getTQ());
    return hasR2;
  }
}