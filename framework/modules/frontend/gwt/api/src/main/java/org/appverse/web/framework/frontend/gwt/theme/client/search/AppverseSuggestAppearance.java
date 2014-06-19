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
package org.appverse.web.framework.frontend.gwt.theme.client.search;

 import com.google.gwt.core.client.GWT;
 import com.google.gwt.resources.client.ClientBundle;
 import com.google.gwt.resources.client.CssResource;
 import com.google.gwt.resources.client.ImageResource;
 import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
 import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
 import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.impl.gxt.SuggestWidgetImpl.SuggestAppearance;

public class AppverseSuggestAppearance<M> implements SuggestAppearance<M> {

	public interface RiaSuggestResources extends ClientBundle {
		@Source({ "Suggest.css" })
		RiaSuggestStyle css();
		
	    ImageResource iconSearch();
	}

	public interface RiaSuggestStyle extends CssResource {
		String searchItem();
	}

	protected final RiaSuggestResources resources;

	protected final RiaSuggestStyle style;

	public AppverseSuggestAppearance() {
		this(GWT.<RiaSuggestResources> create(RiaSuggestResources.class));
	}

	public AppverseSuggestAppearance(RiaSuggestResources resources) {
		this.resources = resources;
		style = resources.css();
		StyleInjectorHelper.ensureInjected(style, true);
	}

	@Override
	public void render(SafeHtmlBuilder sb, M model, SuggestTemplate<M> template) {
		sb.append(template.render(model, style));

	}

	@Override
	public ImageResource iconSearch() {
		return resources.iconSearch();
	}

}
