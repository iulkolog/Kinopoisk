/**
 * Created by Iulkolog on 20.05.2016.
 */
import dao.InterfaceReflectionJdbcDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.List;

@RunWith(Parameterized.class)
public abstract class ReflectionJdbcDaoTest<Context> {


    protected Class daoClass;
    public abstract InterfaceReflectionJdbcDao dao();
    protected Class someClass;
    public abstract Context context();



    @Test
    public void testInsert() throws Exception {

        dao().insert(someClass);
        dao().selectByKey(someClass);
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testDeleteByKey() throws Exception {
    }

    @Test
    public void selectByKey() throws Exception {

    }


    /*@Test
    public void testSelectAll() throws Exception {
        List list = dao().selectByKey()
    }*/

    public ReflectionJdbcDaoTest(Class clazz, Class someClass) {
        this.daoClass = clazz;
        this.someClass = someClass;
    }
}
