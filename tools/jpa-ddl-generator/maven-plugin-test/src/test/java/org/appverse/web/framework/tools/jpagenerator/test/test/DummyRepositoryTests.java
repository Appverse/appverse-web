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
package org.appverse.web.framework.tools.jpagenerator.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.inject.Inject;

import org.appverse.web.framework.tools.jpagenerator.test.domain.Dummy;
import org.appverse.web.framework.tools.jpagenerator.test.repository.DummyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DummyRepositoryTests {
	@Inject
	private DummyRepository repository;

	@Inject
	private RepositoryTestHelper testHelper;

	@Test
	public void deveFindAllEntities() throws Throwable {
		long expectedNrObjects = 1;
		long actualNr = repository.count();
		assertEquals(expectedNrObjects, actualNr);
	}

	@Test
	public void mustFindEntityById() throws Throwable {
		Long id = 1L;
		Dummy object = repository.findOne(id);
		assertNotNull(object);
	}

	@Test
	public void mustPersistOneEntity() throws Throwable {
		Dummy object = testHelper.createMockDummy(999);
		Dummy saved = repository.save(object);
		assertTrue(saved.getId() > 0);
	}

	@Test
	public void mustUpdateExistingObject() throws Throwable {
		Dummy object = repository.findOne(1L);
		object.setName("Modified dummy object");
		Dummy saved = repository.save(object);
		assertTrue(saved.getId() > 0);
	}

	@SuppressWarnings("unused")
	private void printCurrentDirectory() {
		File dir1 = new File(".");
		File dir2 = new File("..");
		try {
			System.out.println("Current dir : " + dir1.getCanonicalPath());
			System.out.println("Parent  dir : " + dir2.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
