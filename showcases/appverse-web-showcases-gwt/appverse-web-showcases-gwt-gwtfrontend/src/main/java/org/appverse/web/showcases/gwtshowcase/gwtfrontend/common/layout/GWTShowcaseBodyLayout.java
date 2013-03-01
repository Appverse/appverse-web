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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.layout;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class GWTShowcaseBodyLayout {

	private static final int LABEL_SHORT_SIZE = 65;
	private static final int LABEL_SIZE = 120;
	private static final String BUTTON_SIZE = "150";
	private static final String BUTTON_SHORT_SIZE = "75";

	private static final VerticalLayoutData bodyLayout, buttonsLayout,
			headerLayout, fieldSetLayout, dualListLayout, checkboxLayout,
			textLayout, basicLayout, toolbarLayout, fullWidthTextLayout,
			shortTextLayout, tabLayout;
	private static final BoxLayoutData boxLayoutButtons, boxLayoutButtonsFlex1,
			centralButtonBoxLayout;
	private static final Margins bodyMargins, headerMargins, buttonsMargins,
			fieldSetMargins, margins0, marginTop, margin5555;

	static {
		bodyMargins = new Margins(15, 45, 25, 45);
		headerMargins = new Margins(25, 0, 0, 25);
		buttonsMargins = new Margins(15, 45, 0, 45);
		fieldSetMargins = new Margins(30, 100, 30, 100);
		margins0 = new Margins(0, 0, 0, 0);
		marginTop = new Margins(10, 0, 0, 0);
		margin5555 = new Margins(5, 5, 5, 5);

		// Header Layout
		headerLayout = new VerticalLayoutData(1, .5d, headerMargins);
		// Body Layout
		bodyLayout = new VerticalLayoutData(1, -1, bodyMargins);
		// General Field layout
		fieldSetLayout = new VerticalLayoutData(-1, -1, fieldSetMargins);
		// Buttons Layout
		buttonsLayout = new VerticalLayoutData(1, -1, buttonsMargins);
		// basic layout ( grids )
		basicLayout = new VerticalLayoutData(1, 1);
		// toolbar layout
		toolbarLayout = new VerticalLayoutData(1, 25);
		// tab layout
		tabLayout = new VerticalLayoutData(-1, -1, margin5555);

		/**** Concrete field type layout ****/
		// dualList layout
		dualListLayout = new VerticalLayoutData(500, -1);
		// checkbox layout
		checkboxLayout = new VerticalLayoutData(140, -1);
		// text layout
		textLayout = new VerticalLayoutData(294, -1);
		shortTextLayout = new VerticalLayoutData(175, -1);
		// full width text layout
		fullWidthTextLayout = new VerticalLayoutData(500, -1);
		/************************************/

		// Central button in fieldset
		centralButtonBoxLayout = new BoxLayoutData(marginTop);

		// Buttons layout
		boxLayoutButtons = new BoxLayoutData(margins0);
		boxLayoutButtonsFlex1 = new BoxLayoutData(margins0);
		boxLayoutButtonsFlex1.setFlex(1);

	}

	public VerticalLayoutData basicLayout() {
		return basicLayout;
	}

	public VerticalLayoutData bodyLayout() {
		return bodyLayout;
	}

	public BoxLayoutData boxLayoutButtons() {
		return boxLayoutButtons;
	}

	public BoxLayoutData boxLayoutButtonsFlex1() {
		return boxLayoutButtonsFlex1;
	}

	public String buttonShortSize() {
		return BUTTON_SHORT_SIZE;
	}

	public String buttonSize() {
		return BUTTON_SIZE;
	}

	public VerticalLayoutData buttonsLayout() {
		return buttonsLayout;
	}

	public Margins buttonsMargins() {
		return buttonsMargins;
	}

	public BoxLayoutData centralButtonBoxLayout() {
		return centralButtonBoxLayout;
	}

	public VerticalLayoutData checkboxLayout() {
		return checkboxLayout;
	}

	public VerticalLayoutData dualListLayout() {
		return dualListLayout;
	}

	public VerticalLayoutData fieldSetLayout() {
		return fieldSetLayout;
	}

	public VerticalLayoutData fullWidthTextLayout() {
		return fullWidthTextLayout;
	}

	public VerticalLayoutData headerLayout() {
		return headerLayout;
	}

	public int labelShortSize() {
		return LABEL_SHORT_SIZE;
	}

	public int labelSize() {
		return LABEL_SIZE;
	}

	public VerticalLayoutData shortTextLayout() {
		return shortTextLayout;
	}

	public VerticalLayoutData tabLayout() {
		return tabLayout;
	}

	public VerticalLayoutData textLayout() {
		return textLayout;
	}

	public VerticalLayoutData toolbarLayout() {
		return toolbarLayout;
	}

}