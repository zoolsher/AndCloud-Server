package com.safecode.andcloud;

import com.safecode.andcloud.dao.MirrorImageDao;
import com.safecode.andcloud.model.MirrorImage;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Hibernate基础测试
 *
 * @author Sumy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {com.safecode.andcloud.configuration.ApplicationContext.class,
        com.safecode.andcloud.configuration.HibernateConfiguration.class})
public class HibernateTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MirrorImageDao mirrorImageDao;

    @Test
    public void testSave() {
        MirrorImage mirrorImage = new MirrorImage();
        mirrorImage.setName("Android " + new DateTime().toString());
        mirrorImage.setPath("/usr/local/android.img");
        mirrorImage.setNote(null);
        mirrorImageDao.saveOrUpdate(mirrorImage);
    }

}
