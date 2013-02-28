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
package org.appverse.web.framework.backend.batch.services.business.impl.live;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.IntegrationDataFilter;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.batch.model.integration.BatchNodeDTO;
import org.appverse.web.framework.backend.batch.services.business.JobRunnerService;
import org.appverse.web.framework.backend.batch.services.integration.BatchNodeRepository;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jobRunnerService")
public class JobRunnerServiceImpl extends AbstractBusinessService implements
		JobRunnerService {

	@AutowiredLogger
	private static Logger logger;

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private BatchNodeRepository batchNodeRepository;

	@Override
	public boolean performNodeDelay(long nodeDelay) throws Exception {
		String host = InetAddress.getLocalHost().getCanonicalHostName();
		IntegrationDataFilter filter = new IntegrationDataFilter();
		filter.addStrictCondition("name", host);
		BatchNodeDTO batchNodeDTO = batchNodeRepository.retrieve(filter);
		if (batchNodeDTO == null) {
			batchNodeDTO = new BatchNodeDTO();
			batchNodeDTO.setName(host);
			batchNodeDTO.setActive("n");
			batchNodeRepository.persist(batchNodeDTO);
		}
		long order = batchNodeDTO.getId();
		long delay = (order - 1) * nodeDelay * 1000;
		if (batchNodeDTO.getActive().equalsIgnoreCase("y")) {
			logger.info("Batch waitting " + (order - 1) * nodeDelay
					+ " seconds");
			Thread.sleep(delay);
			return true;
		} else {
			logger.info("Batch run skipped");
			return false;
		}
	}

	@Override
	public JobExecution runJob(Job job) throws Exception {
		return jobLauncher.run(job,
				job.getJobParametersIncrementer().getNext(new JobParameters()));
	}

	@Override
	public JobExecution runJob(Job job, int postExecutionSleepTime)
			throws Exception {
		if (skipExecution(job.getName(), postExecutionSleepTime)) {
			return null;
		}
		return jobLauncher.run(job,
				job.getJobParametersIncrementer().getNext(new JobParameters()));
	}

	@Override
	public JobExecution runJob(String jobName) throws Exception {
		Job job = jobRegistry.getJob(jobName);
		return jobLauncher.run(job,
				job.getJobParametersIncrementer().getNext(new JobParameters()));
	}

	@Override
	public JobExecution runJob(String jobName, int postExecutionSleepTime)
			throws Exception {
		if (skipExecution(jobName, postExecutionSleepTime)) {
			return null;
		}
		Job job = jobRegistry.getJob(jobName);
		return jobLauncher.run(job,
				job.getJobParametersIncrementer().getNext(new JobParameters()));
	}

	private boolean skipExecution(String jobName, int postExecutionSleepTime) {
		Date startDate = null;
		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName,
				0, 1);

		if (CollectionUtils.isNotEmpty(jobInstances)) {
			// This will retrieve the latest job execution:
			List<JobExecution> jobExecutions = jobExplorer
					.getJobExecutions(jobInstances.get(0));

			if (CollectionUtils.isNotEmpty(jobExecutions)) {
				JobExecution jobExecution = jobExecutions.get(0);
				startDate = jobExecution.getStartTime();
				Long lastExecutionOffsetAsString = (Long) jobExecution
						.getExecutionContext().get("offset");
				long lastExecutionOffset = 0;
				if (lastExecutionOffsetAsString != null) {
					lastExecutionOffset = lastExecutionOffsetAsString
							.longValue();
				} else {
					lastExecutionOffset = TimeZone.getDefault().getOffset(
							new Date().getTime());
				}
				Date currentDate = new Date();
				long offset = TimeZone.getDefault().getOffset(
						new Date().getTime());

				if (startDate != null
						&& currentDate.getTime() - offset
								- (startDate.getTime() - lastExecutionOffset) < postExecutionSleepTime * 1000) {
					logger.debug("Current date offset " + offset);
					logger.debug("Current date with offset "
							+ (currentDate.getTime() - offset));
					logger.debug("Last execution date offset "
							+ lastExecutionOffset);
					logger.debug("Last execution date with offset "
							+ (startDate.getTime() - lastExecutionOffset));
					logger.debug("Current date with offset - start date with offset "
							+ (currentDate.getTime() - offset - (startDate
									.getTime() - lastExecutionOffset)));
					logger.info("Batch executed "
							+ (currentDate.getTime() - offset - (startDate
									.getTime() - lastExecutionOffset)) / 60000
							+ " minutes before");
					logger.info("Batch run skipped ");
					return true;
				}
			}
		}
		logger.info("Batch run started ");
		return false;
	}
}
