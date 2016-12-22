package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.Project;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * 操作Project模型
 *
 * @author sumy
 */
@Repository
public class ProjectDao extends BaseDao {

    public Project findByUserIdAndProjectId(Integer userid, Integer projectid) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select en from Project en where en.user.id = ?1 and en.id = ?2");
        query.setInteger("1", userid);
        query.setInteger("2", projectid);
        Project result = (Project) query.uniqueResult();
        return result;
    }

    public void saveOrUpdate(Project project) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(project);
    }

}
