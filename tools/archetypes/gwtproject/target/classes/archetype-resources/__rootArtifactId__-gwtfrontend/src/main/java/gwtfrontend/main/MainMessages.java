package com.gft.storefront.gwtfrontend.main;

import com.gft.storefront.gwtfrontend.common.ApplicationMessages;
import com.google.gwt.i18n.client.impl.plurals.DefaultRule_en;

public interface MainMessages extends ApplicationMessages {

	String bulkSetupClientContactValidation(String repeatedClientContactsList);

	String bulkSetupClientSplitContactValidation(String repeatedClientContactsList);

	String bulkSetupContactValidation();

	String bulkSetupDBAgentContactValidation(String repeatedDbAgentContactsList);

	String cashflowColumnAttachmentsText(@PluralCount(DefaultRule_en.class) int count);

	String cashflowColumnQueriesPendingText();

	String cashflowColumnQueriesText(@PluralCount(DefaultRule_en.class) int count);

	String cashflowLastUpdate(String time);

	String cashflowQueriesNotAllowed(String status);

	String cashflowStatusWidget(String statusText);

	String cashflowTabStatusText(String status, String date);

	String confirmationWarningTextContactInfo(String field);

	String windowApproveHeader(@PluralCount(DefaultRule_en.class) int approveCount);

	String windowQueryHeader(@PluralCount(DefaultRule_en.class) int approveCount);

	String windowUploadError(String errorDesc);

	String windowUploadFileFormat(String url);

	String windowUploadResultExact(@PluralCount(DefaultRule_en.class) int matchCount);

	String windowUploadResultNear(@PluralCount(DefaultRule_en.class) int matchCount);

	String windowUploadResultNone(@PluralCount(DefaultRule_en.class) int matchCount);
}
