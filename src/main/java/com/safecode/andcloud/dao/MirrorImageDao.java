package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.MirrorImage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * MirrorImageDao
 *
 * @author Sumy
 */
@Repository
public class MirrorImageDao extends BaseDao {

    public void saveOrUpdate(MirrorImage mirrorImage) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(mirrorImage);
    }

    public MirrorImage findById(int id) {
        return this.getSessionFactory().getCurrentSession().get(MirrorImage.class, id);
    }

}
