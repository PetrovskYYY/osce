// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.KeywordDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect KeywordIntegrationTest_Roo_IntegrationTest {
    
    declare @type: KeywordIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: KeywordIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: KeywordIntegrationTest: @Transactional;
    
    @Autowired
    private KeywordDataOnDemand KeywordIntegrationTest.dod;
    
    @Test
    public void KeywordIntegrationTest.testCountKeywords() {
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", dod.getRandomKeyword());
        long count = ch.unibas.medizin.osce.domain.Keyword.countKeywords();
        org.junit.Assert.assertTrue("Counter for 'Keyword' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void KeywordIntegrationTest.testFindKeyword() {
        ch.unibas.medizin.osce.domain.Keyword obj = dod.getRandomKeyword();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Keyword.findKeyword(id);
        org.junit.Assert.assertNotNull("Find method for 'Keyword' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Keyword' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void KeywordIntegrationTest.testFindAllKeywords() {
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", dod.getRandomKeyword());
        long count = ch.unibas.medizin.osce.domain.Keyword.countKeywords();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Keyword', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<ch.unibas.medizin.osce.domain.Keyword> result = ch.unibas.medizin.osce.domain.Keyword.findAllKeywords();
        org.junit.Assert.assertNotNull("Find all method for 'Keyword' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Keyword' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void KeywordIntegrationTest.testFindKeywordEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", dod.getRandomKeyword());
        long count = ch.unibas.medizin.osce.domain.Keyword.countKeywords();
        if (count > 20) count = 20;
        java.util.List<ch.unibas.medizin.osce.domain.Keyword> result = ch.unibas.medizin.osce.domain.Keyword.findKeywordEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Keyword' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Keyword' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void KeywordIntegrationTest.testFlush() {
        ch.unibas.medizin.osce.domain.Keyword obj = dod.getRandomKeyword();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Keyword.findKeyword(id);
        org.junit.Assert.assertNotNull("Find method for 'Keyword' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyKeyword(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Keyword' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void KeywordIntegrationTest.testMerge() {
        ch.unibas.medizin.osce.domain.Keyword obj = dod.getRandomKeyword();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Keyword.findKeyword(id);
        boolean modified =  dod.modifyKeyword(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        ch.unibas.medizin.osce.domain.Keyword merged =  obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Keyword' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void KeywordIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", dod.getRandomKeyword());
        ch.unibas.medizin.osce.domain.Keyword obj = dod.getNewTransientKeyword(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Keyword' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Keyword' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void KeywordIntegrationTest.testRemove() {
        ch.unibas.medizin.osce.domain.Keyword obj = dod.getRandomKeyword();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Keyword' failed to provide an identifier", id);
        obj = ch.unibas.medizin.osce.domain.Keyword.findKeyword(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Keyword' with identifier '" + id + "'", ch.unibas.medizin.osce.domain.Keyword.findKeyword(id));
    }
    
}