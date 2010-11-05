/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.command.runtime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.xml.jaxb.util.JaxbListAdapter;
import org.drools.xml.jaxb.util.JaxbUnknownAdapter;

@XmlAccessorType(XmlAccessType.NONE)
public class SetGlobalCommand
    implements
    GenericCommand<Void> {

	@XmlAttribute(required=true)
    private String  identifier;
	
	@XmlElement
	@XmlJavaTypeAdapter(JaxbUnknownAdapter.class)
    private Object  object;

    @XmlAttribute(name="out-identifier")
    private String  outIdentifier;

    @XmlAttribute
    private boolean out;
    
    public SetGlobalCommand() {
	}

    public SetGlobalCommand(String identifier,
                            Object object) {
        this.identifier = identifier;
        this.object = object;
    }

    public Void execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();

        if ( this.out ) {
            ((StatefulKnowledgeSessionImpl) ksession).session.getExecutionResult().getResults().put( (this.outIdentifier != null) ? this.outIdentifier : this.identifier,
                                                                                                     object );
        }

        ksession.setGlobal( this.identifier,
                            this.object );
        return null;
    }

    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getObject() {
        return this.object;
    }
    
    public void setObject( Object object ) {
    	this.object = object;
    }

    public String getOutIdentifier() {
        return this.outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
        this.out = true;
    }

    public boolean isOut() {
        return this.out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public String toString() {
        return "session.setGlobal(" + this.identifier + ", " + this.object + ");";
    }

}
