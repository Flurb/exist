/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03 Wolfgang M. Meier
 *  wolfgang@exist-db.org
 *  http://exist.sourceforge.net
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */
package org.exist.xpath.value;

import org.exist.xpath.XPathException;

/**
 * @author wolf
 */
public class JavaObjectValue extends AtomicValue {

	private Object object;
	
	public JavaObjectValue(Object object) {
		this.object = object;
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#getType()
	 */
	public int getType() {
		return Type.JAVA_OBJECT;
	}
	
	public Object getObject() {
		return object;
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Sequence#getStringValue()
	 */
	public String getStringValue() {
		return String.valueOf(object);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Sequence#convertTo(int)
	 */
	public AtomicValue convertTo(int requiredType) throws XPathException {
		if(requiredType == Type.JAVA_OBJECT)
			return this;
		throw new XPathException("cannot convert Java object to " + Type.getTypeName(requiredType));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#compareTo(int, org.exist.xpath.value.AtomicValue)
	 */
	public boolean compareTo(int operator, AtomicValue other)
		throws XPathException {
		throw new XPathException("cannot compare Java object to " + Type.getTypeName(other.getType()));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#compareTo(org.exist.xpath.value.AtomicValue)
	 */
	public int compareTo(AtomicValue other) throws XPathException {
		throw new XPathException("cannot compare Java object to " + Type.getTypeName(other.getType()));
	}

}
