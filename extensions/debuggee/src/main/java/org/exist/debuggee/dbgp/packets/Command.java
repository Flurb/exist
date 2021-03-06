/*
 * eXist-db Open Source Native XML Database
 * Copyright (C) 2001 The eXist-db Authors
 *
 * info@exist-db.org
 * http://www.exist-db.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.exist.debuggee.dbgp.packets;

import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.exist.debuggee.DebuggeeJoint;
import org.exist.debuggee.Packet;
import org.exist.debuggee.dbgp.Errors;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public abstract class Command implements Packet {

    private final static Logger LOG = LogManager.getLogger(Packet.class);

    protected final static String namespaces = 
    	"xmlns=\"urn:debugger_protocol_v1\" " +
    	"xmlns:xdebug=\"http://xdebug.org/dbgp/xdebug\" ";
    
    protected final static String xml_declaration = 
    	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    protected IoSession session;

	/**
	 * Unique numerical ID for each command generated by the IDE
	 */
	protected String transactionID; 
	
	public Command(IoSession session, String args) {
		this.session = session;
		
		init();
		
		String[] splited = args.split(" -");
		for (int i = 0; i < splited.length; i++) {
			if (splited[i].length() < 3)
				continue;
			
			String arg = splited[i].substring(0, 1);
			String val = splited[i].substring(2).trim();
			setArgument(arg, val);
		}
	}
	
	protected void init() {
		//used to init original class vars
	}

	public String getTransactionId() {
		return transactionID;
	}

	protected void setArgument(String arg, String val) {
		if (arg.equals("i"))
			transactionID = val;
	}
	
	public IoSession getSession() {
		return session;
	}

	protected DebuggeeJoint getJoint() {
		if (session == null)
			return null;
		
		return (DebuggeeJoint) session.getAttribute("joint");
	}

	public byte[] errorBytes(String commandName) {
		return errorBytes(commandName, Errors.ERR_999, Errors.ERR_999_STR);
	}

	public byte[] errorBytes(String commandName, int errorCode, String errorMessage) {
		String response = xml_declaration +
			"<response " +
				"command=\""+commandName+"\" " +
				"transaction_id=\""+transactionID+"\">" +
				"<error code=\""+String.valueOf(errorCode)+"\">"+
					"<message>"+errorMessage+"</message>"+
				"</error>"+
			"</response>";

		return response.getBytes();
	}

	public abstract void exec();


	public void toDebuggee() {
		session.write(this);
	}
	
	public static Command parse(IoSession session, String message) {
		if (LOG.isDebugEnabled())
			LOG.debug("get message = "+message);
		
		int pos = message.indexOf(" ");
		String command = message.substring(0, pos);
		String args = message.substring(command.length());
		
		if (command.equals("run")) {
			return new Run(session, args);
		
		} else if (command.equals("step_into")) {
			return new StepInto(session, args);
		
		} else if (command.equals("step_over")) {
			return new StepOver(session, args);
		
		} else if (command.equals("step_out")) {
			return new StepOut(session, args);
		
		} else if (command.equals("stop")) {
			return new Stop(session, args);
		
		} else if (command.equals("stack_get")) {
			return new StackGet(session, args);
		
		} else if (command.equals("property_get")) {
			return new PropertyGet(session, args);
		
		} else if (command.equals("context_get")) {
			return new ContextGet(session, args);

        } else if (command.equals("context_names")) {
            return new ContextNames(session, args);
            
		} else if (command.equals("breakpoint_set")) {
			return new BreakpointSet(session, args);
		
		} else if (command.equals("breakpoint_get")) {
			return new BreakpointGet(session, args);
		
		} else if (command.equals("breakpoint_update")) {
			return new BreakpointUpdate(session, args);
		
		} else if (command.equals("breakpoint_remove")) {
			return new BreakpointRemove(session, args);
		
		} else if (command.equals("breakpoint_list")) {
			return new BreakpointList(session, args);
		
		} else if (command.equals("status")) {
            return new Status(session, args);

        } else if (command.equals("stdout")) {
            return new StdOut(session, args);

        } else if (command.equals("stderr")) {
            return new StdErr(session, args);
            
        } else if (command.equals("source")) {
            return new Source(session, args);

        } else if (command.equals("feature_set")) {
			return new FeatureSet(session, args);

        } else if (command.equals("feature_get")) {
			return new FeatureGet(session, args);

        } else if (command.equals("eval")) {
			return new Eval(session, args);

        }
			
		return new Error(command, session, args);
	}

	public static String getFileuri(org.exist.source.Source fileuri) {
//		System.out.println("getFileuri dbgp:"+fileuri.getType()+"://"+fileuri.getKey());
		if (fileuri.type().equalsIgnoreCase("file")) {
			try {
				return new java.io.File(fileuri.path()).toURI().toURL().toString();
			} catch (MalformedURLException e) {
				return "dbgp://"+fileuri.type().toLowerCase()+"/"+fileuri.getKey();
			}
		} else
			return "dbgp://"+fileuri.type().toLowerCase()+fileuri.getKey();
		// jetty.port.jetty
//		return "http://localhost:" + System.getProperty("jetty.port") + "/eXist/admin/admin.xql";
	}
}
