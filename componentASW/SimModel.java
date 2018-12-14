/*
*			Copyright Author
* (USE & RESTRICTIONS - Please read COPYRIGHT file)

* Version		: XX.XX
* Date		: 18-11-29 ����11:27
*/

// Default Package
package componentASW;

import java.awt.Dimension;
import java.awt.Point;

import componentASW.damageAssess.DamageAssessment;
import componentASW.env.Enviroment;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class SimModel extends ViewableDigraph{

    // Add Default Constructor
    public SimModel(){
        this("simModel_1_1");
    }

    // Add Parameterized Constructor
    public SimModel(String name){
        super(name);

// Structure information start
        // Add input port names
        addInport("scen_info");
        addInport("entity_info");

        // Add output port names
        addOutport("wp_launch");
        addOutport("engage_result");

//add test input ports:

        // Initialize sub-components
        ViewableDigraph warship =  new Platform("warship",0,1000,10);
        ViewableDigraph submarine =  new Platform("submarine",0,-1000,20);
        
        ViewableAtomic enviroment =  new Enviroment("enviroment");
        ViewableAtomic damageAssessment =  new DamageAssessment("damageAssessment");
        
        ViewableDigraph decoy_1 =  new Weapon("decoy_1",0,0,0);
        ViewableDigraph decoy_2 =  new Weapon("decoy_2",0,0,0);
//        ViewableDigraph decoy_3 =  new Weapon("decoy_3",0,0,0);
//        ViewableDigraph decoy_4 =  new Weapon("decoy_4",0,0,0);
        
        ViewableDigraph terpedo_1 =  new Weapon("terpedo_1",0,0,0);
        //ViewableDigraph terpedo_2 =  new weapon_1_1("terpedo_2");

        // Add sub-components
        
        add(enviroment);
        add(damageAssessment);
        
        add(warship);
        add(decoy_1);
        add(decoy_2);
//        add(decoy_3);
//        add(decoy_4);
        
        add(submarine);
        
        add(terpedo_1);
        //add(terpedo_2);

        // Add Couplings  
        //addCoupling(ef_1_1, "entity_info", simModel_1_1, "entity_info");
        addCoupling(this,"scen_info",damageAssessment,"scen_info");
        addCoupling(this,"scen_info",enviroment,"scen_info");
        addCoupling(this,"scen_info",warship,"scen_info");
        addCoupling(this,"scen_info",submarine,"scen_info");
        
        addCoupling(damageAssessment,"engage_result",warship,"engage_result");
        addCoupling(damageAssessment,"engage_result",submarine,"engage_result");
        addCoupling(damageAssessment,"engage_result",decoy_1,"engage_result");
        addCoupling(damageAssessment,"engage_result",decoy_2,"engage_result");
//        addCoupling(damageAssessment,"engage_result",decoy_3,"engage_result");
//        addCoupling(damageAssessment,"engage_result",decoy_4,"engage_result");
        addCoupling(damageAssessment,"engage_result",terpedo_1,"engage_result");
        //addCoupling(damageAssessment,"engage_result",terpedo_2,"engage_result");
        addCoupling(damageAssessment,"engage_result",this,"engage_result");
        
        addCoupling(enviroment,"env_info",warship,"env_info");
        addCoupling(enviroment,"env_info",submarine,"env_info");
        addCoupling(enviroment,"env_info",decoy_1,"env_info");
        addCoupling(enviroment,"env_info",decoy_2,"env_info");
//        addCoupling(enviroment,"env_info",decoy_3,"env_info");
//        addCoupling(enviroment,"env_info",decoy_4,"env_info");
        addCoupling(enviroment,"env_info",terpedo_1,"env_info");
        //addCoupling(enviroment,"env_info",terpedo_2,"env_info");
        
        //平台模型发出的耦合关系：
        addCoupling(warship,"move_result",damageAssessment,"move_result");
        addCoupling(warship,"move_result",decoy_1,"move_result");
        addCoupling(warship,"move_result",decoy_2,"move_result");
//        addCoupling(warship,"move_result",decoy_3,"move_result");
//        addCoupling(warship,"move_result",decoy_4,"move_result");
        
        addCoupling(warship,"wp_guidance",decoy_1,"wp_guidance");
        addCoupling(warship,"wp_guidance",decoy_2,"wp_guidance");
//        addCoupling(warship,"wp_guidance",decoy_3,"wp_guidance");
//        addCoupling(warship,"wp_guidance",decoy_4,"wp_guidance");
        
        addCoupling(warship,"wp_launch",this,"wp_launch");
        
        
        
        addCoupling(submarine,"move_result",damageAssessment,"move_result");
        addCoupling(submarine,"move_result",terpedo_1,"move_result");
        addCoupling(submarine,"move_result",terpedo_1,"move_result");
        
        addCoupling(submarine,"wp_guidance",terpedo_1,"wp_guidance");
        addCoupling(submarine,"wp_guidance",terpedo_1,"wp_guidance");
        
        addCoupling(submarine,"wp_launch",this,"wp_launch");
        
        //武器模型发出的耦合关系:
        //诱饵：--所有的武器和平台模型实例都应该接收基础的信号量，因为他们都带有传感器，至于消息在不在接受范围内，请在传感器的OM中体现！
        addCoupling(decoy_1,"move_result",enviroment,"move_result");
        addCoupling(decoy_1,"move_result",submarine,"move_result");
        addCoupling(decoy_2,"move_result",enviroment,"move_result");
        addCoupling(decoy_2,"move_result",submarine,"move_result");
//        addCoupling(decoy_3,"move_result",enviroment,"move_result");
//        addCoupling(decoy_3,"move_result",submarine,"move_result");
//        addCoupling(decoy_4,"move_result",enviroment,"move_result");
//        addCoupling(decoy_4,"move_result",submarine,"move_result");
        addCoupling(terpedo_1,"move_result",enviroment,"move_result");
        addCoupling(terpedo_1,"move_result",warship,"move_result");
        
        addCoupling(terpedo_1,"guidance_info",warship,"guidance_info");
        

// Structure information end
        initialize();
        }
	    public void layoutForSimView() {
			preferredSize = new Dimension(2300, 1600);
			((ViewableComponent) withName("warship")).setPreferredLocation(new Point(360, 15));
			((ViewableComponent) withName("submarine")).setPreferredLocation(new Point(320, 1100));
			((ViewableComponent) withName("terpedo_1")).setPreferredLocation(new Point(320, 580));
			((ViewableComponent) withName("decoy_1")).setPreferredLocation(new Point(1200, 15));
			((ViewableComponent) withName("decoy_2")).setPreferredLocation(new Point(1200, 900));
//			((ViewableComponent) withName("decoy_3")).setPreferredLocation(new Point(350, 15));
//			((ViewableComponent) withName("decoy_4")).setPreferredLocation(new Point(350, 15));
			
			((ViewableComponent) withName("enviroment")).setPreferredLocation(new Point(15, 15));
			((ViewableComponent) withName("damageAssessment")).setPreferredLocation(new Point(15, 300));
		}

    }
