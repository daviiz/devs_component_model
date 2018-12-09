package refDemo.PursuerEvader;

import GenCol.Function;
import GenCol.FunctionIterator;
import GenCol.Pair;
import GenCol.doubleEnt;


public class twoDDerivEnvir extends Function{

public twoDDerivEnvir(){
   super();
}


public void addName(String name){
put(name,new doubleEnt(0));
}

public void setVal(String name, double x, double y){
replace(name, new vect2DEnt(x,y));
}

public void setVal(String name, vect2DEnt val){
replace(name, val);
}

public vect2DEnt getVal(String name){
Object ent = this.assoc(name);
if (ent == null)
return new vect2DEnt(Double.POSITIVE_INFINITY,0);
else return (vect2DEnt)ent;
}

public static void main(String[] args){

twoDDerivEnvir d = new twoDDerivEnvir();
d.addName("voltage");
d.setVal("voltage",3,5);
d.addName("Current");
d.setVal("Current",10,20);


 System.out.println(d.getVal("Current").toString());   /*
 for (Pair   p = ((Pair)(d.getHead()));p != null;p=(Pair)p.getRight()){
       entity   ent = p.getKey();
       String inf = ent.getName();
       System.out.println(inf);
       }                     */

FunctionIterator itr = (FunctionIterator)d.iterator();
        while(itr.hasNext())
       {
   Pair p = (Pair)itr.next();
   Object ent = p.getKey();
   vect2DEnt dent = (vect2DEnt)p.getValue();
   String ind = dent.toString();
   String inf = ent.toString();

   System.out.println("Name :"+inf+" Value : "+ind);
     }

}

}
