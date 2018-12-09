package refDemo.RadarProject;

public class R2Method1Analyzer extends R2Analyzer{

  public R2Method1Analyzer() {
    this("R2Method1Analyzer");
  }

  public R2Method1Analyzer(String name) {
    super(name);
  }

  public boolean HasR2(){

    if(hasR2){
      //   System.out.println("my:" + m_myRadar.getTQ() + " ext:" + m_extRadar.getTQ());
      hasR2 = ((m_myRadar.getTQ() + 2 > m_extRadar.getTQ()) ||
              (m_myRadar.getTQ() == m_extRadar.getTQ()));
    }
    else{
      hasR2 = ( (m_myRadar.getTQ() >= m_extRadar.getTQ() + 2) ||
            (m_myRadar.getTQ() == m_extRadar.getTQ()));
    }
    return hasR2;
  }
}