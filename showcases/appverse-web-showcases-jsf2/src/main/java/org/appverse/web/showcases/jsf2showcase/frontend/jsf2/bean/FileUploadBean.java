/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.jsf2showcase.frontend.jsf2.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ManagedBean
@SessionScoped
public class FileUploadBean implements Serializable {

	private static final long serialVersionUID = 1083575060771610056L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();

	public void paint(OutputStream stream, Object object) throws IOException {
		stream.write(getFiles().get((Integer) object).getData());
		stream.close();
	}

	public void listener(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();
		UploadedImage file = new UploadedImage();
		file.setLength(item.getData().length);
		file.setName(item.getName());
		file.setData(item.getData());
		files.add(file);
	}

	public String clearUploadData() {
		files.clear();
		return null;
	}

	public int getSize() {
		if (getFiles().size() > 0) {
			return getFiles().size();
		} else {
			return 0;
		}
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public ArrayList<UploadedImage> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UploadedImage> files) {
		this.files = files;
	}

	@PostConstruct
	public void initIt() {
		logger.info("*** BEAN POST CONSTRUCTION: " + this.getClass().toString());
	}

	@PreDestroy
	public void cleanUp() {
		logger.info("*** BEAN POST DESTRUCTION: " + this.getClass().toString());
	}
}