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
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.container.ViewportDefaultAppearance;

/**
 * NEW IN APPVERSEWEBTHEME: This class does not exist in original Sencha GXT
 * "Blue" theme. We have added it to overwrite Viewport CSS
 * 
 */
public class AppverseWebViewportDefaultAppearance extends
		ViewportDefaultAppearance {

	public interface AppverseWebViewportResources extends ViewportResources {

		@Source("background.png")
		@ImageOptions(repeatStyle=RepeatStyle.Both)
		public ImageResource background();	  

		@Override
		@Source({ "com/sencha/gxt/theme/base/client/container/Viewport.css",
				"AppverseWebViewport.css" })
		public AppverseWebViewportStyle style();
	}

	public interface AppverseWebViewportStyle extends ViewportStyle {

	}

	public AppverseWebViewportDefaultAppearance() {
		super(
				GWT.<AppverseWebViewportResources> create(AppverseWebViewportResources.class),
				GWT.<ViewportTemplate> create(ViewportTemplate.class));
	}
}
