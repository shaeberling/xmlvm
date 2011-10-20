/* XPathFactory.java -- 
   Copyright (C) 2004, 2005  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package javax.xml.xpath;

import gnu.xml.xpath.XPathFactoryImpl;

/**
 * Factory for creating XPath environments.
 *
 * @author (a href='mailto:dog@gnu.org'>Chris Burdess</a)
 * @since 1.3
 */
public abstract class XPathFactory
{

  /**
   * The default property name according to the JAXP specification.
   */
  public static final String DEFAULT_PROPERTY_NAME =
	 	"javax.xml.xpath.XPathFactory";

  /**
   * The default object model URI.
   */
  public static final String DEFAULT_OBJECT_MODEL_URI =
    XPathConstants.DOM_OBJECT_MODEL;

  protected XPathFactory()
  {
  }

  /**
   * Returns a new factory for the default (DOM) object model.
   */
  public static final XPathFactory newInstance()
  {
    try
      {
        return newInstance(DEFAULT_OBJECT_MODEL_URI);
      }
    catch (XPathFactoryConfigurationException e)
      {
        throw new RuntimeException(e.getMessage());
      }
  }

  /**
   * Returns a new factory for the given object model URI.
   * The implementation class to load is the first found in the following
   * locations that advertises support for the given model URI:
   * <ol>
   * <li>the <code>javax.xml.xpath.XPathFactory</code> system property</li>
   * <li>the above named property value in the
   * <code><i>$JAVA_HOME</i>/lib/jaxp.properties</code> file</li>
   * <li>the class name specified in the
   * <code>META-INF/services/javax.xml.xpath.XPathFactory</code> system
   * resource</li>
   * <li>the default factory class</li>
   * </ol>
   * @param uri the object model URI
   */
  public static final XPathFactory newInstance(String uri)
    throws XPathFactoryConfigurationException
  {
    XPathFactory ret = new XPathFactoryImpl();
    if (ret.isObjectModelSupported(uri))
      {
        return ret;
      }
    else
    {
      String msg = "no factories with support for " + uri;
      throw new XPathFactoryConfigurationException(msg);
    }
  }


  /**
   * Indicates whether the specified object model URI is supported by
   * this factory.
   */
  public abstract boolean isObjectModelSupported(String objectModel);

  /**
   * Sets the state of the named feature.
   */
  public abstract void setFeature(String name, boolean value)
    throws XPathFactoryConfigurationException;

  /**
   * Returns the state of the named feature.
   */
  public abstract boolean getFeature(String name)
    throws XPathFactoryConfigurationException;

  /**
   * Sets the XPath variable resolver calback.
   */
  public abstract void setXPathVariableResolver(XPathVariableResolver resolver);

  /**
   * Sets the XPath extension function resolver calback.
   */
  public abstract void setXPathFunctionResolver(XPathFunctionResolver resolver);

  /**
   * Returns a new XPath evaluation environment.
   */
  public abstract XPath newXPath();
  
}
