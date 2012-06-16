// Automatically generated by xmlvm2csharp. Do not edit!

using org.xmlvm;

namespace Compatlib.System.Windows.Controls {
public class Panel: global::Compatlib.System.Windows.FrameworkElement {
public virtual global::System.Object getChildren(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Panel: Compatlib.System.Windows.Controls.UIElementCollection getChildren()]
    return children;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Panel: Compatlib.System.Windows.Controls.UIElementCollection getChildren()]
}

public new void @this(){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Panel: void <init>()]
    if (base.element == null)
    {
        base.element = new OverridePanel(this);
    }
    base.@this();
    children.@this();
    children.owner = this;
    children.collection = ((global::System.Windows.Controls.Panel)base.element).Children;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Panel: void <init>()]
}

public virtual void setBackground(Compatlib.System.Windows.Media.Brush n1){
//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Panel: void setBackground(Compatlib.System.Windows.Media.Brush)]
    ((global::System.Windows.Controls.Panel)base.element).Background = n1.brush;
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Panel: void setBackground(Compatlib.System.Windows.Media.Brush)]
}

//XMLVM_BEGIN_WRAPPER[Compatlib.System.Windows.Controls.Panel]
    private Compatlib.System.Windows.Controls.UIElementCollection children = new Compatlib.System.Windows.Controls.UIElementCollection();

    private class OverridePanel : global::System.Windows.Controls.Panel {
        private Panel panel;

        public OverridePanel(Panel panel)
        {
            this.panel = panel;
            this.Loaded += OverridePanel_Loaded;
        }

        void OverridePanel_Loaded(object sender, global::System.Windows.RoutedEventArgs e)
        {
            global::System.Diagnostics.Debug.WriteLine("Loaded fired");

            foreach (global::System.Windows.UIElement child in base.Children)
            {
                global::System.Diagnostics.Debug.WriteLine("Rendering " + child.GetType().Name);
            }

            InvalidateMeasure();
            InvalidateArrange();
        }

        protected override global::System.Windows.Size ArrangeOverride(global::System.Windows.Size finalSize)
        {
            Size size = new Size();
            size.size = finalSize;

            for(int i=0; i<panel.children.getCount(); i++)
            {
                global::Compatlib.System.Windows.UIElement child = (global::Compatlib.System.Windows.UIElement)panel.children._1_1access(i);
                global::Compatlib.System.Windows.Rect rect = new global::Compatlib.System.Windows.Rect();
				int orientation = ((global::Compatlib.System.Windows.Application)global::Compatlib.System.Windows.Application.getCurrent()).getOrientation();
                int x = child.y;
                int y = child.x;
                rect.rect = new global::System.Windows.Rect(x, y, child.element.DesiredSize.Width, child.element.DesiredSize.Height);
                if (child.element is global::System.Windows.Controls.Primitives.Popup)
                {
                    rect.rect = new global::System.Windows.Rect(child.x, child.y, 800, 200);
                }
                //global::System.Diagnostics.Debug.WriteLine("Arranging child: " + rect.rect);
                child.Arrange(rect);
            }

            return panel.ArrangeOverride(size) != null ? ((System.Windows.Size)panel.ArrangeOverride(size)).size : base.ArrangeOverride(finalSize);
        }

        protected override global::System.Windows.Size MeasureOverride(global::System.Windows.Size availableSize)
        {
            Size size = new Size();
            size.size = availableSize;

            for (int i = 0; i < panel.children.getCount(); i++)
            {
                global::Compatlib.System.Windows.UIElement child = (global::Compatlib.System.Windows.UIElement)panel.children._1_1access(i);
                child.element.Measure(new global::System.Windows.Size(child.width, child.height));
                //global::System.Diagnostics.Debug.WriteLine("Measured child: " + child.element.DesiredSize);
            }

            return panel.MeasureOverride(size) != null ? ((System.Windows.Size)panel.MeasureOverride(size)).size : base.MeasureOverride(availableSize);
        }
    }
//XMLVM_END_WRAPPER[Compatlib.System.Windows.Controls.Panel]

} // end of class: Panel

} // end of namespace: Compatlib.System.Windows.Controls
