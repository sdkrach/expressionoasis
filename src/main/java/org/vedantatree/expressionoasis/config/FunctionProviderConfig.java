/**	
 *  Copyright (c) 2005-2014 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software. You can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL 
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES 
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
 *  OR OTHER DEALINGS IN THE SOFTWARE.See the GNU Lesser General Public License 
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Please consider to contribute any enhancements to upstream codebase. 
 *  It will help the community in getting improved code and features, and 
 *  may help you to get the later releases with your changes.
 */
package org.vedantatree.expressionoasis.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.extensions.FunctionProvider;


/**
 * Represents the configuration of a function provider, and builds the function provider
 * that it represents.
 * 
 * @author Kris Marwood
 * @version 1.0
 */

@Root(name = "functionProvider")
public class FunctionProviderConfig
{

	/* the name of the function provider class represented by this object */
	@Attribute(name = "className")
	private final String					className;

	/* the arguments that must be passed to the function provider's constructor */
	@ElementList(name = "constructorArgs", entry = "constructorArg", required = false)
	private final List<ConstructorArgument>	args;

	/* an instance of the function provider defined by this class */
	private FunctionProvider				provider	= null;

	/**
	 * Constructor arguments are annotated so that the Simple XML framework can use
	 * constructor injection to create an immutable object.
	 * 
	 * @param className the name of the function provider class to instantiate
	 * @param args the arguments to pass to the constructor during instantiation
	 */
	public FunctionProviderConfig(
			@Attribute(name = "className") String className,
			@ElementList(name = "constructorArgs", entry = "constructorArg", required = false) List<ConstructorArgument> args )
	{

		this.className = className;
		this.args = args;
	}

	/**
	 * Gets an instance of the function provider that is defined in the XML config
	 * this object represents.
	 * 
	 * @return an instance of the function provider as defined in the XML config
	 *         this object represents.
	 * 
	 * @throws ExpressionEngineException
	 */
	public FunctionProvider getFunctionProvider()
	{
		if( provider == null )
		{
			try
			{
				Class providerClass = Class.forName( className );
				Constructor constructor = providerClass.getConstructor( getConstructorParameterTypes() );
				provider = (FunctionProvider) constructor.newInstance( getConstructorParameterValues() );
			}
			catch( InstantiationException e )
			{
				throw new RuntimeException( e );
			}
			catch( IllegalAccessException e )
			{
				throw new RuntimeException( e );
			}
			catch( IllegalArgumentException e )
			{
				throw new RuntimeException( e );
			}
			catch( InvocationTargetException e )
			{
				throw new RuntimeException( e );
			}
			catch( NoSuchMethodException e )
			{
				throw new RuntimeException( e );
			}
			catch( SecurityException e )
			{
				throw new RuntimeException( e );
			}
			catch( ClassNotFoundException e )
			{
				throw new RuntimeException( e );
			}
		}
		return provider;
	}

	/**
	 * Gets the parameters required by the function provider class's constructor
	 * (as defined by the args tags in the XML config).
	 * 
	 * @return an array of classes in the function provider constructor's signature
	 * @throws ClassNotFoundException
	 */
	private Class[] getConstructorParameterTypes() throws ClassNotFoundException
	{
		Class[] types;

		if( args != null )
		{
			types = new Class[args.size()];
			for( int i = 0; i < args.size(); i++ )
			{
				types[i] = Class.forName( args.get( i ).getClassName() );
			}
		}
		else
		{
			types = new Class[0];
		}

		return types;
	}

	/**
	 * Gets the values to pass to the function provider class's constructor
	 * (as defined by the args tags in the XML config).
	 * 
	 * @return an array of objects to pass to the function provider class's constructor
	 * @throws ClassNotFoundException
	 */
	private Object[] getConstructorParameterValues() throws ClassNotFoundException
	{
		Object[] values;

		if( args != null )
		{
			values = new Class[args.size()];
			for( int i = 0; i < args.size(); i++ )
			{
				if( args.get( i ).getClassName().equals( "java.lang.Class" ) )
				{
					values[i] = Class.forName( args.get( i ).getValue() );
				}
				else
				{
					values[i] = args.get( i ).getValue();
				}
			}
		}
		else
		{
			values = new Class[0];
		}
		return values;
	}
}
