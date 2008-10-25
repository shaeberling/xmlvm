/*
 *  XMLVM --- An XML-based Programming Language
 *  Copyright (c) 2004-2005 by Arno Puder
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  For more information, visit the XMLVM Home Page at
 *  http://www.xml11.org/xmlvm/
 */

//---------------------------------------------------------------
// The following classes are a re-implementation of the Java
// classes used by the program being run
//---------------------------------------------------------------


/* Java as IDs for every object. They are e.q. used in toString
   and equals() methods. This is a quick and dirty solution
*/
function IdGen()
{
	IdGen.prefix = "ID_";
	IdGen.value = 0;
	
	
	IdGen.generateNext = function()
	{
		IdGen.value++;
		return (IdGen.prefix + IdGen.value);
	}
}
IdGen();


Array.prototype._toString = function()
{
	if(this.id == undefined)
	{
		this.id = IdGen.generateNext();
	}
	
	return this.id;
}

var domNodeMap = new Array();

function NativeInterfaceDOM()
{
	NativeInterfaceDOM.createNode = function(id, pid, html)
	{
		var node = document.createElement("div");
		domNodeMap[id] = node;
		if (id == pid) {
			// This is a parent. Append it to the top-level root
			var obj = getXML11Root();
			obj.appendChild(node);
		} else {
			domNodeMap[pid].appendChild(node);
		}
		node.innerHTML = html._str;
	}
}
NativeInterfaceDOM();
