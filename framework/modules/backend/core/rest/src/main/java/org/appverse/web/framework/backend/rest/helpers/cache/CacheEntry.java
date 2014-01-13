/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

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
package org.appverse.web.framework.backend.rest.helpers.cache;

import java.sql.Date;

import javax.ws.rs.core.EntityTag;

public class CacheEntry {
	private Object o;
	private Long expiration = null;
	private EntityTag etag;
	private int status;

	public CacheEntry()
	{

	}

	public CacheEntry(final Object o, final EntityTag etag, final Long maxAge) {
		this.o = o;
		this.etag = etag;
		if (maxAge != null)
		{
			Date d = new Date(System.currentTimeMillis() + (maxAge * 1000));
			expiration = d.getTime();
		}
	}

	public Object getObject() {
		return o;
	}

	public void setObject(final Object o)
	{
		this.o = o;
	}

	public void setMaxAge(final Long maxAge)
	{
		Date d = new Date(System.currentTimeMillis() + (maxAge * 1000));
		expiration = d.getTime();
	}

	public EntityTag getEtag() {
		return etag;
	}

	public void setEtag(final EntityTag etag)
	{
		this.etag = etag;
	}

	public boolean hasExpired()
	{
		boolean expired = true;
		if (expiration != null && System.currentTimeMillis() < expiration)
			expired = false;
		return expired;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(final int status) {
		this.status = status;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(final Long expiration) {
		this.expiration = expiration;
	}
}
