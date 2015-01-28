/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.adaptors.jdbc;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.validation.constraints.NotNull;

/**
 * Class that if provided a query that returns a RQUi (parameter of query
 * must be username) will do one time password validation by using URQUi software
 * If they match, then authentication succeeds.
 *
 * @author Jonathan Bell
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class URQUiAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

    @NotNull
    private String sql;

    @NotNull
    private String url;

    @NotNull
    private String urquiid;

    @NotNull
    private String urquikey;

    protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials) throws AuthenticationException {
        final String username = getPrincipalNameTransformer().transform(credentials.getUsername());
        final String password = credentials.getPassword();   // This will be URQUi

        try {
            final String dbRQUi = getJdbcTemplate().queryForObject(this.sql, String.class, username);

            urquiCheck uc = new urquiCheck();

            try {
                return uc.validate(urquiid, urquikey, dbRQUi, password, url);
            } catch (Exception ex) {
                return false;
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            // this means the username was not found.
            return false;
        }
    }

    /**
     * @param sql The sql to set.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }

    /**
     * @param url The fieldUser to set.
     */
    public final void seturl(final String url) {
        this.url = url;
    }

    /**
     * @param urquiid The fieldUser to set.
     */
    public final void seturquiid(final String urquiid) {
        this.urquiid = urquiid;
    }

    /**
     * @param urquikey The fieldUser to set.
     */
    public final void seturquikey(final String urquikey) {
        this.urquikey = urquikey;
    }

}
