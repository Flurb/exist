/*
 * eXist Open Source Native XML Database
 * Copyright (C) 2001-2018 The eXist Project
 * http://exist-db.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.exist.backup;

import org.exist.TestUtils;
import org.exist.backup.restore.listener.ConsoleRestoreListener;
import org.exist.test.ExistWebServer;
import org.exist.xmldb.XmldbURI;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class XMLDBBackupTest {

    @ClassRule
    public static final ExistWebServer existWebServer = new ExistWebServer(true, false, true, true);
    private static final String PORT_PLACEHOLDER = "${PORT}";

    private static final String COLLECTION_NAME = "test-xmldb-backup-restore";

    @ClassRule
    public static final TemporaryFolder tempFolder = new TemporaryFolder();

    @Parameterized.Parameters(name = "{0}")
    public static java.util.Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "local", XmldbURI.EMBEDDED_SERVER_URI.toString() },
                { "remote", "xmldb:exist://localhost:" + PORT_PLACEHOLDER + "/xmlrpc" }
        });
    }

    @Parameterized.Parameter
    public String apiName;

    @Parameterized.Parameter(value = 1)
    public String baseUri;

    private static final String DOC1_NAME = "doc1.xml";
    private final String doc1Content = "<timestamp>" + System.currentTimeMillis() + "</timestamp>";

    private final String getBaseUri() {
        return baseUri.replace(PORT_PLACEHOLDER, Integer.toString(existWebServer.getPort()));
    }

    @Test
    public void backupRestore() throws XMLDBException, SAXException, IOException, URISyntaxException, ParserConfigurationException {
        final XmldbURI collectionUri = XmldbURI.create(getBaseUri()).append("/db").append(COLLECTION_NAME);
        final String backupFilename = "test-xmldb-backup-" + System.currentTimeMillis() + ".zip";

        // backup the collection
        final Path backupFile = backup(backupFilename, collectionUri);

        // delete the collection
        deleteCollection(collectionUri);

        // restore the collection
        restore(backupFile, XmldbURI.create(getBaseUri()).append("/db"));

        // check restore has restored the collection
        final Collection testCollection = DatabaseManager.getCollection(collectionUri.toString(), TestUtils.ADMIN_DB_USER, TestUtils.ADMIN_DB_PWD);
        assertNotNull(testCollection);
        final Resource doc1 = testCollection.getResource(DOC1_NAME);
        assertNotNull(doc1);
        final Source expected = Input.fromString(doc1Content).build();
        final Source actual = Input.fromString(doc1.getContent().toString()).build();
        final Diff diff = DiffBuilder.compare(expected)
                .withTest(actual)
                .checkForIdentical()
                .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    private Path backup(final String filename, final XmldbURI collectionUri) throws IOException, XMLDBException, SAXException {
        final Path backupFile = tempFolder.newFile(filename).toPath();
        final Backup backup = new Backup(TestUtils.ADMIN_DB_USER, TestUtils.ADMIN_DB_PWD,
                backupFile,
                collectionUri);
        backup.backup(false, null);
        return backupFile;
    }

    private void restore(final Path backupFile, final XmldbURI collectionUri) throws XMLDBException, SAXException, URISyntaxException, ParserConfigurationException, IOException {
        final Restore restore = new Restore();
        restore.restore(new ConsoleRestoreListener(), TestUtils.ADMIN_DB_USER, TestUtils.ADMIN_DB_PWD, null, backupFile, collectionUri.toString());
    }

    private void deleteCollection(final XmldbURI collectionUri) throws XMLDBException {
        final Collection parent = DatabaseManager.getCollection(collectionUri.removeLastSegment().toString(), TestUtils.ADMIN_DB_USER, TestUtils.ADMIN_DB_PWD);
        final CollectionManagementService colService = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
        colService.removeCollection(collectionUri.lastSegment().toString());
    }

    @Before
    public void before() throws XMLDBException {
        final Collection root = DatabaseManager.getCollection(getBaseUri() + "/db", TestUtils.ADMIN_DB_USER, TestUtils.ADMIN_DB_PWD);
        final CollectionManagementService colService = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
        final Collection testCollection = colService.createCollection(COLLECTION_NAME);
        assertNotNull(testCollection);

        final Resource doc1 = testCollection.createResource(DOC1_NAME, XMLResource.RESOURCE_TYPE);
        doc1.setContent(doc1Content);
        testCollection.storeResource(doc1);
    }
}