import com.xt.mybatis.bean2.Employee2;
import com.xt.mybatis.bean2.Employee2Example;
import com.xt.mybatis.mapper2.Employee2Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MBGTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    @Test
    public void testMbg () throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }


    @Test
    public void testMyBatis3Simple() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            Employee2Mapper mapper = openSession.getMapper(Employee2Mapper.class);
            List<Employee2> emps = mapper.selectByExample(null);
            System.out.println(emps);

        }finally{
            openSession.close();
        }
    }

    @Test
    public void testMyBatis3() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            Employee2Mapper mapper = openSession.getMapper(Employee2Mapper.class);
            //xxxExample就是封装查询条件的
            //1、查询所有
//            List<Employee2> emps = mapper.selectByExample(null);
            //2、查询员工名字中有e字母的，和员工性别是1的
            //封装员工查询条件的example
            Employee2Example example = new Employee2Example();
            Employee2Example.Criteria criteria = example.createCriteria();
            criteria.andLastNameLike("%t%").andGenderEqualTo("1");

            //创建一个Criteria，这个Criteria就是拼装查询条件
            //select id, last_name, email, gender, d_id from tbl_employee
            //WHERE ( last_name like ? and gender = ? ) or email like "%e%"
            Employee2Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEmailLike("%t%");

            example.or(criteria1);
            List<Employee2> emps = mapper.selectByExample(example);
            System.out.println(emps);

        }finally{
            openSession.close();
        }
    }
}
