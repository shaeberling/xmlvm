// Automatically generated by xmlvm2csharp (do not edit).

using org.xmlvm;
namespace Compatlib.System.Windows.Input {
public class ManipulationStartedEventArgs: global::Compatlib.System.Windows.Input.InputEventArgs {
public void @this(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void <init>()]
    throw new global::org.xmlvm._nNotYetImplementedException("a");
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void <init>()]
}

public virtual bool isHandled(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: boolean isHandled()]
    return ((global::System.Windows.Input.ManipulationStartedEventArgs)base.args).Handled;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: boolean isHandled()]
}

public virtual void setHandled(bool n1){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void setHandled(boolean)]
    ((global::System.Windows.Input.ManipulationStartedEventArgs)base.args).Handled = n1;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void setHandled(boolean)]
}

public virtual global::System.Object getManipulationOrigin(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: Compatlib.System.Windows.Point getManipulationOrigin()]
    global::Compatlib.System.Windows.Point ret = new global::Compatlib.System.Windows.Point();
    ret.point = ((global::System.Windows.Input.ManipulationStartedEventArgs)base.args).ManipulationOrigin;
    return ret;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: Compatlib.System.Windows.Point getManipulationOrigin()]
}

public virtual void setManipulationOrigin(global::Compatlib.System.Windows.Point n1){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void setManipulationOrigin(Compatlib.System.Windows.Point)]
    throw new global::org.xmlvm._nNotYetImplementedException("a");
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs: void setManipulationOrigin(Compatlib.System.Windows.Point)]
}

//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs]
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Input.ManipulationStartedEventArgs]

} // end of class: ManipulationStartedEventArgs

} // end of namespace: Compatlib.System.Windows.Input
