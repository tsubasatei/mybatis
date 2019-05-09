import com.xt.mybatis.bean.Department;
import com.xt.mybatis.bean.Employee;
import com.xt.mybatis.mapper.DepartmentMapper;
import com.xt.mybatis.mapper.EmployeeMapper;
import com.xt.mybatis.mapper.EmployeeMapperAnnotation;
import com.xt.mybatis.mapper.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1、获取sqlSessionFactory对象:
 * 		解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSession；
 * 		注意：【MappedStatement】：代表一个增删改查的详细信息
 *
 * 2、获取sqlSession对象
 * 		返回一个DefaultSQlSession对象，包含Executor和Configuration;
 * 		这一步会创建Executor对象；
 *
 * 3、获取接口的代理对象（MapperProxy）
 * 		getMapper，使用MapperProxyFactory创建一个MapperProxy的代理对象
 * 		代理对象里面包含了，DefaultSqlSession（Executor）
 * 4、执行增删改查方法
 *
 * 总结：
 * 	1、根据配置文件（全局，sql映射）初始化出Configuration对象
 * 	2、创建一个DefaultSqlSession对象，
 * 		他里面包含Configuration以及
 * 		Executor（根据全局配置文件中的defaultExecutorType创建出对应的Executor）
 *  3、DefaultSqlSession.getMapper（）：拿到Mapper接口对应的MapperProxy；
 *  4、MapperProxy里面有（DefaultSqlSession）；
 *  5、执行增删改查方法：
 *  		1）、调用DefaultSqlSession的增删改查（Executor）；
 *  		2）、会创建一个StatementHandler对象。
 *  			（同时也会创建出ParameterHandler和ResultSetHandler）
 *  		3）、调用StatementHandler预编译参数以及设置参数值;
 *  			使用ParameterHandler来给sql设置参数
 *  		4）、调用StatementHandler的增删改查方法；
 *  		5）、ResultSetHandler封装结果
 *  注意：
 *  	四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler);
 *
 * @throws IOException
 */

/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper	====>  xxMapper.xml
 *
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：将sql抽取出来。
 *
 */
public class EmployeeTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }


    /**
     * 1、根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象 有数据源一些运行环境信息
     * 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。
     * 3、将sql映射文件注册在全局配置文件中
     * 4、写代码：
     * 		1）、根据全局配置文件得到SqlSessionFactory；
     * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
     * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
     * 		3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
     *
     * @throws IOException
     */
    @Test
    public void testGetById () throws IOException {
        SqlSession sqlSession = null;
        try {

            // 2、获取sqlSession实例，能直接执行已经映射的sql语句
            // sql的唯一标识：statement Unique identifier matching the statement to use.
            // 执行sql要用的参数：parameter A parameter object to pass to the statement.
            sqlSession = getSqlSessionFactory().openSession();

            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.getById(2);
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }
    }
    @Test
    public void testGetByIdAnnotation () throws IOException {
        SqlSession sqlSession = null;
        try {

            // 2、获取sqlSession实例，能直接执行已经映射的sql语句
            // sql的唯一标识：statement Unique identifier matching the statement to use.
            // 执行sql要用的参数：parameter A parameter object to pass to the statement.
            sqlSession = getSqlSessionFactory().openSession();

            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapperAnnotation employeeMapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
            Employee employee = employeeMapper.getById(2);
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 测试增删改
     * 1、mybatis允许增删改直接定义以下类型返回值
     * 		Integer、Long、Boolean、void
     * 2、我们需要手动提交数据
     * 		sqlSessionFactory.openSession();===》手动提交
     * 		sqlSessionFactory.openSession(true);===》自动提交
     * @throws IOException
     */
    @Test
    public void testCRUD () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            // 新增
            Employee employee = new Employee(null, "xt", null, "0");
            Long result = mapper.addEmp(employee);
            System.out.println(result);
            System.out.println(employee.getId());

            // 添加
            /*Employee employee = new Employee(4, "to", "to@163.com", "1");
            Boolean flag = mapper.updateEmp(employee);
            System.out.println(flag);*/

            // 删除
//            mapper.deleteEmp(4);
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testParam () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//            Employee emp = mapper.getEmpByIdAndLastName(2, "tom");
            /*Map<String, Object> map = new HashMap<>();
            map.put("id", 2);
            map.put("lastName", "tom");
            Employee emp = mapper.getEmpByMap(map);
            System.out.println(emp);*/

            List<Employee> emps = mapper.getEmpsByLastNameLike("%t%");
            System.out.println(emps);

            Map<String, Object> empMap = mapper.getEmpByIdReturnMap(6);
            System.out.println(empMap);

            Map<Integer, Employee> returnMap = mapper.getEmpByLastNameLikeReturnMap("%t%");
            System.out.println(returnMap);

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPlus () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapperPlus mapper = sqlSession.getMapper(EmployeeMapperPlus.class);
//            Employee emp = mapper.getEmpById(2);
//            Employee emp = mapper.getEmpAndDept(2);
            Employee emp = mapper.getEmpByIdStep(2);
            System.out.println(emp.getLastName());
//            System.out.println(emp.getDept());


        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testCollection () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//            Department dept = mapper.getDeptByIdPlus(1);
            Department dept = mapper.getDeptByIdStep(1);
            System.out.println(dept.getDeptName());
//            System.out.println(dept.getEmps());


        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testCondition () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        try {
            EmployeeMapperPlus mapper = sqlSession.getMapper(EmployeeMapperPlus.class);
            //select id, last_name, email, gender from employee WHERE id = ? and last_name like ? and gender=?
//            List<Employee> emps = mapper.getEmpsByConditionIf(new Employee(2, "%tom%", null, "1"));

//            List<Employee> emps = mapper.getEmpsByConditionTrim(new Employee(2, "%tom%", null, "1"));

            // select id, last_name, email, gender from employee WHERE last_name like ?
//            List<Employee> emps = mapper.getEmpsByConditionChoose(new Employee(null, "%tom%", null, "1"));
//            System.out.println(emps);

//            mapper.updateEmp(new Employee(2, "tom", "tom@163.com", "1"));

            /*List<Employee> emps = Arrays.asList(new Employee("naruto", "naruto.com", "1"),
                    new Employee("sasuke", "sasuke.com", "1"),
                    new Employee("sakura", "sakura.com", "0"));
            mapper.addEmps(emps);*/

//            List<Employee> emps = mapper.getEmpsByConditionForeach(Arrays.asList(1, 2, 3, 4));

            List<Employee> emps = mapper.getEmpsTestInnerParameter(new Employee(null, "tom", null, null));
            System.out.println(emps);
        } finally {

            sqlSession.close();

        }
    }
}
