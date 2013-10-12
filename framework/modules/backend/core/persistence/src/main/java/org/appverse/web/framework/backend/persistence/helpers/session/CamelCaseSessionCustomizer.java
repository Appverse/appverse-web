/*
 Copyright (c) 2013 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the conditions of the AppVerse Public License v2.0
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */
package org.appverse.web.framework.backend.persistence.helpers.session;

import java.sql.SQLException;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.schemaframework.IndexDefinition;

/**
 * <p>This session customizer maps JPA entities camel case names to underscore case.
 * Override with @Table(name = "MY_TABLE_NAME") and @Column(name = "MY_FIELD_NAME")</p>
 * <p>Usage:</p>
 * Add to persistence.xml
 * <pre>
 * {@code
 * <property name="eclipselink.session.customizer" value="org.appverse.web.framework.backend.persistence.helpers.session.CamelCaseSessionCustomizer"/>
 * }
 * </pre>
 *
 * @author Ivan Rodriguez Murillo
 */
public class CamelCaseSessionCustomizer implements SessionCustomizer {
    @Override
    public void customize(Session session) throws SQLException {
        for (ClassDescriptor descriptor : session.getDescriptors().values()) {
            // Only change the table name for non-embedable entities with no
            // @Table already
            if (!descriptor.getTables().isEmpty() && descriptor.getAlias().equalsIgnoreCase(descriptor.getTableName())) {
                String tableName = addUnderscores(descriptor.getTableName());
                descriptor.setTableName(tableName);
                for (IndexDefinition index : descriptor.getTables().get(0).getIndexes()) {
                    index.setTargetTable(tableName);
                }
            }
            for (DatabaseMapping mapping : descriptor.getMappings()) {
                // Only change the column name for non-embedable entities with
                // no @Column already
                if (mapping.getField() != null && !mapping.getAttributeName().isEmpty()
                        && mapping.getField().getName().equalsIgnoreCase(mapping.getAttributeName())) {
                    mapping.getField().setName(addUnderscores(mapping.getAttributeName()));
                }
            }
        }
    }

    private static String addUnderscores(String name) {
        StringBuffer buf = new StringBuffer(name.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i))
                    && Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toUpperCase();
    }

}