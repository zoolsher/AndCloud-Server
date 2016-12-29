package com.safecode.andcloud.dao;

import com.safecode.andcloud.model.Token;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by zoolsher on 2016/12/28.
 *
 * @author zoolsher
 */
@Repository
public class TokenDao extends BaseDao {
    public void saveOrUpdate(Token token) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(token);
    }

    public Token getInfoViaToken(String tokenid) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery("select to from Token to where to.token = ?1");
        query.setString("1", tokenid);
        try {
            return (Token) query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
}
