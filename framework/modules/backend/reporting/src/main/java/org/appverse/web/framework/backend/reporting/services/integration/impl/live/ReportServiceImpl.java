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
package org.appverse.web.framework.backend.reporting.services.integration.impl.live;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.appverse.web.framework.backend.api.model.integration.ExcelSheetReportData;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.reporting.services.integration.ReportService;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportServiceImpl extends AbstractBusinessService implements
		ReportService {

	@Override
	public byte[] generateExcel(List<ExcelSheetReportData> excelSheetsReportData)
			throws Exception {

		if (excelSheetsReportData == null || excelSheetsReportData.size() == 0) {
			throw new Exception("There are no data to make report.");
		}

		String[] sheetNamesArray = new String[excelSheetsReportData.size()];
		List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
		int i = 0;
		for (ExcelSheetReportData excelSheetReportData : excelSheetsReportData) {
			sheetNamesArray[i] = excelSheetReportData.getSheetName();
			i++;

			JRDataSource reportDataSource = new JRMapCollectionDataSource(
					excelSheetReportData.getSheetData());
			JasperPrint jasperPrint = null;
			if (excelSheetReportData.getSheetReportLocation() != null
					&& !excelSheetReportData.getSheetReportLocation()
							.equals("")) {
				jasperPrint = JasperFillManager.fillReport(
						excelSheetReportData.getSheetReportLocation(),
						excelSheetReportData.getSheetParameters(),
						reportDataSource);
			} else {
				jasperPrint = JasperFillManager.fillReport(
						excelSheetReportData.getSheetReport(),
						excelSheetReportData.getSheetParameters(),
						reportDataSource);
			}
			jasperPrints.add(jasperPrint);
		}

		JasperPrint firstJasperPrint = jasperPrints.get(0);
		if (jasperPrints.size() > 1) {
			for (i = 1; i < jasperPrints.size(); i++) {
				List<JRPrintPage> additionalPages = new ArrayList<JRPrintPage>(
						jasperPrints.get(i).getPages());
				int fistJasperPrintPages = firstJasperPrint.getPages().size();
				for (int count = 0; count < additionalPages.size(); count++) {
					firstJasperPrint.addPage(fistJasperPrintPages,
							additionalPages.get(count));
				}
			}
		}

		JRExporter exporter = new JExcelApiExporter();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,
				firstJasperPrint);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);

		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES,
				sheetNamesArray);
		exporter.setParameter(JExcelApiExporterParameter.IS_COLLAPSE_ROW_SPAN,
				Boolean.TRUE);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32768);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
				outputStream);
		// exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,
		// "C:/development/workspaces/jasper/report1.xls");
		exporter.exportReport();
		return outputStream.toByteArray();
	}
}
