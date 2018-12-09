
package refDemo.PursuerEvader;


import GenCol.FunctionIterator;
import GenCol.Pair;
import GenCol.entity;
import model.modeling.message;
import view.modeling.ViewableAtomic;


public class twoDDerivFn extends ViewableAtomic{

protected twoDDerivEnvir de;
protected double  clock = 0;
protected vect2DEnt outval,lastVal;

public twoDDerivFn(String name){
 super(name+"Deriv");
 de = new twoDDerivEnvir();
  addInport("stop");
  addOutport("out");
}

public twoDDerivFn(){
   this("X");
}


public void addInfluencer(String integratorName){
de.addName(integratorName);
addInport("in"+integratorName);
}

public vect2DEnt valueOf(String integratorName){
return de.getVal(integratorName);
}

public vect2DEnt derivative(){//override
return new vect2DEnt(0,0);
}

public void initialize(){
     clock = 0.0;
     lastVal = new vect2DEnt(INFINITY,INFINITY);
     passivateIn(" "+new vect2DEnt(0,0).toString());
     super.initialize();
 }


public void  deltext(double e,message x)
{

Continue(e);
clock = clock + e;
FunctionIterator itr = (FunctionIterator)de.iterator();
    while(itr.hasNext())
      {
        Pair p = (Pair)itr.next();
        Object ent = p.getKey();
        String inf = ent.toString();

   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"in"+inf,i)) {
         entity en = x.getValOnPort("in"+inf,i);
         vect2DEnt f = (vect2DEnt)en;
         de.setVal(inf,f);
      }
}

        outval = derivative();

      if (!lastVal.equals(outval)){//can't use change
         holdIn(""+outval.toString(), 0);
         lastVal = outval;
         }
        else passivateIn(""+lastVal.toString());

  for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"stop",i)){
     outval = new vect2DEnt(0,0);
     holdIn(""+outval.toString(),0);
     }

}



public void  deltint( )
{
    sigma = INFINITY;
}

public message  out( )
{
   message  m = new message();
   m.add(makeContent("out", outval));
  return m;
}


}





