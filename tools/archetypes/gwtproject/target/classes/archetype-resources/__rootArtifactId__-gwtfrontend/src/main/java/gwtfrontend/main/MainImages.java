package com.gft.storefront.gwtfrontend.main;

import com.gft.storefront.gwtfrontend.common.ApplicationImages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface MainImages extends ApplicationImages {

	MainImages INSTANCE = GWT.create(MainImages.class);

	@Source("images/add-icon-16x16px.png")
	ImageResource add_icon();

	@Source("images/db-contacts-button.png")
	ImageResource button_contact();

	@Source("images/excel-icon-18x18px.png")
	ImageResource file_type_excel();

	@Source("images/pdf-icon-18x18px.png")
	ImageResource file_type_pdf();

	@Source("images/word-icon-18x18px.png")
	ImageResource file_type_word();

	@Source("images/alertwarning-icon-16x16px.png")
	ImageResource icon_alert();

	@Source("images/approve-icon-14x14px.png")
	ImageResource icon_approve();

	@Source("images/approve-comment-icon-14x17px.png")
	ImageResource icon_approve_comment();

	@Source("images/attach-icon-18x18px.png")
	ImageResource icon_attach();

	@Source("images/contact-icon-18x18px.png")
	ImageResource icon_contact();

	@Source("images/error-icon-16x16.png")
	ImageResource icon_error();

	@Source("images/incomplete-icon-14x14px.png")
	ImageResource icon_incomplete();

	@Source("images/pending-icon-16x16px.png")
	ImageResource icon_pending();

	@Source("images/sucess-icon-18x18px.png")
	ImageResource icon_success();

	@Source("images/remove-icon-16x16px.png")
	ImageResource remove_icon();

}
