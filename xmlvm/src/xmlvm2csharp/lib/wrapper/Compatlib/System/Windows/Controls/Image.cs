// Automatically generated by xmlvm2csharp. Do not edit!

using org.xmlvm;

namespace Compatlib.System.Windows.Controls {
public class Image: global::Compatlib.System.Windows.FrameworkElement {
public new void @this(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Image: void <init>()]
    base.element = new global::System.Windows.Controls.Image();
    base.@this();
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Image: void <init>()]
}

public virtual void setSource(Compatlib.System.Windows.Media.Imaging.BitmapImage n1){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Image: void setSource(Compatlib.System.Windows.Media.Imaging.BitmapImage)]
    ((global::System.Windows.Controls.Image)base.element).Source = n1.bitmapImage;
	this.source = n1;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Image: void setSource(Compatlib.System.Windows.Media.Imaging.BitmapImage)]
}

public virtual void setStretch(Compatlib.System.Windows.Media.Stretch n1){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Image: void setStretch(Compatlib.System.Windows.Media.Stretch)]
    ((global::System.Windows.Controls.Image)base.element).Stretch = n1.stretch;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Image: void setStretch(Compatlib.System.Windows.Media.Stretch)]
}

public virtual global::System.Object getSource(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Image: Compatlib.System.Windows.Media.Imaging.BitmapImage getSource()]
    return source;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Image: Compatlib.System.Windows.Media.Imaging.BitmapImage getSource()]
}

//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Image]
private global::Compatlib.System.Windows.Media.Imaging.BitmapImage source;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Image]

} // end of class: Image

} // end of namespace: Compatlib.System.Windows.Controls
