
package org.xmlvm.iphone;

import org.xmlvm.iphone.internal.Simulator;
import org.xmlvm.iphone.internal.SimulatorDesktop;



public abstract class UIApplication
{
//    static public UIApplication theApplication = null;



    public UIApplication()
    {
//        theApplication = this;
        if (!Simulator.initialized)
            // We run on the desktop
            new SimulatorDesktop();
    }



    public abstract void applicationDidFinishLaunching(
            NSNotification aNotification);




    protected void setStatusBarHidden(boolean flag)
    {
    	Simulator.setStatusBarHidden(flag);
    }



    public static void main(String[] args, Class appClass)
    {
        try {
            UIApplication theApp = (UIApplication) appClass.newInstance();
            theApp.applicationDidFinishLaunching(null);
        }
        catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
