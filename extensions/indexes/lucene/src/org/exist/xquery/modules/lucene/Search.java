/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-07 The eXist Project
 *  http://exist-db.org
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
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * \$Id\$
 */
package org.exist.xquery.modules.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import org.exist.dom.QName;

import org.exist.indexing.lucene.LuceneIndex;
import org.exist.indexing.lucene.LuceneIndexWorker;

import org.exist.memtree.NodeImpl;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;

import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 *  Class implementing the ft:search() method
 * @author Dannes Wessels (dannes@exist-db.org)
 */
public class Search extends BasicFunction {

    private static final Logger logger = Logger.getLogger(Search.class);
    
    /**
     * Function signatures
     */
    public final static FunctionSignature signatures[] = {
        new FunctionSignature(
            new QName("search", LuceneModule.NAMESPACE_URI, LuceneModule.PREFIX),
            "Search for (non-XML) data with lucene",
            new SequenceType[]{
                new FunctionParameterSequenceType("path", Type.STRING, Cardinality.ZERO_OR_MORE,
                "URI paths of documents or collections in database. Collection URIs should end on a '/'."),
                new FunctionParameterSequenceType("query", Type.STRING, Cardinality.EXACTLY_ONE,
                "query string")
            },
            new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE,
                    "All documents that are match by the query"))
    };

    /**
     * Constructor
     */
    public Search(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {

        NodeImpl report = null;
        try {
            // Only match documents that match these URLs 
            List<String> toBeMatchedURIs = new ArrayList<String>();

            // Get first agument, these are the documents / collections to search in
            for (SequenceIterator i = args[0].iterate(); i.hasNext();) {
                Item item = i.nextItem();
                toBeMatchedURIs.add(item.getStringValue());
            }

            // Get second argument, this is the query
            String query = args[1].itemAt(0).getStringValue();

            // Get the lucene worker
            LuceneIndexWorker index = (LuceneIndexWorker) context.getBroker()
                    .getIndexController().getWorkerByIndexId(LuceneIndex.ID);

            // Perform search
            report = index.search(context, toBeMatchedURIs, query);


        } catch (XPathException ex) {
            // Log and rethrow
            logger.error(ex);
            throw ex;
        }

        // Return list of matching files.
        return report;
    }
}